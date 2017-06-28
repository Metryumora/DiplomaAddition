package com.chdtu.fitis.dipladd.docmanager;

import com.chdtu.fitis.dipladd.grades.CourseGrade;
import com.chdtu.fitis.dipladd.grades.GradeSummary;
import com.chdtu.fitis.dipladd.grades.StudentSummary;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Component
public class Util {

    private static final String PLACEHOLDER_PREFIX = "#";

    private static WordprocessingMLPackage getTemplate(String name) throws Docx4JException, FileNotFoundException {
        return WordprocessingMLPackage.load(new FileInputStream(new File(name)));
    }

    private static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<>();
        if (obj instanceof JAXBElement) obj = ((JAXBElement<?>) obj).getValue();

        if (obj.getClass().equals(toSearch))
            result.add(obj);
        else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }

        }
        return result;
    }

    private static void replacePlaceholder(WordprocessingMLPackage template, String placeholder, String name) {
        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);

        for (Object text : texts) {
            Text textElement = (Text) text;
            if (textElement.getValue().equals(placeholder)) {
                textElement.setValue(name);
            }
        }
    }

    private static void replacePlaceholders(WordprocessingMLPackage template, Map<String, Object> placeholdersValues) {
        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);
        String value;
        for (Object text : texts) {
            Text textElement = (Text) text;
            if (textElement.getValue().startsWith(PLACEHOLDER_PREFIX)) {
                try {
                    value = placeholdersValues.get(textElement.getValue().trim()).toString();
                    if (value != null) {
                        textElement.setValue(value);
                    }
                } catch (NullPointerException e) {
                    System.out.println(textElement.getValue() + " is null");
                }
            }
        }
    }

    private static void saveTemplate(WordprocessingMLPackage template, String target) throws IOException, Docx4JException {
        File f = new File(target);
        template.save(f);
    }

    public static void replaceInPattern(String filepath, String placeholder, String value) {
        WordprocessingMLPackage template = null;
        try {
            template = getTemplate(filepath);
        } catch (Docx4JException | FileNotFoundException e) {
            e.printStackTrace();
        }
        replacePlaceholder(template, value, placeholder);
        try {
            saveTemplate(template, "processed " + filepath);
        } catch (IOException | Docx4JException e) {
            e.printStackTrace();
        }
    }

    public static void replaceInPattern(String filepath, Map<String, Object> placeholdersValues) {
        replaceInPattern(filepath, placeholdersValues, "processed " + filepath);
    }

    public static void replaceInPattern(String patternFilepath, Map<String, Object> placeholdersValues, String resultFilepath) {
        WordprocessingMLPackage template = null;
        try {
            template = getTemplate(patternFilepath);
        } catch (Docx4JException | FileNotFoundException e) {
            e.printStackTrace();
        }
        replacePlaceholders(template, placeholdersValues);
        try {
            saveTemplate(template, resultFilepath);
        } catch (IOException | Docx4JException e) {
            e.printStackTrace();
        }
    }

    public static void fillDiplomaSupplementPattern(String patternFilepath, StudentSummary summary, String resultFilepath) {
        try {
            WordprocessingMLPackage template = getTemplate(patternFilepath);
            Map<String, Object> placeholdersValues = summary.getPlaceholderDictionary();
            replacePlaceholders(template, placeholdersValues);

            int subjectNumber = 1;

            List<List<Map<String, String>>> listOfGradesMapsLists = new ArrayList<>();
            for (GradeSummary gradeSummary : summary.getGradeSummaries()) {
                List<Map<String, String>> gradesMapList = new ArrayList<>();
                for (CourseGrade courseGrade : gradeSummary.getCourseGrades()) {
                    Map<String, String> gradeMap = new HashMap<>();
                    gradeMap.put("#SubjNumber", String.format("%d", subjectNumber));
                    subjectNumber++;
                    gradeMap.putAll(courseGrade.getPlaceholderDictionary());
                    gradesMapList.add(gradeMap);
                }
                listOfGradesMapsLists.add(gradesMapList);
            }
            fillTableWithGrades(template, listOfGradesMapsLists);
            saveTemplate(template, resultFilepath);

        } catch (IOException | Docx4JException | JAXBException e) {
            e.printStackTrace();
        }
    }

    private static void fillTableWithGrades(WordprocessingMLPackage template,
                                            List<List<Map<String, String>>> tableDataDictionary)
            throws Docx4JException, JAXBException {
        List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);
        Tbl tempTable = getTemplateTable(tables, "#GradesTable");
        List<Object> rows = getAllElementFromObject(tempTable, Tr.class);

        //Doing reverse to avoid saving file after filling each section
        Collections.reverse(tableDataDictionary);

        Tr templateRow = (Tr) rows.get(7);
        int sectionNumber = 3;
        int rowToAddIndex;

        for (List<Map<String, String>> tableSectionDataDictionary : tableDataDictionary) {
            if (sectionNumber > 0) {
                rowToAddIndex = rows.indexOf(findRowInTable(tempTable, "#Section" + sectionNumber)) + 1;
            } else {
                rowToAddIndex = 2;
            }
            for (Map<String, String> replacements : tableSectionDataDictionary) {
                addRowToTable(tempTable, templateRow, rowToAddIndex, replacements);
                rowToAddIndex++;
            }
            sectionNumber--;
        }
        tempTable.getContent().remove(templateRow);
    }


    private static Tr findRowInTable(Tbl table, String templateKey)
            throws Docx4JException, JAXBException {
        for (Object row : table.getContent()) {
            List<?> textElements = getAllElementFromObject(row, Text.class);
            for (Object text : textElements) {
                Text textElement = (Text) text;
                if (textElement.getValue() != null && textElement.getValue().trim().equals(templateKey))
                    return (Tr) row;
            }
        }
        return null;
    }

    private static Tbl getTemplateTable(List<Object> tables, String templateKey)
            throws Docx4JException, JAXBException {
        for (Object tbl : tables) {
            List<?> textElements = getAllElementFromObject(tbl, Text.class);
            for (Object text : textElements) {
                Text textElement = (Text) text;
                if (textElement.getValue() != null && textElement.getValue().trim().equals(templateKey))
                    return (Tbl) tbl;
            }
        }
        return null;
    }

    private static void addRowToTable(Tbl reviewTable, Tr templateRow, Map<String, String> replacements) {
        addRowToTable(reviewTable, templateRow, reviewTable.getContent().size(), replacements);
    }

    private static void addRowToTable(Tbl reviewTable, Tr templateRow, int rowNumber, Map<String, String> replacements) {
        Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
        List<?> textElements = getAllElementFromObject(workingRow, Text.class);
        for (Object object : textElements) {
            Text text = (Text) object;
            String replacementValue = (String) replacements.get(text.getValue().trim());
            if (replacementValue != null)
                text.setValue(replacementValue);
        }
        reviewTable.getContent().add(rowNumber, workingRow);
    }
}

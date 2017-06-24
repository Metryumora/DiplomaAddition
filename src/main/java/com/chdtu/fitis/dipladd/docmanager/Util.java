package com.chdtu.fitis.dipladd.docmanager;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Text;

import javax.xml.bind.JAXBElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Metr_yumora on 24.06.2017.
 */
public class Util {

    private static final String PLACEHOLDER_PREFIX = "PH";

    private static WordprocessingMLPackage getTemplate(String name) throws Docx4JException, FileNotFoundException {
        WordprocessingMLPackage template = WordprocessingMLPackage.load(new FileInputStream(new File(name)));
        return template;
    }

    private static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<Object>();
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

    private static void replacePlaceholders(WordprocessingMLPackage template, Map<String, String> placeholdersValues) {
        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);
        String value;
        for (Object text : texts) {
            Text textElement = (Text) text;
            if (textElement.getValue().startsWith(PLACEHOLDER_PREFIX)) {
                value = placeholdersValues.get(textElement.getValue());
                if (value != null) {
                    textElement.setValue(value);
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

    public static void replaceInPattern(String filepath, Map<String, String> placeholdersValues) {
        WordprocessingMLPackage template = null;
        try {
            template = getTemplate(filepath);
        } catch (Docx4JException | FileNotFoundException e) {
            e.printStackTrace();
        }
        replacePlaceholders(template, placeholdersValues);
        try {
            saveTemplate(template, "processed " + filepath);
        } catch (IOException | Docx4JException e) {
            e.printStackTrace();
        }
    }

    public static void replaceInPattern(String patternFilepath, Map<String, String> placeholdersValues, String resultFilepath) {
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
}

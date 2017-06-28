package com.chdtu.fitis.dipladd.grades;

import com.chdtu.fitis.dipladd.entity.Grade;
import com.chdtu.fitis.dipladd.entity.Student;

import java.util.HashMap;
import java.util.Map;

import static com.chdtu.fitis.dipladd.entity.KnowledgeControl.COURSEWORK;
import static com.chdtu.fitis.dipladd.entity.KnowledgeControl.COURSE_PROJECT;

public class CourseGrade {

    private String subjectName;
    private String studyingPeriod;
    private double creditsECTS;
    private int semester;
    private boolean multipleSemester;
    private Integer gradeScale;//Оцінка від 1 до 5
    private double points;//Оцінка від 0 до 100
    private String gradeNationalScale;//Оцінка від незадовільно до відмінно
    private String gradeECTS;//Оцінка від F до А
    private boolean markType;
    private int hours;
    private String knowledgeControlName;
    private Grade grade;

    CourseGrade(Grade grade, Student student) {
        subjectName = grade.getSubject().getName();
        int creationYear = student.getGroup().getCreationYear();
        int studyStartYear = student.getGroup().getStudyStartYear();
        semester = grade.getSubject().getSemester();
        studyingPeriod = getPeriod(creationYear, studyStartYear, semester);
        if (grade.getSubject().getKnowledgeControl().getId() == COURSEWORK
                || grade.getSubject().getKnowledgeControl().getId() == COURSE_PROJECT) {
            hours = 0;
        } else {
            hours = grade.getSubject().getHours();
        }
        creditsECTS = getCreditsFromHours(hours);
        if (grade.getPoints() != null) points = grade.getPoints();
        if (grade.getGrade() != null) this.gradeScale = GradeSummary.getGradeScaleFromPoints(points);
        if (grade.getGradeECTS() != null) gradeECTS = grade.getGradeECTS();
        else gradeECTS = "-";
        markType = grade.getSubject().getKnowledgeControl().getGrade();
        gradeNationalScale = getNationalGradeScale();
        knowledgeControlName = grade.getSubject().getKnowledgeControl().getName();
        multipleSemester = false;
        this.grade = grade;
    }

    CourseGrade(Student student) {
        subjectName = "";
        int creationYear = 0;
        int studyStartYear = 0;
        semester = 0;
        studyingPeriod = "";
        hours = 0;
        creditsECTS = 0;
        points = 0;
        this.gradeScale = 0;
        gradeECTS = "";
        markType = true;
        gradeNationalScale = "";
        knowledgeControlName = "";
        multipleSemester = false;
    }


    private static String getPeriod(int creationYear, int studyStartYear, int semester) {
        int yearFinish;
        int yearStart = creationYear - studyStartYear + 1;
        if (semester % 2 == 0) {
            yearStart += (semester) / 2;
            yearFinish = yearStart;
        } else {
            yearStart += (semester) / 2;
            yearFinish = yearStart + 1;
        }
        return yearStart + "-" + yearFinish;
    }

    public double getCreditsFromHours(int hours) {
        return hours / 30.0;
    }

    public String getNationalGradeScale() {
        if (markType) {
            switch (gradeECTS) {
                case "A":
                    return "Відмінно";
                case "B":
                    return "Добре";
                case "C":
                    return "Добре";
                case "D":
                    return "Задовільно";
                case "E":
                    return "Задовільно";
                case "F":
                    return "Незадовільно";
                default:
                    return "-";
            }
        } else return "Зараховано";
    }

    public String getNationalGradeScaleEnglish() {
        if (markType) {
            switch (gradeECTS) {
                case "A":
                    return "Excellent";
                case "B":
                    return "Good";
                case "C":
                    return "Good";
                case "D":
                    return "Satisfactory";
                case "E":
                    return "Satisfactory";
                case "F":
                    return "Failed";
                default:
                    return "-";
            }
        } else return "Passed";
    }

    private String getECTSGrade() {
        if (points >= 90 && points <= 100) return "A";
        if (points >= 82 && points <= 89) return "B";
        if (points >= 74 && points <= 81) return "C";
        if (points >= 64 && points <= 73) return "D";
        if (points >= 60 && points <= 63) return "E";
        return "F";
    }

    public Map<String, String> getPlaceholderDictionary() {
        Map<String, String> result = new HashMap<>();
        try {
            result.put("#UkrSubjName", grade.getSubject().getName());
            result.put("#EngSubjName", grade.getSubject().getNameEnglish());
            result.put("#Credits", String.format("%.1f", getCreditsECTS()));
            result.put("#Hours", String.format("%d", getHours()));
            result.put("#Points", String.format("%.0f", getPoints()));
            result.put("#UkrNatGrade", getNationalGradeScale());
            result.put("#EngNatGrade", getNationalGradeScaleEnglish());
            result.put("#ECTS", getGradeECTS());
        } catch (NullPointerException e) {
            System.out.println("Some of the grade's properties are null! " + this.getSubjectName());
        }
        return result;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getStudyingPeriod() {
        return studyingPeriod;
    }

    public void setStudyingPeriod(String studyingPeriod) {
        this.studyingPeriod = studyingPeriod;
    }

    public static String getValueInString(Double value) {
        String s = String.valueOf(value);
        String result = s.substring(0, s.indexOf(".") + 2);
        return result;
    }

    public String getMarkTypeInUkrainian() {
        if (markType) {
            return "";
        } else return "(Зал.)";

    }

    public String getMultipleSemesterInUkrainian() {
        if (multipleSemester) {
            return "(БС)";
        } else return "";
    }

    public double getCreditsECTS() {
        return creditsECTS;
    }

    public void setCreditsECTS(double creditsECTS) {
        this.creditsECTS = creditsECTS;
    }

    public Integer getGradeScale() {
        return gradeScale;
    }

    public void setGradeScale(Integer gradeScale) {
        this.gradeScale = gradeScale;
    }

    public String getGradeNationalScale() {
        return gradeNationalScale;
    }

    public void setGradeNationalScale(String gradeNationalScale) {
        this.gradeNationalScale = gradeNationalScale;
    }

    public String getGradeECTS() {
        return gradeECTS;
    }

    public void setGradeECTS(String gradeECTS) {
        this.gradeECTS = gradeECTS;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getKnowledgeControlName() {
        return knowledgeControlName;
    }

    public void setKnowledgeControlName(String knowledgeControlName) {
        this.knowledgeControlName = knowledgeControlName;
    }

    public boolean isMarkType() {
        return markType;
    }

    public void setMarkType(boolean markType) {
        this.markType = markType;
    }

    public boolean isMultipleSemester() {
        return multipleSemester;
    }

    public void setMultipleSemester(boolean multipleSemester) {
        this.multipleSemester = multipleSemester;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return subjectName + " " + points;
    }
}

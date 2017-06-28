package com.chdtu.fitis.dipladd.grades;

import com.chdtu.fitis.dipladd.entity.Grade;
import com.chdtu.fitis.dipladd.entity.Student;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentSummary {

    private DateFormat dateOfBirthFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static final String[] TITLES = {"", "Курсові роботи (проекти)", "Практики", "Атестація"};
    private List<GradeSummary> gradeSummaries;
    private String studentName;
    private String groupName;
    private double averagePoint;
    private double averageScale;
    private int totalHours;
    private String totalCreditsECTS;
    private Student student;

    public StudentSummary(Student student, List<List<Grade>> listsOfGrades) {
        gradeSummaries = new ArrayList<>();
        for (int i = 0; i < listsOfGrades.size(); i++) {
            gradeSummaries.add(new GradeSummary(TITLES[i], student, listsOfGrades.get(i)));
        }
        studentName = student.getStudentFullName();
        groupName = student.getGroup().getName();
        averagePoint = calculateAveragePoint();
        averageScale = calculateAverageScale();
        totalHours = calculateTotalHours();
        totalCreditsECTS = calculateTotalCredits();
        this.student = student;
    }

    public double calculateAveragePoint() {
        double result;
        int amount = 0;
        int sum = 0;
        for (GradeSummary gradeSummary : gradeSummaries) {
            for (CourseGrade courseGrade : gradeSummary.getCourseGrades()) {
                if (courseGrade.isMarkType() && courseGrade.getPoints() != 0) {
                    sum += courseGrade.getPoints();
                    amount++;
                }
            }
        }
        result = sum / (double) amount;
        return result;
    }

    public int calculateTotalHours() {
        int result = 0;
        for (GradeSummary gradeSummary : gradeSummaries) {
            for (CourseGrade courseGrade : gradeSummary.getCourseGrades()) {
                result += courseGrade.getHours();
            }
        }
        return result;
    }

    public String calculateTotalCredits() {
        double result = totalHours / 30.0;
        return CourseGrade.getValueInString(result);
    }

    public double calculateAverageScale() {
        double result = 0;
        int amount = 0;
        int sum = 0;
        for (GradeSummary gradeSummary : gradeSummaries) {
            for (CourseGrade courseGrade : gradeSummary.getCourseGrades()) {
                if (courseGrade.isMarkType()
                        && courseGrade.getGradeScale() != null
                        && courseGrade.getGradeScale() != 0
                        && courseGrade.getGradeScale() != 1
                        && courseGrade.getGradeScale() != 2
                        ) {
                    sum += courseGrade.getGradeScale();
                    amount++;
                }
            }
        }
        result = sum / (double) amount;
        return result;
    }

    public Map<String, Object> getPlaceholderDictionary() {
        Map<String, Object> result = new HashMap<>();

        result.put("#UkrSurname", student.getSurname());
        result.put("#EngSurname", student.getSurnameEnglish());
        result.put("#UkrName", student.getName());
        result.put("#EngName", student.getNameEnglish());
        result.put("#UkrPatronimic", student.getPatronimic());
        result.put("#EngPatronimic", student.getPatronimicEnglish());
        result.put("#BirthDate",
                student.getBirthDate() != null
                        ? dateOfBirthFormat.format(student.getBirthDate())
                        : "#BirthDate");

        String modeOfStudyUkr = "";
        String modeOfStudyEng = "";
        char modeOfStudy = student.getGroup().getModeOfStudy();
        if (modeOfStudy == 1076) {
            modeOfStudyUkr = "Денна";
            modeOfStudyEng = "Full-time";
        } else if (modeOfStudy == 1079) {
            modeOfStudyUkr = "Заочна";
            modeOfStudyEng = "Extramural";
        }
        result.put("#ModeOfStudyUkr", modeOfStudyUkr);
        result.put("#ModeOfStudyEng", modeOfStudyEng);

        result.put("#TotalCredits", totalCreditsECTS);
        result.put("#TotalHours", totalHours);
        result.put("#TotalMark", String.format("%.0f", round(averagePoint, 0)));

        result.put("#UkrBachelorDiplomaTheme", student.getBachelorWorkTheme());
        result.put("#StudyingPeriod", student.getGroup().getStudyingPeriod());
        return result;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String[] getTITLES() {
        return TITLES;
    }

    public List<GradeSummary> getGradeSummaries() {
        return gradeSummaries;
    }

    public void setGradeSummaries(List<GradeSummary> gradeSummaries) {
        this.gradeSummaries = gradeSummaries;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public double getAveragePoint() {
        return averagePoint;
    }

    public void setAveragePoint(double averagePoint) {
        this.averagePoint = averagePoint;
    }

    public double getAverageScale() {
        return averageScale;
    }

    public void setAverageScale(double averageScale) {
        this.averageScale = averageScale;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public String getTotalCreditsECTS() {
        return totalCreditsECTS;
    }

    public void setTotalCreditsECTS(String totalCreditsECTS) {
        this.totalCreditsECTS = totalCreditsECTS;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}

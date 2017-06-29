package com.chdtu.fitis.dipladd.grades;

import com.chdtu.fitis.dipladd.entity.Grade;
import com.chdtu.fitis.dipladd.entity.Student;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class GradeSummary {

    private String title;

    private Student student;

    private ArrayList<CourseGrade> courseGrades;

    public GradeSummary(String title, Student student, List<Grade> grades) {
        this.student = student;
        if (grades.size() == 0) {
            this.title = title;
            courseGrades = new ArrayList<>();
            courseGrades.add(new CourseGrade(student));
        } else {
            this.title = title;
            courseGrades = new ArrayList<>();

            for (Grade grade : grades) {
                courseGrades.add(new CourseGrade(grade, student));
            }

            List<CourseGrade> subjectsWithSameName;
            for (int i = 0; i < courseGrades.size(); i++) {
                subjectsWithSameName = new ArrayList<>();
                subjectsWithSameName.add(courseGrades.get(i));
                for (int j = i + 1; j < courseGrades.size(); j++) {
                    if (courseGrades.get(i).getSubjectName().trim().equals(courseGrades.get(j).getSubjectName().trim())) {
                        subjectsWithSameName.add(courseGrades.get(j));
                        courseGrades.remove(j);
                        j--;
                    }
                }
                courseGrades.set(i, combineCourseGrades(subjectsWithSameName));
            }
            setPeriodsToNormalView();

        }
        courseGrades.sort((o1, o2) -> {
            Collator ukrainianCollator = Collator.getInstance(new Locale("uk", "UA"));
            return ukrainianCollator.compare(o1.getSubjectName(), o2.getSubjectName());
        });
    }

    private CourseGrade combineCourseGrades(List<CourseGrade> courseGrades) {
        int[] years = new int[courseGrades.size() * 2];
        int i = 0;
        for (CourseGrade courseGrade : courseGrades) {
            years[i] = Integer.parseInt(courseGrade.getStudyingPeriod().substring(0, 4));
            years[i + 1] = Integer.parseInt(courseGrade.getStudyingPeriod().substring(5));
            i += 2;
        }
        int min = years[0];
        int max = years[0];
        for (i = 0; i < courseGrades.size() * 2; i++) {
            if (years[i] > max) max = years[i];
            if (years[i] < min) min = years[i];
        }
        String period = min + "-" + max;
        CourseGrade resultGrade = courseGrades.get(0);
        int hours = 0;
        int creditsECTS = 0;
        double averagePoints;
        int sumPoints = 0;
        int sumCountedPoints = 0;
        double averageGradeScale;
        int sumGradeScale = 0;
        int sumCountedGradeScale = 0;
        int numberOfExams = 0;
        int numberOfCounted = 0;
        for (CourseGrade courseGrade : courseGrades) {
            hours += courseGrade.getHours();
            if (courseGrade.isMarkType()) {
                sumPoints += courseGrade.getPoints();
                if (courseGrade.getGradeScale() != null) sumGradeScale += courseGrade.getGradeScale();
                numberOfExams++;
            } else {
                sumCountedPoints += courseGrade.getPoints();
                if (courseGrade.getGradeScale() != null) sumCountedGradeScale += courseGrade.getGradeScale();
                numberOfCounted++;
            }
        }
        if (numberOfExams != 0) {
            averagePoints = sumPoints / (double) numberOfExams;
            averageGradeScale = sumGradeScale / (double) numberOfExams;
            resultGrade.setMarkType(true);
            double rightPoints[] = calculateRightScaleAndPoints(averageGradeScale, averagePoints);
            resultGrade.setGradeScale((int) rightPoints[0]);
            resultGrade.setPoints(rightPoints[1]);
        } else {
            averagePoints = sumCountedPoints / (double) numberOfCounted;
            averageGradeScale = sumCountedGradeScale / (double) numberOfCounted;
            resultGrade.setGradeScale((int) Math.round(averageGradeScale));
            resultGrade.setPoints((int) Math.round(averagePoints));
            resultGrade.setMarkType(false);
        }
        resultGrade.setHours(hours);
        resultGrade.setCreditsECTS(resultGrade.getCreditsFromHours(hours));
        resultGrade.setGradeECTS(resultGrade.getGradeECTS());
        resultGrade.setGradeNationalScale(resultGrade.getNationalGradeScale());
        resultGrade.setStudyingPeriod(period);
        if (numberOfExams > 1) resultGrade.setMultipleSemester(true);
        if (numberOfExams == 0 && numberOfCounted > 1) resultGrade.setMultipleSemester(true);
        return resultGrade;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ArrayList<CourseGrade> getCourseGrades() {
        return courseGrades;
    }

    public void setCourseGrades(ArrayList<CourseGrade> courseGrades) {
        this.courseGrades = courseGrades;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private void setPeriodsToNormalView() {
        for (CourseGrade grade : courseGrades) {
            if (grade.getStudyingPeriod() != null)
                if (grade.getStudyingPeriod().substring(0, 4).equals(grade.getStudyingPeriod().substring(5))) {
                    int year = Integer.parseInt(grade.getStudyingPeriod().substring(0, 4)) - 1;
                    grade.setStudyingPeriod(year + "-" + grade.getStudyingPeriod().substring(5));
                }
        }

    }

    private double[] calculateRightScaleAndPoints(double averageGradeScale, double averagePoints) {
        double[] result = new double[2];
        if (Math.abs(averageGradeScale - 3.5) < 0.001 || Math.abs(averageGradeScale - 4.5) < 0.001) {
//            result[0] = getGradeScaleWhenHalf(averagePoints);
//            if (getGradeScaleFromPoints(averagePoints) > result[0])
//                result[1] = getMaxPointsFromGradeScale(result[0]);
//            else result[1]=getMinPointsFromGradeScale(result[0]);
            result[1] = (int) Math.round(averagePoints);
            result[0] = getGradeScaleFromPoints(result[1]);
        } else {
            if (getGradeScaleFromPoints(averagePoints) == Math.round(averageGradeScale)) {
                result[0] = (int) Math.round(averageGradeScale);
                result[1] = (int) Math.round(averagePoints);
            }
            if (getGradeScaleFromPoints(averagePoints) > Math.round(averageGradeScale)) {
                result[0] = (int) Math.round(averageGradeScale);
                result[1] = getMaxPointsFromGradeScale(averageGradeScale);
            }
            if (getGradeScaleFromPoints(averagePoints) < Math.round(averageGradeScale)) {
                result[0] = (int) Math.round(averageGradeScale);
                result[1] = getMinPointsFromGradeScale(averageGradeScale);
            }
        }

        return result;
    }

    public static int getGradeScaleFromPoints(double points) {
        if (points >= 90 && points <= 100) return 5;
        if (points >= 74 && points <= 89) return 4;
        if (points >= 60 && points <= 73) return 3;
        return 0;
    }

//    public static int getGradeScaleWhenHalf(double points) {
//        if (points >= 86 && points <= 100) return 5;
//        if (points >= 69 && points <= 85) return 4;
//        if (points >= 60 && points <= 68) return 3;
//        return 0;
//    }

    private int getMaxPointsFromGradeScale(double gradeScale) {
        if (Math.round(gradeScale) == 5) return 100;//impossible situation;
        if (Math.round(gradeScale) == 4) return 89;
        if (Math.round(gradeScale) == 3) return 73;
        return 0;
    }

    private int getMinPointsFromGradeScale(double gradeScale) {
        if (Math.round(gradeScale) == 5) return 90;
        if (Math.round(gradeScale) == 4) return 74;
        if (Math.round(gradeScale) == 3) return 60;//impossible situation;
        return 0;
    }

    @Override
    public String toString() {
        return title + " " + courseGrades.size() + " grade(s)";
    }
}

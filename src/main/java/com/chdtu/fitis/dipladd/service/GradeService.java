package com.chdtu.fitis.dipladd.service;

import com.chdtu.fitis.dipladd.dao.GradeDao;
import com.chdtu.fitis.dipladd.entity.Grade;
import com.chdtu.fitis.dipladd.entity.KnowledgeControl;
import com.chdtu.fitis.dipladd.entity.Student;
import com.chdtu.fitis.dipladd.entity.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Metr_yumora on 14.06.2017.
 */

@Service
@Repository
public class GradeService {

    public static final int FIRST_SEMESTER = 1;

    private GradeDao gradeDao;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public void setGradeDao(GradeDao gradeDao) {
        this.gradeDao = gradeDao;
    }

    public List<Grade> findAll() {
        return gradeDao.findAll();
    }

    // TODO: 24.06.2017 Check result
    public List<Grade> getGrades(
            Student student,
            int lastSemester,
            List<String> subjectsNames,
            List<Integer> knowledgeControlsTypes) {
        return entityManager.createQuery(
                "Select g From Grade g" +
                        " Join g.subject subj" +
                        " Where g.studentId=" + student.getId() +
                        " and subj.knowledgeControlID in (:kindsOfKnowledgeControl)" +
                        " and g.subject.name in (:subjects)" +
                        " and subj.semester>=" + FIRST_SEMESTER +
                        " and subj.semester<=" + lastSemester +
                        " Order by subj.name",
                Grade.class)
                .setParameter("subjects", subjectsNames)
                .setParameter("kindsOfKnowledgeControl", knowledgeControlsTypes)
                .getResultList();
    }

    public List<List<Grade>> getGradesByStudent(Student student, List<Subject> subjects) {
        int lastSemester = student.getGroup().calculateLastSemester();
        List<String> subjectsNames = new ArrayList<>();
        subjects.forEach((Subject s) -> subjectsNames.add(s.getName()));

        List<Integer> knowledgeControlsTypes = new ArrayList<>();
        List<List<Grade>> grades = new ArrayList<>();

        knowledgeControlsTypes.add(KnowledgeControl.EXAM);
        knowledgeControlsTypes.add(KnowledgeControl.TEST);
        knowledgeControlsTypes.add(KnowledgeControl.DIFFERENTIATED_TEST);
        grades.add(getGrades(student, lastSemester, subjectsNames, knowledgeControlsTypes));
        knowledgeControlsTypes.clear();

        knowledgeControlsTypes.add(KnowledgeControl.COURSEWORK);
        knowledgeControlsTypes.add(KnowledgeControl.COURSE_PROJECT);
        grades.add(getGrades(student, lastSemester, subjectsNames, knowledgeControlsTypes));
        knowledgeControlsTypes.clear();

        knowledgeControlsTypes.add(KnowledgeControl.PRACTICE);
        grades.add(getGrades(student, lastSemester, subjectsNames, knowledgeControlsTypes));
        knowledgeControlsTypes.clear();

        knowledgeControlsTypes.add(KnowledgeControl.FINAL_EXAMINATION);
        grades.add(getGrades(student, lastSemester, subjectsNames, knowledgeControlsTypes));
        knowledgeControlsTypes.clear();

        return grades;
    }
}

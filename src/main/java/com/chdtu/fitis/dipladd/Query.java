package com.chdtu.fitis.dipladd;

import com.chdtu.fitis.dipladd.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Supreme on 13.06.2016.
 */

public class Query {

    @PersistenceContext
    private EntityManager entityManager;

    public final int FIRST_SEMESTER = 1;

    public String buildGetGradesQuery(int studentId, int lastSemester, List<Object> knowledgeControl) {

        String setOfKnowledgeControlKinds = listToStringIndex(knowledgeControl);
        String resultQuery =
                "Select g From Grade g " +
                        "Join Subject subj On g.subjectId=subj.id " +
                        "Join Student st On g.studentId=st.id " +
                        "Join GroupSubject gs On g.subjectId=gs.subjectId and (gs.group=st.group) " +
                        "Where g.studentId=" + studentId + " " +
                        "and subj.knowledgeControlID in " + setOfKnowledgeControlKinds +
                        "and subj.name in (" +
                        "Select E.name FROM Subject E " +
                        "Join GroupSubject F  on F.subjectId=E.id " +
                        "Where  F.group=st.group and F.inDiplomaAddition=true" +
                        ") " +
                        " and subj.semester>=" + FIRST_SEMESTER +
                        " and subj.semester<=" + lastSemester +
                        " Order by subj.name";
        return resultQuery;
    }

    public static String listToStringIndex(List<Object> list) {
        String result = "(";
        for (Object item : list) {
            result += item.toString() + ",";
        }
        if (list.size() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        result += ")";
        return result;
    }

    public List<List<Grade>> getGrades(Student student) {
        int lastSemester = student.getGroup().calculateLastSemester();

        List<Object> knowledgeControlsTypes = new ArrayList<>();
        List<List<Grade>> grades = new ArrayList<>();
        knowledgeControlsTypes.add(KnowledgeControl.EXAM);
        knowledgeControlsTypes.add(KnowledgeControl.TEST);
        knowledgeControlsTypes.add(KnowledgeControl.DIFFERENTIATED_TEST);
        grades.add(entityManager.createQuery(buildGetGradesQuery(student.getId(), lastSemester, knowledgeControlsTypes)).getResultList());
        knowledgeControlsTypes.clear();

        knowledgeControlsTypes.add(KnowledgeControl.COURSEWORK);
        knowledgeControlsTypes.add(KnowledgeControl.COURSE_PROJECT);
        grades.add(entityManager.createQuery(buildGetGradesQuery(student.getId(), lastSemester, knowledgeControlsTypes)).getResultList());
        knowledgeControlsTypes.clear();

        knowledgeControlsTypes.add(KnowledgeControl.PRACTICE);
        grades.add(entityManager.createQuery(buildGetGradesQuery(student.getId(), lastSemester, knowledgeControlsTypes)).getResultList());
        knowledgeControlsTypes.clear();

        knowledgeControlsTypes.add(KnowledgeControl.FINAL_EXAMINATION);
        grades.add(entityManager.createQuery(buildGetGradesQuery(student.getId(), lastSemester, knowledgeControlsTypes)).getResultList());
        knowledgeControlsTypes.clear();
        return grades;
    }

    public List<Department> getActiveDepartments() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Department> query = cb.createQuery(Department.class);
        Root<Department> department = query.from(Department.class);
        query.select(department)
                .where(cb.equal(department.get("accountable"), true));
        TypedQuery<Department> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    public List<Group> getSelectedGroups(int selectedDepartmentId) {
        return entityManager.createQuery(
                "Select gr FROM Group gr " +
                        "Join Speciality sp on sp.id=gr.specialityId " +
                        "Join Department dep on dep.id=sp.departmentId " +
                        "Join CurrentYear cr on cr.currentYear>0 " +
                        "Where gr.active=true " +
                        "and not gr.name='??-122' " +
                        "and gr.creationYear=cr.currentYear-3 " +
                        "and dep.id =" + selectedDepartmentId
        ).getResultList();
    }
}

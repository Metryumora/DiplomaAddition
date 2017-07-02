package com.chdtu.fitis.dipladd.service;

import com.chdtu.fitis.dipladd.dao.SubjectDao;
import com.chdtu.fitis.dipladd.entity.Student;
import com.chdtu.fitis.dipladd.entity.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Metr_yumora on 24.06.2017.
 */

@Service
@Repository
public class SubjectService {

    @PersistenceContext
    EntityManager entityManager;

    private SubjectDao subjectDao;

    @Autowired
    public void setSubjectDao(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    // TODO: 24.06.2017 Check result
    public List<Subject> getByStudent(Student student) {
        return entityManager.createQuery(
                "Select E FROM Subject E " +
                        "Join E.groupSubjects F   " +
                        "Where  F.group.id=" + student.getGroup().getId() + " and F.inDiplomaAddition=true",
                Subject.class
        ).getResultList();
    }

}

package com.chdtu.fitis.dipladd.dao;

import com.chdtu.fitis.dipladd.entity.Grade;
import com.chdtu.fitis.dipladd.entity.GradePrimaryKey;
import com.chdtu.fitis.dipladd.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Metr_yumora on 12.06.2017.
 */
@Transactional
public interface GradeDao extends JpaRepository<Grade, GradePrimaryKey> {

    List<List<Grade>> getGradesByStudent(Student student);

}

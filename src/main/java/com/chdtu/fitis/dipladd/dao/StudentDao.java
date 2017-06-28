package com.chdtu.fitis.dipladd.dao;

import com.chdtu.fitis.dipladd.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Metr_yumora on 12.06.2017.
 */
@Transactional
public interface StudentDao extends JpaRepository<Student, Integer> {

    public List<Student> getByGroupAndInActiveOrderBySurnameAsc(Integer groupId, Boolean isActive);

    public Student getBySurnameAndNameAndPatronimic(String surname, String name, String patronimic);

}

package com.chdtu.fitis.dipladd.dao;

import com.chdtu.fitis.dipladd.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Metr_yumora on 12.06.2017.
 */
@Transactional
public interface SubjectDao extends JpaRepository<Subject, Integer> {
}

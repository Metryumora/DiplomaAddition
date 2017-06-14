package com.chdtu.fitis.dipladd.service;

import com.chdtu.fitis.dipladd.dao.StudentDao;
import com.chdtu.fitis.dipladd.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Metr_yumora on 12.06.2017.
 */

@Service
@Repository
public class StudentService {

    @Autowired
    private StudentDao studentDao;

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentDao.findAll();
    }

    @Transactional(readOnly = true)
    public Student get(Integer id) {
        return studentDao.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<Student> getByGroupAndInActiveOrderBySurnameAsc(Integer groupId, Boolean isActive) {
        return studentDao.getByGroupAndInActiveOrderBySurnameAsc(groupId, isActive);
    }
}

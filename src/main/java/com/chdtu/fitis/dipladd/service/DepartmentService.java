package com.chdtu.fitis.dipladd.service;

import com.chdtu.fitis.dipladd.dao.DepartmentDao;
import com.chdtu.fitis.dipladd.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Metr_yumora on 14.06.2017.
 */
@Service
@Repository
public class DepartmentService {

    private DepartmentDao departmentDao;

    @Autowired
    public void setDepartmentDao(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Transactional(readOnly = true)
    public List<Department> findByAccountable(Boolean accountable) {
        return departmentDao.findByAccountable(accountable);
    }
}

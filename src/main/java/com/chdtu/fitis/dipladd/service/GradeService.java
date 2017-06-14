package com.chdtu.fitis.dipladd.service;

import com.chdtu.fitis.dipladd.dao.GradeDao;
import com.chdtu.fitis.dipladd.entity.Grade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Metr_yumora on 14.06.2017.
 */
@Service
@Repository
public class GradeService {

    @Autowired
    private GradeDao gradeDao;

    public List<Grade> findAll(){
        return gradeDao.findAll();
    }
}

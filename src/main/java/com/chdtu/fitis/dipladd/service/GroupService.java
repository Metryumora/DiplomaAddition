package com.chdtu.fitis.dipladd.service;

import com.chdtu.fitis.dipladd.dao.GroupDao;
import com.chdtu.fitis.dipladd.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Metr_yumora on 14.06.2017.
 */
@Service
@Repository
public class GroupService {

    @Autowired
    private GroupDao groupDao;

    @Transactional(readOnly = true)
    public Group get(Integer id){
        return groupDao.findOne(id);
    }

}

package com.chdtu.fitis.dipladd.service;

import com.chdtu.fitis.dipladd.dao.GroupDao;
import com.chdtu.fitis.dipladd.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Metr_yumora on 14.06.2017.
 */
@Service
@Repository
public class GroupService {

    private GroupDao groupDao;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Transactional(readOnly = true)
    public Group get(Integer id) {
        return groupDao.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<Group> getBySpecialityDepartmentId(Integer id) {
        return groupDao.getBySpecialityDepartmentId(id);
    }

    @Transactional(readOnly = true)
    public List<Group> getGraduatingThisYearByDepartmentId(Integer id) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return entityManager.createQuery(
                "select gr from Group gr " +
                        "join gr.speciality sp " +
                        "join sp.department dep " +
                        "where gr.active = true " +
                        "and dep.id= " + id + " " +
                        "and gr.creationYear = " + (currentYear - 4),
                Group.class).getResultList();
    }

}

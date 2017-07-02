package com.chdtu.fitis.dipladd.dao;

import com.chdtu.fitis.dipladd.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Metr_yumora on 12.06.2017.
 */
@Transactional
public interface GroupDao extends JpaRepository<Group, Integer> {

    List<Group> getBySpecialityDepartmentId(Integer id);

}

package com.chdtu.fitis.dipladd.сontroller;

import com.chdtu.fitis.dipladd.Query;
import com.chdtu.fitis.dipladd.entity.Department;
import com.chdtu.fitis.dipladd.service.DepartmentService;
import com.chdtu.fitis.dipladd.service.GradeService;
import com.chdtu.fitis.dipladd.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Metr_yumora on 12.06.2017.
 */

@Controller
public class MainController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public ModelAndView showMainPage(ModelMap modelMap) {
        //studentService.findAll().forEach((Student s)->System.out.println(s.getName()));
        //departmentService.findByAccountable(true).forEach((Department d)->d.getName());
        //departmentService.findByAccountable(true).forEach((Grade g)->g.getGradeECTS());
        //System.out.println(studentService.get(42));

        Query query = new Query();
        query.getActiveDepartments().forEach((Department d)-> System.out.println(d.getName()));

        return new ModelAndView("index", modelMap);
    }

}

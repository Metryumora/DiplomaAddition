package com.chdtu.fitis.dipladd.сontroller;

import com.chdtu.fitis.dipladd.Query;
import com.chdtu.fitis.dipladd.docmanager.Util;
import com.chdtu.fitis.dipladd.entity.Department;
import com.chdtu.fitis.dipladd.entity.Grade;
import com.chdtu.fitis.dipladd.entity.Student;
import com.chdtu.fitis.dipladd.entity.Subject;
import com.chdtu.fitis.dipladd.service.DepartmentService;
import com.chdtu.fitis.dipladd.service.GradeService;
import com.chdtu.fitis.dipladd.service.StudentService;
import com.chdtu.fitis.dipladd.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Metr_yumora on 12.06.2017.
 */

@Controller
@EnableAutoConfiguration
public class MainController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SubjectService subjectService;

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public ModelAndView showMainPage(ModelMap modelMap) {
        //departmentService.findByAccountable(true).forEach((Department d) -> System.out.println(d.getName()));
        //studentService.findAll().forEach((Student s)->System.out.println(s.getId()));
        //departmentService.findByAccountable(true).forEach((Department d)->d.getName());
        //departmentService.findByAccountable(true).forEach((Grade g)->g.getGradeECTS());
        //System.out.println(studentService.get(42));

        Student student = (studentService.get(4117));
        List<Subject> subjects = subjectService.getByStudent(student);
        List<List<Grade>> grades = gradeService.getGradesByStudent(student, subjects);


//        Query query = new Query();
//        query.getActiveDepartments().forEach((Department d)-> System.out.println(d.getName()));

        return new ModelAndView("index", modelMap);
    }

    @RequestMapping(value = "/doc", method = {RequestMethod.GET})
    public ModelAndView testDoc(ModelMap modelMap) {

        Map<String,String> placeholdersValues = new HashMap<>();
        placeholdersValues.put("PHGroup","ПЗ-144");
        placeholdersValues.put("PHName","Тулуб В.О.");
        placeholdersValues.put("PHWorkNumber","1");
        placeholdersValues.put("PHYear","2017");
        placeholdersValues.put("PHSubject","Програмування обчислень");

        Util.replaceInPattern("Pattern.docx", placeholdersValues);

        return new ModelAndView("index", modelMap);
    }

}

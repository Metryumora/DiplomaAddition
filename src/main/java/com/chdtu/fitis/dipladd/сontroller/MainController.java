package com.chdtu.fitis.dipladd.сontroller;

import com.chdtu.fitis.dipladd.docmanager.Util;
import com.chdtu.fitis.dipladd.entity.Grade;
import com.chdtu.fitis.dipladd.entity.Group;
import com.chdtu.fitis.dipladd.entity.Student;
import com.chdtu.fitis.dipladd.entity.Subject;
import com.chdtu.fitis.dipladd.grades.StudentSummary;
import com.chdtu.fitis.dipladd.service.*;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

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
    @Autowired
    private GroupService groupService;

    private static final String ROOT_OUTPUT_DIRECTORY = "C:\\out";

    private static String filepathPrefix = "";

    private static final String templateFilepath = "DiplomaSupplementTemplate.docx";

    public MainController() throws FileNotFoundException, Docx4JException {
    }

    private void createRootOutputDirectory() {
        File directory = new File(ROOT_OUTPUT_DIRECTORY);
        if (directory.exists() || directory.mkdir()) {
            filepathPrefix = directory.getAbsolutePath() + "\\";
        }
    }

    private void createGroupDiplomaAdditionsDirectory(Student student) {
        File directory = new File(filepathPrefix + student.getGroup().getName());
        if (directory.exists() || directory.mkdir()) {
            filepathPrefix += student.getGroup().getName() + "\\";
        }
    }

    @RequestMapping(value = "/s", method = {RequestMethod.GET})
    public ModelAndView generateForStudent(ModelMap modelMap, @RequestParam("id") Integer id) {
        Student student = studentService.get(id);
        generateDiplomaAddition(student);
        return new ModelAndView("index", modelMap);
    }

    @RequestMapping(value = "/g", method = {RequestMethod.GET})
    public ModelAndView generateForGroup(ModelMap modelMap, @RequestParam("id") Integer id) {
        Group group = groupService.get(id);
        generateDiplomaAdditions(group);
        return new ModelAndView("index", modelMap);
    }

    private void generateDiplomaAdditions(Group group) {
        List<Student> students = studentService.getByGroupAndInActiveOrderBySurnameAsc(group, true);
        students.forEach(this::generateDiplomaAddition);
    }

    private void generateDiplomaAddition(Student student) {
        createRootOutputDirectory();
        createGroupDiplomaAdditionsDirectory(student);
        List<Subject> subjects = subjectService.getByStudent(student);
        List<List<Grade>> grades = gradeService.getGradesByStudent(student, subjects);
        StudentSummary studentSummary = new StudentSummary(student, grades);
        Util.fillDiplomaSupplementTemplate(templateFilepath, studentSummary,
                filepathPrefix + student.generateDocumentName());
    }
}

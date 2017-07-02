package com.chdtu.fitis.dipladd.сontroller;

import com.chdtu.fitis.dipladd.ajax.AjaxResponseBody;
import com.chdtu.fitis.dipladd.ajax.KeyItem;
import com.chdtu.fitis.dipladd.entity.Department;
import com.chdtu.fitis.dipladd.entity.Group;
import com.chdtu.fitis.dipladd.entity.Student;
import com.chdtu.fitis.dipladd.service.DepartmentService;
import com.chdtu.fitis.dipladd.service.GroupService;
import com.chdtu.fitis.dipladd.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Metr_yumora on 02.07.2017.
 */

@RestController
public class SelectorController {

    private DepartmentService departmentService;
    private GroupService groupService;
    private StudentService studentService;

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/get/departments")
    public ResponseEntity<?> getDepartments() {
        AjaxResponseBody result = new AjaxResponseBody();

        List<Department> departments = departmentService.findByAccountable(true);
        if (departments.isEmpty()) {
            result.setMessage("No departments found!");
        } else {
            result.setMessage("success");
        }
        departments.forEach(department -> result.getItems().add(new KeyItem(department.getId(), department.getAbbreviation())));

        return ResponseEntity.ok(result);

    }

    @PostMapping(value = "/get/groups")
    public ResponseEntity<?> getGroups(@RequestBody String id) {
        AjaxResponseBody result = new AjaxResponseBody();

        List<Group> groups = groupService.getGraduatingThisYearByDepartmentId(Integer.parseInt(id));
        if (groups.isEmpty()) {
            result.setMessage("No groups found!");
        } else {
            result.setMessage("Success");
        }
        groups.forEach(group -> result.getItems().add(new KeyItem(group.getId(), group.getName())));

        return ResponseEntity.ok(result);

    }

    @PostMapping("/get/students")
    public ResponseEntity<?> getStudents(@RequestBody String id) {
        AjaxResponseBody result = new AjaxResponseBody();

        List<Student> students = studentService.getByGroupIdAndInActiveOrderBySurnameAsc(Integer.parseInt(id), true);
        if (students.isEmpty()) {
            result.setMessage("No students found!");
        } else {
            result.setMessage("Success");
        }
        students.forEach(student -> result.getItems().add(new KeyItem(student.getId(), student.getInitials())));

        return ResponseEntity.ok(result);

    }

}

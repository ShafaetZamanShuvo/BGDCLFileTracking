package com.bgdcl.bgdcl_file_tracking.controller;

import com.bgdcl.bgdcl_file_tracking.model.Department;
import com.bgdcl.bgdcl_file_tracking.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/get-all-departments")
    public ResponseEntity<List<Department>> getAllDepartment() {
        List < Department > departments = departmentService.getAllDepartment();
        return ResponseEntity.ok().body(departments);
    }

}

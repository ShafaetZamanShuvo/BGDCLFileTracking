package com.bgdcl.bgdcl_file_tracking.controller;

import com.bgdcl.bgdcl_file_tracking.model.Department;
import com.bgdcl.bgdcl_file_tracking.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-department")
    public ResponseEntity<Department> addDepartment( @RequestBody Department department) {
        try {
            return ResponseEntity.ok().body(departmentService.addDepartment(department));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //edit department
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/edit-department")
    public ResponseEntity<Department> editDepartment(@RequestBody Department department) {
        try {
            return ResponseEntity.ok().body(departmentService.updateDepartment(department));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //delete department
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        try {
            departmentService.deleteDepartment(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting department: " + e.getMessage());
        }
    }

}

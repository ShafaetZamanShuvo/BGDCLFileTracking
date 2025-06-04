package com.bgdcl.bgdcl_file_tracking.service;

import com.bgdcl.bgdcl_file_tracking.model.Department;
import com.bgdcl.bgdcl_file_tracking.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartment() {
        return departmentRepository.findAll();
    }
}

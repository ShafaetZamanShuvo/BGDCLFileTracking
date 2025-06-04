package com.bgdcl.bgdcl_file_tracking.controller;

import com.bgdcl.bgdcl_file_tracking.model.Designation;
import com.bgdcl.bgdcl_file_tracking.service.DesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/designation")
public class DesignationController {

    @Autowired
    private DesignationService designationService;

    @GetMapping("/all")
    public ResponseEntity<List<Designation>> getAllDesignation() {
        List<Designation> designations = designationService.getAllDesignation();
        return ResponseEntity.ok().body(designations);
    }
}

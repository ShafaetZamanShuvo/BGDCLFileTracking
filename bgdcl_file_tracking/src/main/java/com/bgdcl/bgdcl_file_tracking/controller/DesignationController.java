package com.bgdcl.bgdcl_file_tracking.controller;

import com.bgdcl.bgdcl_file_tracking.model.Designation;
import com.bgdcl.bgdcl_file_tracking.service.DesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/designation")
public class DesignationController {

    @Autowired
    private DesignationService designationService;

    @GetMapping("/get-all-designations")
    public ResponseEntity<List<Designation>> getAllDesignation() {
        return ResponseEntity.ok().body(designationService.getAllDesignation());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-designation")
    public ResponseEntity<Designation> addDesignation(@RequestBody Designation designation) {
        try {
            return ResponseEntity.ok().body(designationService.addDesignation(designation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //edit designation
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/edit-designation")
    public ResponseEntity<Designation> editDesignation(@RequestBody Designation designation) {
        try {
            return ResponseEntity.ok().body(designationService.updateDesignation(designation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDesignation(@PathVariable Long id) {
        try {
            designationService.deleteDesignation(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting designation: " + e.getMessage());
        }
    }

}

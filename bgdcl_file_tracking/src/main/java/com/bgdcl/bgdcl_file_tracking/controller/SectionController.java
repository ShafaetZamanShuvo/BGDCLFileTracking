package com.bgdcl.bgdcl_file_tracking.controller;

import com.bgdcl.bgdcl_file_tracking.model.Department;
import com.bgdcl.bgdcl_file_tracking.model.Section;
import com.bgdcl.bgdcl_file_tracking.service.DepartmentService;
import com.bgdcl.bgdcl_file_tracking.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/section")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @GetMapping("/get-all-sections")
    public ResponseEntity<List<Section>> getAllSection() {
        List<Section> sections = sectionService.getAllSection();
        return ResponseEntity.ok().body(sections);
    }

    //get section by department id
    @GetMapping("/department/{id}")
    public ResponseEntity<List<Section>> getSectionByDepartmentId(@PathVariable Long id) {
        List<Section> sections = sectionService.getSectionByDepartmentId(id);
        return ResponseEntity.ok().body(sections);
    }

    //add section
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-section")
    public ResponseEntity<Section> addSection(@RequestBody Section section) {
        try {
            return ResponseEntity.ok().body(sectionService.addSection(section));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //edit section
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/edit-section")
    public ResponseEntity<Section> editSection(@RequestBody Section section) {
        try {
            return ResponseEntity.ok().body(sectionService.updateSection(section));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}

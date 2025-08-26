package com.bgdcl.bgdcl_file_tracking.service;

import com.bgdcl.bgdcl_file_tracking.model.Section;
import com.bgdcl.bgdcl_file_tracking.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {
    @Autowired
    private SectionRepository sectionRepository;

    public List<Section> getAllSection() {
        return sectionRepository.findAll();
    }

    public List<Section> getSectionByDepartmentId(Long id) {
        return sectionRepository.findByDepartmentId(id);
    }

    public Section addSection(Section section) {
        return sectionRepository.save(section);
    }

    public Section updateSection(Section section) {
        return sectionRepository.save(section);
    }
}

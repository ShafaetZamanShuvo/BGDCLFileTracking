package com.bgdcl.bgdcl_file_tracking.service;


import com.bgdcl.bgdcl_file_tracking.model.Designation;
import com.bgdcl.bgdcl_file_tracking.repository.DesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignationService {

    @Autowired
    private DesignationRepository designationRepository;

    public List<Designation> getAllDesignation() {
        return designationRepository.findAll();
    }

    public Designation addDesignation(Designation designation) {
        return designationRepository.save(designation);
    }

    public Designation updateDesignation(Designation designation) {
        // TODO: update designation
        //first find the designation by id
        Designation existingDesignation = designationRepository.findById(designation.getId()).orElse(null);
        if (existingDesignation != null) {
            existingDesignation.setName(designation.getName());
            existingDesignation.setStatus(designation.getStatus());
            return designationRepository.save(existingDesignation);
        } else {
            return null;
        }
    }
    
    public void deleteDesignation(Long id) {
        designationRepository.deleteById(id);
    }
}

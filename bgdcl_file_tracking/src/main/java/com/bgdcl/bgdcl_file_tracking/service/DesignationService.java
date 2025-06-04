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

}

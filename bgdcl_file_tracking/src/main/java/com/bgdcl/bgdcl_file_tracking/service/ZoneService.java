package com.bgdcl.bgdcl_file_tracking.service;

import com.bgdcl.bgdcl_file_tracking.model.Zone;
import com.bgdcl.bgdcl_file_tracking.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    public List<Zone> getAllZone() {return zoneRepository.findAll();}

    public Zone addZone(Zone zone) {
        return zoneRepository.save(zone);
    }
}

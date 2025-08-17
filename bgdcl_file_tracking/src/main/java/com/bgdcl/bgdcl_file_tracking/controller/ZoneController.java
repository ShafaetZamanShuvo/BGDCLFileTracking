package com.bgdcl.bgdcl_file_tracking.controller;

import com.bgdcl.bgdcl_file_tracking.model.Zone;
import com.bgdcl.bgdcl_file_tracking.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/zone")
public class ZoneController {

    @Autowired
    private ZoneService zoneService;

    @GetMapping("/get-all-zones")
    public ResponseEntity<List<Zone>> getAllZone() {
        List<Zone> zones = zoneService.getAllZone();
        return ResponseEntity.ok().body(zones);
    }

    //add zone
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add-zone")
    public ResponseEntity<Zone> addZone(@RequestBody Zone zone) {
        try {
            return ResponseEntity.ok().body(zoneService.addZone(zone));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

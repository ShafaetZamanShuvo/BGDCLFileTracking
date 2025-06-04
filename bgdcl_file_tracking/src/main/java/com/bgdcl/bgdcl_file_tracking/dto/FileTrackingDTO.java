package com.bgdcl.bgdcl_file_tracking.dto;

import com.bgdcl.bgdcl_file_tracking.model.CustomerFile;
import com.bgdcl.bgdcl_file_tracking.model.FileStatus;
import com.bgdcl.bgdcl_file_tracking.model.FileTracking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileTrackingDTO implements Serializable {
    private Long id;
    private String oldCode;
    private FileStatus currentStatus;
    private Integer currentDepartmentId;
    private boolean pendingAcceptance;
    private LocalDateTime lastUpdated;
    
    // Fields from CustomerFile
    private String customerCode;
    private String customerName;
    private String zone;
    
    // Constructor to map from entities
    public FileTrackingDTO(FileTracking fileTracking, CustomerFile customerFile) {
        this.id = fileTracking.getId();
        this.oldCode = fileTracking.getOldCode();
        this.currentStatus = fileTracking.getCurrentStatus();
        this.currentDepartmentId = fileTracking.getCurrentDepartmentId();
        this.pendingAcceptance = fileTracking.isPendingAcceptance();
        this.lastUpdated = fileTracking.getLastUpdated();
        
        if (customerFile != null) {
            this.customerCode = customerFile.getCustomerCode();
            this.customerName = customerFile.getCustomerName();
            this.zone = customerFile.getZone();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }

    public FileStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(FileStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Integer getCurrentDepartmentId() {
        return currentDepartmentId;
    }

    public void setCurrentDepartmentId(Integer currentDepartmentId) {
        this.currentDepartmentId = currentDepartmentId;
    }

    public boolean isPendingAcceptance() {
        return pendingAcceptance;
    }

    public void setPendingAcceptance(boolean pendingAcceptance) {
        this.pendingAcceptance = pendingAcceptance;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
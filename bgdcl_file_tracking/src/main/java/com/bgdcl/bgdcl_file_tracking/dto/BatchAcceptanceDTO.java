package com.bgdcl.bgdcl_file_tracking.dto;

import java.util.List;

public class BatchAcceptanceDTO {
    private List<String> fileCodes;
    private Long departmentId;
    private Long userId;
    private boolean accept;
    private String notes;
    
    // Getters and setters

    public List<String> getFileCodes() {
        return fileCodes;
    }

    public void setFileCodes(List<String> fileCodes) {
        this.fileCodes = fileCodes;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
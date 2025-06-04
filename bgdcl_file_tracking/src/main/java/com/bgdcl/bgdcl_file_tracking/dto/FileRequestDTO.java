package com.bgdcl.bgdcl_file_tracking.dto;

import java.time.LocalDateTime;

public class FileRequestDTO {
    private String fileCode;
    private Long requestingDepartmentId;
    private Long requestedFromDepartmentId;
    private Long requestingUserId;
    private String notes;
    private String requestingDepartmentName;
    private LocalDateTime requestTime;
    private String status;
    
    // Getters and setters

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public Long getRequestingDepartmentId() {
        return requestingDepartmentId;
    }

    public void setRequestingDepartmentId(Long requestingDepartmentId) {
        this.requestingDepartmentId = requestingDepartmentId;
    }

    public Long getRequestedFromDepartmentId() {
        return requestedFromDepartmentId;
    }

    public void setRequestedFromDepartmentId(Long requestedFromDepartmentId) {
        this.requestedFromDepartmentId = requestedFromDepartmentId;
    }

    public Long getRequestingUserId() {
        return requestingUserId;
    }

    public void setRequestingUserId(Long requestingUserId) {
        this.requestingUserId = requestingUserId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRequestingDepartmentName() {
        return requestingDepartmentName;
    }

    public void setRequestingDepartmentName(String requestingDepartmentName) {
        this.requestingDepartmentName = requestingDepartmentName;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
package com.bgdcl.bgdcl_file_tracking.dto;

public class FileAcceptanceDTO {
    private String fileCode;
    private Long departmentId;
    private Long userId;
    private boolean accept;
    private String notes;
    
    // Default constructor
    public FileAcceptanceDTO() {
    }
    
    // All-args constructor
    public FileAcceptanceDTO(String fileCode, Long departmentId, Long userId, boolean accept, String notes) {
        this.fileCode = fileCode;
        this.departmentId = departmentId;
        this.userId = userId;
        this.accept = accept;
        this.notes = notes;
    }
    
    // Getters and setters
    public String getFileCode() {
        return fileCode;
    }
    
    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
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
    
    @Override
    public String toString() {
        return "FileAcceptanceDTO{" +
                "fileCode='" + fileCode + '\'' +
                ", departmentId=" + departmentId +
                ", userId=" + userId +
                ", accept=" + accept +
                ", notes='" + notes + '\'' +
                '}';
    }
}
package com.bgdcl.bgdcl_file_tracking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_tracking",
        indexes = {
                @Index(name = "old_code_index", columnList = "old_code")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileTracking implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "old_code", nullable = false, unique = true)
    private String oldCode;


    @Enumerated(EnumType.STRING)
    @Column(name = "current_status")
    private FileStatus currentStatus = FileStatus.in;

    @Column(name = "current_department_id")
    private Integer currentDepartmentId;

    @Column(name = "pending_acceptance", columnDefinition = "boolean default false")
    private boolean pendingAcceptance = false;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

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
}

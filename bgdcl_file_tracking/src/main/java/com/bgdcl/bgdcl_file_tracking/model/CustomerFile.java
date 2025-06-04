package com.bgdcl.bgdcl_file_tracking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "customer_file",
        indexes = {
                @Index(name = "idx_old_code", columnList = "oldCode")
        })
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerFile implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String oldCode;

    @Column(nullable = false, unique = true)
    private String customerCode;

    @Column(nullable = true)
    private String customerName;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String fileExists;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String zone;

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

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getFileExists() {
        return fileExists;
    }

    public void setFileExists(String fileExists) {
        this.fileExists = fileExists;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}

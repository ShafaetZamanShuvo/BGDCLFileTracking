package com.bgdcl.bgdcl_file_tracking.repository;

import com.bgdcl.bgdcl_file_tracking.model.CustomerFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerFileRepository extends JpaRepository<CustomerFile, Long> {
    Optional<CustomerFile> findByOldCode(String oldCode);
}

package com.bgdcl.bgdcl_file_tracking.repository;

import com.bgdcl.bgdcl_file_tracking.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {
    Optional<Designation> findByName(String name);
}

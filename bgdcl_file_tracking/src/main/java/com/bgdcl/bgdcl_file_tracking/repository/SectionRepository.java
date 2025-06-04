package com.bgdcl.bgdcl_file_tracking.repository;

import com.bgdcl.bgdcl_file_tracking.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByDepartmentId(Long id);
}

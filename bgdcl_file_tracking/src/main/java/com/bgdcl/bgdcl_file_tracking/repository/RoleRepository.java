package com.bgdcl.bgdcl_file_tracking.repository;

import com.bgdcl.bgdcl_file_tracking.model.ERole;
import com.bgdcl.bgdcl_file_tracking.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}

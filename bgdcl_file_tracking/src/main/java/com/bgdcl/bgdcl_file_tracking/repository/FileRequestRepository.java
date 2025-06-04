package com.bgdcl.bgdcl_file_tracking.repository;

import com.bgdcl.bgdcl_file_tracking.model.FileRequest;
import com.bgdcl.bgdcl_file_tracking.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRequestRepository extends JpaRepository<FileRequest, Long> {
    List<FileRequest> findByRequestedFromDepartmentIdAndStatus(Long departmentId, RequestStatus requestStatus);

    List<FileRequest> findByFileCodeAndStatus(String oldCode, RequestStatus requestStatus);

    List<FileRequest> findByFileCodeAndStatusAndRequestingDepartmentId(String oldCode, RequestStatus requestStatus, Long toDeptId);
}

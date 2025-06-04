package com.bgdcl.bgdcl_file_tracking.repository;

import com.bgdcl.bgdcl_file_tracking.model.FileTransaction;
import com.bgdcl.bgdcl_file_tracking.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findByUserId(Long userId);
}

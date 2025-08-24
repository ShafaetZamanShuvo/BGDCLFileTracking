package com.bgdcl.bgdcl_file_tracking.controller;

import com.bgdcl.bgdcl_file_tracking.dto.UserInfoDTO;
import com.bgdcl.bgdcl_file_tracking.model.User;
import com.bgdcl.bgdcl_file_tracking.model.UserInfo;
import com.bgdcl.bgdcl_file_tracking.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserInfoController {

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/get-all-users")
    public List<UserInfoDTO> getAllUserInfo() {
        return userDetailsService.getAllUserInfo();
    }

    @GetMapping("/get-all-user-ids")
    public List<User> getAllUserIds() {
        return userDetailsService.getAllUsers();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add-user")
    public ResponseEntity <UserInfo> addUserInfo(@RequestBody UserInfo userInfo) {
        try {
            return ResponseEntity.ok().body(userDetailsService.addUserInfo(userInfo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}

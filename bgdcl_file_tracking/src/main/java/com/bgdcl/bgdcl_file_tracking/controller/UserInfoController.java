package com.bgdcl.bgdcl_file_tracking.controller;

import com.bgdcl.bgdcl_file_tracking.dto.UserInfoDTO;
import com.bgdcl.bgdcl_file_tracking.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

}

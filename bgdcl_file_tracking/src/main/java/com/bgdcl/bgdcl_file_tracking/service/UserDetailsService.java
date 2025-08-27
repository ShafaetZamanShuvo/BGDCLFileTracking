package com.bgdcl.bgdcl_file_tracking.service;

import com.bgdcl.bgdcl_file_tracking.dto.UserInfoDTO;
import com.bgdcl.bgdcl_file_tracking.model.User;
import com.bgdcl.bgdcl_file_tracking.model.UserInfo;
import com.bgdcl.bgdcl_file_tracking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private UserRepository userRepository;

    public UserInfoDTO getUserDetails(Long userId) {
        // Get user info in a single database call
        UserInfo userInfo = userInfoRepository.findByUserId(userId);

        if (userInfo == null) {
            return null; // Or throw an appropriate exception
        }

        // Create DTO
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserId(userId);
        userInfoDTO.setFullName(userInfo.getFullName());
        userInfoDTO.setDepartmentId(userInfo.getDepartmentId());
        userInfoDTO.setSectionId(userInfo.getSectionId());
        userInfoDTO.setZoneId(userInfo.getZone_id());
        userInfoDTO.setDesignationId(userInfo.getDesignationId());

//        get email and username
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        userInfoDTO.setEmail(user.getEmail());
        userInfoDTO.setUsername(user.getUsername());

        // Fetch related entities in parallel using Optional
        //set
        Optional.ofNullable(userInfo.getDesignationId())
                .flatMap(designationRepository::findById)
                .ifPresent(designation -> userInfoDTO.setDesignation(designation.getName()));

        Optional.ofNullable(userInfo.getDepartmentId())
                .flatMap(departmentRepository::findById)
                .ifPresent(department -> userInfoDTO.setDepartment(department.getName()));

        Optional.ofNullable(userInfo.getSectionId())
                .flatMap(sectionRepository::findById)
                .ifPresent(section -> userInfoDTO.setSection(section.getName()));

        Optional.ofNullable(userInfo.getZone_id())
                .flatMap(zoneRepository::findById)
                .ifPresent(zone -> userInfoDTO.setZone(zone.getName()));

        return userInfoDTO;
    }

    public List<UserInfoDTO> getAllUserInfo() {
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        List<UserInfoDTO> userInfoDTOList = new ArrayList<>();
        for (UserInfo userInfo : userInfoList) {
            userInfoDTOList.add(getUserDetails(userInfo.getUserId()));
        }
        return userInfoDTOList;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserInfo addUserInfo(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }
}
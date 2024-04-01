package com.drm.server.service;

import com.drm.server.domain.admin.Admin;
import com.drm.server.domain.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    public Admin findById(Long adminId){
        return adminRepository.findById(adminId).orElseThrow(() -> new IllegalArgumentException("invalid adminId"));
    }

}

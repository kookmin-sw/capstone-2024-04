package com.drm.server.service;

import com.drm.server.domain.admin.Admin;
import com.drm.server.domain.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    public Admin findById(Long adminId){
        return adminRepository.findById(adminId).orElseThrow(() -> new IllegalArgumentException("invalid adminId"));
    }

}

package com.drm.server.service;

import com.drm.server.domain.admin.Admin;
import com.drm.server.domain.admin.AdminRepository;
import com.drm.server.domain.user.CustomUserDetails;
import com.drm.server.domain.user.CustomUserInfoDto;
import com.drm.server.domain.user.User;
import com.drm.server.domain.user.UserRepository;
import com.google.firebase.auth.UserIdentifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository usersRepository;
    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Long id = Long.parseLong(userId);
        if(id == 9999){
            Admin admin = adminRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당하는 관리자가 앖다"));
            CustomUserInfoDto userInfoDto = CustomUserInfoDto.builder().email(admin.getEmail()).deleted(false).authority(admin.getAuthority()).userId(admin.getAdminId()).build();
            return new CustomUserDetails(userInfoDto);
        }
        User user = usersRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없다"));
        CustomUserInfoDto userInfoDto = modelMapper.map(user, CustomUserInfoDto.class);
        return new CustomUserDetails(userInfoDto);
    }
}

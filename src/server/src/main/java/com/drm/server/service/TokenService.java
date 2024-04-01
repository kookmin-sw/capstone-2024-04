package com.drm.server.service;

import com.drm.server.config.jwt.JwtTokenProvider;
import com.drm.server.controller.dto.request.UserRequest;
import com.drm.server.controller.dto.response.UserResponse;
import com.drm.server.domain.admin.Admin;
import com.drm.server.domain.admin.AdminRepository;
import com.drm.server.domain.user.CustomUserInfoDto;
import com.drm.server.domain.user.User;
import com.drm.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final AdminRepository adminRepository;
    public UserResponse.TokenInfo createToken(UserRequest.SignIn signIn){
        User getUser = userRepository.findByEmail(signIn.getEmail()).orElseThrow(() -> new IllegalArgumentException("이메일 정보가 없습니다"));
        if(!passwordEncoder.matches(signIn.getPassword(),getUser.getPassword())) throw new IllegalArgumentException("잘못된 비밀번호 입니다");
        CustomUserInfoDto userInfo = modelMapper.map(getUser, CustomUserInfoDto.class);
        UserResponse.TokenInfo  tokenInfo = jwtTokenProvider.generateToken(userInfo);
        //RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisService.setValuesWithTimeUnit("RT:" + getUser.getUserId(),tokenInfo.getRefreshToken(),tokenInfo.getRefreshTokenExpirationTime(),TimeUnit.MILLISECONDS);
        return tokenInfo;
    }
    public UserResponse.TokenInfo createAdminToken(UserRequest.SignIn signIn){
        Admin getAdmin = adminRepository.findByEmail(signIn.getEmail()).orElseThrow(()-> new IllegalArgumentException("이메일 정보가 없습니다"));
        if(!passwordEncoder.matches(signIn.getPassword(),getAdmin.getPassword())) throw new IllegalArgumentException("잘못된 비밀번호 입니다");
        CustomUserInfoDto userInfo = CustomUserInfoDto.builder().userId(getAdmin.getAdminId()).authority(getAdmin.getAuthority()).deleted(false).build();
        UserResponse.TokenInfo  tokenInfo = jwtTokenProvider.generateToken(userInfo);
        //RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisService.setValuesWithTimeUnit("RT:" +getAdmin.getAdminId(),tokenInfo.getRefreshToken(),tokenInfo.getRefreshTokenExpirationTime(),TimeUnit.MILLISECONDS);
        return tokenInfo;
    }
}

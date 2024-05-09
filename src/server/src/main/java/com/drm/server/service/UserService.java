package com.drm.server.service;

import com.drm.server.common.FileDto;
import com.drm.server.controller.dto.response.UserResponse;
import com.drm.server.domain.mediaApplication.MediaApplicationRepository;
import com.drm.server.domain.user.User;
import com.drm.server.domain.user.UserRepository;
import com.drm.server.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;





    public void checkDuplicatedEmail(String email)  {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            log.debug("UserService.checkDuplicatedEmail exception occur email: {}", email);
            throw new IllegalArgumentException("이미 사용중인 이메일 입니다");
        }
    }

    public UserResponse.UserInfo updateProfile(User user,FileDto fileDto){
        user.updateProfileImage(fileDto.getUploadFileUrl());
        return new UserResponse.UserInfo(userRepository.save(user));

    }
    public void verifyPassword(User user,String password){
        if(!passwordEncoder.matches(password,user.getPassword())) throw new IllegalArgumentException("잘못된 비밀번호 입니다");
    }
    public void updatePassword(User user, String password, String updatePassword){
        if(!passwordEncoder.matches(password,user.getPassword())) throw new IllegalArgumentException("잘못된 비밀번호 입니다");
        user.updatePassword(passwordEncoder.encode(updatePassword));
        userRepository.save(user);
    }
    public UserResponse.UserInfo createUser(String email, String password,String company) {
        this.checkDuplicatedEmail(email);
        User setUser = User.toEntity(email, passwordEncoder.encode(password),company);
        User getUser =userRepository.save(setUser);
        return new UserResponse.UserInfo(getUser);
    }
    public User getUser(String userId){
        return userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("Invalid userId"));
    }
    public User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid userId"));
    }
    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

//    public void withdrawal(User getUser) {
//        mediaApplicationRepository.existsRunningMedia()
//        getUser.withdrwal();
//
//    }
}

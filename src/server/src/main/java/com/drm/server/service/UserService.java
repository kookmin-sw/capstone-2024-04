package com.drm.server.service;

import com.drm.server.controller.dto.response.UserResponse;
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
    private static final String AUTH_CODE_PREFIX = "AuthCode:";

    private final UserRepository userRepository;

    private final EmailService emailService;
    private final RedisTemplate redisTemplate;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;



    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    public void sendCodeToEmail(String email)  {
        this.checkDuplicatedEmail(email);
        String title = "Do you Read Me 이메일 인증 번호";
        String authCode = this.createCode();
        emailService.sendEmail(email, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        redisService.setValuesWithDuration(AUTH_CODE_PREFIX + email,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));

    }

    public String verifiedCode(String email, String authCode)  {
        this.checkDuplicatedEmail(email);
        String redisAuthCode = (String)redisTemplate.opsForValue().get(AUTH_CODE_PREFIX + email);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
        if(authResult){
            return "인증에 성공했습니다";
        }else {
            throw new IllegalArgumentException("잘못된 인증정보 입니다");
        }
    }
    private void checkDuplicatedEmail(String email)  {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            log.debug("UserService.checkDuplicatedEmail exception occur email: {}", email);
            throw new IllegalArgumentException("이미 사용중인 이메일 입니다");
        }
    }

    private String createCode()  {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("userService.createCode() exception occur");
            throw new BusinessLogicException("NoSuchAlgorithmException");
        }
    }


    public UserResponse.UserInfo createUser(String email, String password) {
        this.checkDuplicatedEmail(email);
        User setUser = User.toEntity(email, passwordEncoder.encode(password));
        User getUser =userRepository.save(setUser);
        return new UserResponse.UserInfo(getUser.getUserId(), getUser.getEmail(), getUser.getCompany());
    }
    public User getUser(String userId){
        return userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("Invalid userId"));
    }
}

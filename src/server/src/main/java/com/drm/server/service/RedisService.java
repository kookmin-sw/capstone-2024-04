package com.drm.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate redisTemplate;
    private static String REFRESHTOKEN = "RefreshToken:";

    public void setValuesWithDuration(String key, String value, Duration duration){
        redisTemplate.opsForValue().set(key,
                value, duration);
    }
    public boolean checkExistsValue(String redisAuthCode) {
        return !(redisAuthCode == null || redisAuthCode.isBlank());
    }

    public void setValuesWithTimeUnit(String key, String value, Long expirationTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value,expirationTime ,timeUnit);
    }
    public String getRefrestToken(String userId){
        return  (String)redisTemplate.opsForValue().get(REFRESHTOKEN + userId);
    }
}

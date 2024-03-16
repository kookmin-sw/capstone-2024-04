package com.drm.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate redisTemplate;

    public void setValues(String key, String value, Duration duration){
        redisTemplate.opsForValue().set(key,
                value, duration);
    }

    public boolean checkExistsValue(String redisAuthCode) {
        return !(redisAuthCode == null || redisAuthCode.isBlank());
    }
}

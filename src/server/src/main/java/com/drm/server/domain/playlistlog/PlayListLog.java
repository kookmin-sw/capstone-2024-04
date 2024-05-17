package com.drm.server.domain.playlistlog;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "coupon", timeToLive = 7 * 24 * 60 * 60 * 1000L)
public class PlayListLog {
//    id는 장소id>생성시간
    @Id
    private Long id;
    private String startTime;
    private String endTime;
    private Long playListId;
    private Long mediaApplication;

}

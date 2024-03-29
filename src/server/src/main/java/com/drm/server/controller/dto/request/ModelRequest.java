package com.drm.server.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ModelRequest {
    private Long peopleId;
    private Long cameraId;
//    private Long mediaId;

    private LocalDateTime arriveTime;
    private LocalDateTime leaveTime;

    private int presentFrameCnt;
    private int interestFrameCnt;

    private List<Integer> frameData;
}

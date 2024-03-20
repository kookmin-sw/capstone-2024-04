package com.drm.server.controller.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ModelRequest {
    private int mediaId;
    private int cameraId;
    private int peopleId;
    private LocalDateTime arriveTime;
    private LocalDateTime leaveTime;
    private int presentFrameCnt;
    private int interestFrameCnt;
    private List<Integer> frameData;
}

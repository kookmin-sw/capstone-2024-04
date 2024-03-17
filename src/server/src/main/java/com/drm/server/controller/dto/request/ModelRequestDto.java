package com.drm.server.controller.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ModelRequestDto {
    private int peopleId;
    private String arriveTime;
    private String leaveTime;
    private int presentFrameCnt;
    private int interestFrameCnt;
    private List<Integer> frameData;
    private int cameraId;
}

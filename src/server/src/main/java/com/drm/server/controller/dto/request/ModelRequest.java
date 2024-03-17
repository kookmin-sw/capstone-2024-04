package com.drm.server.controller.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ModelRequest {
    private int mediaId;
    private int cameraId;
    private int peopleId;
    private String arriveTime;
    private String leaveTime;
    private int presentFrameCnt;
    private int interestFrameCnt;
    private List<Integer> frameData;
}

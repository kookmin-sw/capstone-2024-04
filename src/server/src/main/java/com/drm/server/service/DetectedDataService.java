package com.drm.server.service;

import org.springframework.stereotype.Service;

@Service
public class DetectedDataService {
    private void saveDetectedData(){
        // 한번의 찍혀온 데이터 저장 로직
        // 1. 해당 Media 관련 데이터 업데이트(광고별 통계)
        // 2. DetectedFace에 사람별 데이터 저장
    }
}

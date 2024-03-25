package com.drm.server.service;

import com.drm.server.controller.dto.request.ModelRequest;
import com.drm.server.domain.detectedface.DetectedFace;
import com.drm.server.domain.detectedface.DetectedFaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class DetectedDataServiceTest {

    @Autowired
    private DetectedDataService detectedDataService;

    @Autowired
    private DetectedFaceRepository detectedFaceRepository;
    //
    @Autowired
    DetectedDataServiceTest(DetectedDataService detectedDataService, DetectedFaceRepository detectedFaceRepository) {
        this.detectedDataService = detectedDataService;
        this.detectedFaceRepository = detectedFaceRepository;
    }
//    private final MediaService mediaService;
//
//    private final MediaRepository mediaRepository;


//    @Autowired
//    DetectedDataServiceTest (DetectedDataService detectedDataService, MediaService mediaService, DetectedFaceRepository detectedFaceRepository, MediaRepository mediaRepository) {
//        this.detectedDataService = detectedDataService;
//        this.mediaService = mediaService;
//        this.detectedFaceRepository = detectedFaceRepository;
//        this.mediaRepository = mediaRepository;
//    }

    @Test
    public void serviceLoadTest(){
        boolean t = detectedDataService.checkPeopleIndex(0L);
    }

    @Test
    public void processDetectedData() {
        // 통합 테스트
        // given
        LocalDateTime time = LocalDateTime.now();
        List<Integer> intList = new ArrayList<>();

        // Create a Random Int
        Random random = new Random();
        int totalFrameCnt = random.nextInt(10) + 10;
        int interestFrameCnt = 0;
        for (int i = 0; i < totalFrameCnt; i++) {
            int randomInt = random.nextInt(2);
            if(randomInt > 0) { interestFrameCnt += 1;}
            intList.add(randomInt);
        }
        ModelRequest modelRequest = ModelRequest.builder()
                .cameraId(3L)
                .arriveTime(time.minusSeconds(20))
                .leaveTime(time)
                .presentFrameCnt(totalFrameCnt)
                .interestFrameCnt(interestFrameCnt)
                .frameData(intList)
                .build();

        // when
       detectedDataService.processDetectedData(modelRequest);
       DetectedFace detectedFace = detectedFaceRepository.findFirstByOrderByDetectedFaceIdDesc();

        // then
        assertEquals(modelRequest.getInterestFrameCnt(), detectedFace.getFaceCaptureCnt());
        assertEquals(modelRequest.getPresentFrameCnt(), detectedFace.getEntireCaptureCnt());
    }

    @Test
    public void SaveDetectedData() {
        LocalDateTime time = LocalDateTime.now();
        List<Integer> intList = new ArrayList<>();

        // Create a Random Int
        Random random = new Random();
        int totalFrameCnt = random.nextInt(10) + 10;
        int interestFrameCnt = 0;
        for (int i = 0; i < totalFrameCnt; i++) {
            int randomInt = random.nextInt(2);
            if(randomInt > 0) { interestFrameCnt += 1;}
            intList.add(randomInt);
        }
        ModelRequest modelRequest = ModelRequest.builder()
                .cameraId(3L)
                .arriveTime(time.minusSeconds(20))
                .leaveTime(time)
                .presentFrameCnt(totalFrameCnt)
                .interestFrameCnt(interestFrameCnt)
                .frameData(intList)
                .build();

        detectedDataService.saveDetectedData(modelRequest, true);
        DetectedFace detectedFace = detectedFaceRepository.findFirstByOrderByDetectedFaceIdDesc();

        // then
        assertEquals(modelRequest.getInterestFrameCnt(), detectedFace.getFaceCaptureCnt());
        assertEquals(modelRequest.getPresentFrameCnt(), detectedFace.getEntireCaptureCnt());
    }

    @Test
    public void saveNotExistMediaData() {
    }

    @Test
    public void saveNotExistLocationData() {
    }

    @Test
    public void saveBadFrameData() {
    }

    @Test
    public void saveBadPeopleCntData() {
    }
}
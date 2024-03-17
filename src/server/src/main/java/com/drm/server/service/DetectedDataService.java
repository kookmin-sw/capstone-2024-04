package com.drm.server.service;

import com.drm.server.controller.dto.request.ModelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetectedDataService {

    private final MediaService mediaService;
    public void processDetectedData(ModelRequest modelRequest){
        // 한번의 찍혀온 데이터 저장 로직
        // 1. DetectedFace에 사람별 데이터 저장
        this.saveDetectedData(modelRequest);
        // 2. 해당 Media 관련 데이터 업데이트(광고별 통계)
        boolean dataExistBool = checkDataExist(modelRequest);
        boolean dataValidBool = checkDataValid(modelRequest);
        if(dataExistBool && dataValidBool) {
            boolean interestBool = checkPeopleInterest(modelRequest.getInterestFrameCnt());
            mediaService.updateMediaData(modelRequest.getMediaId(), interestBool);
        }
    }

    public void saveDetectedData(ModelRequest modelRequest){

    }

    public boolean checkPeopleInterest(int interestFrameCnt){
        // Data 를 판단하여 Interest 으로 판단할지 아닐지 검증하는 로직
        if(interestFrameCnt > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean checkDataExist(ModelRequest modelRequest){
        // mediaId 존재 여부 검증 / cameraId 존재 여부 검증 / peopleId (사람 라벨링 인덱스가 순차적으로 들어오는지 판단)

        return true;
    }

    public boolean checkDataValid(ModelRequest modelRequest){
        // Data 검증

        return true;
    }
}

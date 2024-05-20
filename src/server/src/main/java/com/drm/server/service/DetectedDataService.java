package com.drm.server.service;

import com.drm.server.controller.dto.request.ModelRequest;
import com.drm.server.domain.detectedface.DetectedFace;
import com.drm.server.domain.detectedface.DetectedFaceRepository;
import com.drm.server.domain.location.LocationRepository;
import com.drm.server.domain.media.MediaRepository;
import com.drm.server.domain.mediaApplication.MediaApplication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class DetectedDataService {

    // 아래의 Service / Repository 를 어떤 식으로 주입해줄 수 있을까? -> 추후에 사진이 아닌, 영상용 데이터가 들어왔을때를 고려한다.
    // 궁금한 것 -> 이런 식으로 autowired 없이 필드 주입시에, DetectedDataService 호출시마다,
    // 새로운 필드 변수(mediaService, RowDetectedFaceRepository) 가 생성될까?
    private final MediaService mediaService;
    private final PlayListService playListService;
    private final DailyMediaBoardService dailyMediaBoardService;
    private final DetectedFaceRepository detectedFaceRepository;
    private final MediaRepository mediaRepository;
    private final LocationRepository locationRepository;

    public void processDetectedData(ModelRequest modelRequest){
        // 사람별 Data 저장하는 서비스 로직
        // 데이터 상에 문제가 없을 시 해당 Media 관련 데이터 업데이트(광고별 통계)
        boolean mediaExist = checkMediaExist(modelRequest.getCameraId());
        boolean locationExist = checkLocationExist(modelRequest.getCameraId());

        boolean dataValid = checkDataValid(modelRequest);
        boolean peopleIndexValid = checkPeopleIndex(modelRequest.getPeopleId());
        boolean useThisData = false;

        if(mediaExist && locationExist &&  dataValid && peopleIndexValid) {
            boolean interestBool = checkPeopleInterest(modelRequest.getInterestFrameCnt());
            // mediaRepository 여러번 쿼리 던지는 것 리팩토링 해야됨.
            LocalDateTime time = modelRequest.getArriveTime();
            MediaApplication currentMedia = playListService.getMediaApplicationFromPlaylist(modelRequest.getCameraId(), time);
//            dailyMediaBoardService.updateMediaData(currentMedia, modelRequest, interestBool);
            useThisData = true;
        }
        // 데이터 문제 여부와 상관 없이 DetectedFace에 사람별 데이터 그대로 저장
        this.saveDetectedData(modelRequest, useThisData);
    }

    public void saveDetectedData(ModelRequest modelRequest, Boolean useBool){
        DetectedFace detectedFace = DetectedFace.builder()
                .detectedFaceId(modelRequest.getPeopleId())
                .arriveAt(modelRequest.getArriveTime())
                .leaveAt(modelRequest.getLeaveTime())
                .faceCaptureCnt(modelRequest.getInterestFrameCnt())
                .entireCaptureCnt(modelRequest.getPresentFrameCnt())
                .staring(modelRequest.getFrameData())
                .used(useBool)
                .build();
        detectedFaceRepository.save(detectedFace);
    }


    public boolean checkMediaExist(Long cameraId){
        // mediaId 존재 여부 검증 / peopleId (사람 라벨링 인덱스가 순차적으로 들어오는지 판단)
//        if(!(mediaRepository.existsById(mediaId))){
//            log.debug("[FAIL] MEDIA ID : " + mediaId + " NOT EXIST "  + "\n");
//            return false;
//        }
        Long mediaId = 0L;
        log.debug("[SUCCESS] MEDIA ID : " + mediaId + " EXIST " + "\n");
        return true;
    }

    public boolean checkLocationExist(Long cameraId){
        // cameraId(location) 존재 여부 검증
        // camera -> location -> playlist 순으로 돌아가야 mediaId 검증 가능
//        if(!(locationRepository.existsById(locationId))){
//            log.debug("[FAIL] CAMERA(LOCATION) ID : " + locationId + " NOT EXIST " + "\n");
//            return false;
//        }
        Long locationId = 0L;
        log.debug("[SUCCESS] CAMERA(LOCATION) ID : " + locationId + " EXIST "  + "\n");
        return true;
    }
    public boolean checkDataValid(ModelRequest modelRequest){
        // staring List Data 검증
        // Bad case - FrameCnt 와 데이터의 개수가 맞지 않을 경우
        if(modelRequest.getFrameData().size() != modelRequest.getPresentFrameCnt()){
            log.debug("[FAIL] DATA SIZE DIFFERS FROM PROMISED FRAME CNT " + modelRequest.getFrameData()  + "\n");
            return false;
        }
        System.out.println("[SUCCESS] FRAME DATA VALID " + "\n");

        return true;
    }
    public boolean checkPeopleIndex(Long peopleId){
        // 사람 인덱스 확인 요구사항 = 기존에 나왔던 라벨과 중복 없이 올라가는지
        // Bad case - 같은 사람에 대한 데이터를 여러번 보내는 경우
        // Bad case - 모델의 처리 문제 등으로 라벨이 1,5,7 등 불연속적으로 붙어서 요청 들어올 경우 - 어떻게? 기존 사람 디비의 라벨링 넘버 + 1 인지 확인할까?
        // ModelServer 는 보내는 People Label Index 를 가지고 있지 않을텐데? 그럼 새로 보낼때마다 0부터 다시 보내지 않나? 그럼 어떻게하지..
        // 그냥 peopleLabel 을 안 받고, 보내는 건 다 "새로운 사람" 이라고 가정해서 처리해야 할 수도 있을 듯..
        return true;
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
}

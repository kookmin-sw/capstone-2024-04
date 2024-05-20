package com.drm.server.messageq;

import com.drm.server.controller.dto.response.PlayListLog;
import com.drm.server.controller.dto.response.PlayListResponse;
import com.drm.server.domain.dailyMediaBoard.DailyMediaBoard;
import com.drm.server.domain.detectedface.DetectedFace;
import com.drm.server.domain.detectedface.DetectedFaceRepository;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.rowdetectedface.RowDetectedFace;
import com.drm.server.domain.rowdetectedface.RowDetectedFaceRepository;
import com.drm.server.service.DailyDetailBoardService;
import com.drm.server.service.DailyMediaBoardService;
import com.drm.server.service.MediaApplicationService;
import com.drm.server.service.PlayListService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class DetectedFaceConsumer {
    @Autowired
    private final DetectedFaceRepository detectedFaceRepository;
    private final MediaApplicationService mediaApplicationService;
    private final DailyMediaBoardService dailyMediaBoardService;
    private final DailyDetailBoardService dailyDetailBoardService;
    private final RowDetectedFaceRepository rowDetectedFaceRepository;
    private final KSqlDBHandler kSqlDBHandler;


    //컨슈머가 캐치하는 구간
    @KafkaListener(topics = "drm-face-topic")
    public void updateQty(String kafkaMessage) throws IOException {
        log.info("얼굴인식 Kafka Message: ->" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        List<Integer> startTimeList = (List<Integer>) map.get("startAt");
        List<Integer> arriveTimeList = (List<Integer>) map.get("leaveAt");

//        모델 row 데이터 저장
        RowDetectedFace rowDetectedFace = RowDetectedFace.toEntity(map, startTimeList, arriveTimeList);
        rowDetectedFaceRepository.save(rowDetectedFace);

        //광고 찾는 로직 추가
        String qurey = kSqlDBHandler.getFilteredData(Long.valueOf((Integer) map.get("cameraId")),rowDetectedFace.getArriveAt().toString(),rowDetectedFace.getLeaveAt().toString());
        List<PlayListLog> playListLog = kSqlDBHandler.queryKsqlDb(qurey);

//         Mediaapplication를 찾아서 기존 detectedFace 넣기,
//        DetectedFace detectedFace = DetectedFace.toEntity(map, startTimeList, arriveTimeList);
////        기존 찾는 로직. 이거 없애고 playListLog 여기서 나온 값을 가지고 팢아서 넣으면 됨
//        MediaApplication mediaApplication = mediaApplicationService.findByCameraIdAndDate((Integer) map.get("cameraId"), detectedFace.getArriveAt());
//        detectedFace.updateMediaApplication(mediaApplication);
//        DetectedFace savedDetectedFace = detectedFaceRepository.save(detectedFace);
////       갱신 대시보드 업데이트
//        DailyMediaBoard dailyMediaBoard = dailyMediaBoardService.updateDailyBoard(savedDetectedFace);
//        dailyDetailBoardService.updateDatailBoard(detectedFace,dailyMediaBoard);
    }
}
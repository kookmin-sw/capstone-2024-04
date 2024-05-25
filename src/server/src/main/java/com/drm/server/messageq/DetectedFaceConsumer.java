package com.drm.server.messageq;

import com.drm.server.controller.dto.response.PlayListLog;
import com.drm.server.controller.dto.response.PlayListResponse;
import com.drm.server.domain.dailyMediaBoard.DailyMediaBoard;
import com.drm.server.domain.detectedface.DetectedFace;
import com.drm.server.domain.detectedface.DetectedFaceRepository;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.playlist.PlayList;
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
import java.util.ArrayList;
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
    private final PlayListService playListService;
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

        List<DetectedFace> detectedFaceList = new ArrayList<>();
        // 한 사람의 등장 - 퇴장 사이에 여러개의 광고가 노출되었을 경우(Playlist에서 송출되는 광고)
        for(PlayListLog playListLogData : playListLog ){
            MediaApplication mediaApplication = playListService.getMediaApplicationFromPlaylistId(playListLogData.getPlaylistId());
            // playList 광고 시작, 전환 시간을 포함한 detectedFace save
            // DetectedFace toEntity 내부에서 시간대 단위로 몇개 프레임에 해당되는 것까지 끊어지는지 계산하고
            // 수치 업데이트하여 각자 저장.
            DetectedFace detectedFace = DetectedFace.toEntity(map, playListLogData.getStartTime(), playListLogData.getEndTime());
            detectedFace.updateMediaApplication(mediaApplication);
            DetectedFace savedDetectedFace = detectedFaceRepository.save(detectedFace);
            detectedFaceList.add(savedDetectedFace);
        }

////       갱신 대시보드 업데이트
//        DailyMediaBoard dailyMediaBoard = dailyMediaBoardService.updateDailyBoard(savedDetectedFace);
//        dailyDetailBoardService.updateDatailBoard(detectedFace,dailyMediaBoard);
    }
}
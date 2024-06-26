package com.drm.server.messageq;

import com.drm.server.domain.dailyMediaBoard.DailyMediaBoard;
import com.drm.server.domain.detectedface.DetectedFace;
import com.drm.server.domain.detectedface.DetectedFaceRepository;
import com.drm.server.domain.mediaApplication.MediaApplication;
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


  //   컨슈머가 캐치하는 구간
//    @KafkaListener(topics = "drm-face-topic")
////    @KafkaListener(topics = "drm-face-topic", containerFactory = "kafkaBatchListeningFactory")
//    public void updateQty(String kafkaMessage) {
//        log.info("얼굴인식 Kafka Message: ->" + kafkaMessage);
//
//        Map<Object, Object> map = new HashMap<>();
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
//        } catch (JsonProcessingException ex) {
//            ex.printStackTrace();
//        }
//
//        List<Integer> startTimeList = (List<Integer>) map.get("startAt");
//        List<Integer> arriveTimeList = (List<Integer>) map.get("leaveAt");
//
//        DetectedFace detectedFace = DetectedFace.toEntity(map, startTimeList, arriveTimeList);
//
//        MediaApplication mediaApplication = mediaApplicationService.findByCameraIdAndDate((Integer) map.get("cameraId"), detectedFace.getArriveAt());
//        detectedFace.updateMediaApplication(mediaApplication);
//        DetectedFace savedDetectedFace = detectedFaceRepository.save(detectedFace);
//        DailyMediaBoard dailyMediaBoard = dailyMediaBoardService.updateDailyBoard(savedDetectedFace);
//        dailyDetailBoardService.updateDatailBoard(detectedFace,dailyMediaBoard);
//    }

//    컨슈머가 캐치하는 구간 -> Batch Listener
    @KafkaListener(topics = "drm-face-topic")
    public void updateBatchQty(List<String> kafkaMessages) {
        for (String kafkaMessage : kafkaMessages) {
            log.info("얼굴인식 Kafka Message: ->" + kafkaMessage);

            // Your existing processing logic for each Kafka message
            Map<Object, Object> map = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();
            try {
                map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
                });
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }

            List<Integer> startTimeList = (List<Integer>) map.get("startAt");
            List<Integer> arriveTimeList = (List<Integer>) map.get("leaveAt");

            DetectedFace detectedFace = DetectedFace.toEntity(map, startTimeList, arriveTimeList);

            MediaApplication mediaApplication = mediaApplicationService.findByCameraIdAndDate((Integer) map.get("cameraId"), detectedFace.getArriveAt());
            detectedFace.updateMediaApplication(mediaApplication);
            DetectedFace savedDetectedFace = detectedFaceRepository.save(detectedFace);
            DailyMediaBoard dailyMediaBoard = dailyMediaBoardService.updateDailyBoard(savedDetectedFace);
            dailyDetailBoardService.updateDatailBoard(detectedFace, dailyMediaBoard);
        }
    }
}
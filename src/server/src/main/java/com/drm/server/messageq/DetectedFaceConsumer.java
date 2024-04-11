package com.drm.server.messageq;

import com.drm.server.domain.detectedface.DetectedFace;
import com.drm.server.domain.detectedface.DetectedFaceRepository;
import com.drm.server.domain.mediaApplication.MediaApplication;
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


    //컨슈머가 캐치하는 구간
    @KafkaListener(topics = "drm-face-topic")
    public void updateQty(String kafkaMessage) {
        log.info("Kafka Message: ->" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        List<Integer> dateTimeList = (List<Integer>) map.get("startAt");
        int year = dateTimeList.get(0);
        int month = dateTimeList.get(1);
        int day = dateTimeList.get(2);
        int hour = dateTimeList.get(3);
        int minute = dateTimeList.get(4);
        int second = dateTimeList.get(5);
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute, second);

        DetectedFace detectedFace = DetectedFace.builder()
                .faceCaptureCnt((Integer) map.get("interestPeopleCnt"))
                .entireCaptureCnt((Integer) map.get("passedPeopleCnt"))
                .staring((List<Boolean>) map.get("staringData"))
                .arriveAt(localDateTime)
                .leaveAt(localDateTime)
                .age((Integer)map.get("age"))
                .male((Boolean)map.get("male"))
                .fps((Integer) map.get("fps"))
                .build();

        MediaApplication mediaApplication = mediaApplicationService.findByCameraIdAndDate((Integer) map.get("cameraId"), detectedFace.getArriveAt());
        detectedFace.updateMediaApplication(mediaApplication);

        detectedFaceRepository.save(detectedFace);
    }
}
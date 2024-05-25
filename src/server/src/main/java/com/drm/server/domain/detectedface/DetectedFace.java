package com.drm.server.domain.detectedface;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.common.KoreaLocalDateTime;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.handler.LongConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DetectedFace extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detectedFaceId;

    @Column
    private LocalDateTime arriveAt;
    private LocalDateTime leaveAt;


    @Convert(converter = LongConverter.class)
    @Column(length = 4000)
    private List<Boolean> staring;

    private int faceCaptureCnt;
    private int entireCaptureCnt;

    private int age;
    private boolean male;
    private boolean used;
    private int fps;

    @ManyToOne
    @JoinColumn(name = "media_application")
    private MediaApplication mediaApplication;

    public void updateMediaApplication(MediaApplication mediaApplication) {
        this.mediaApplication = mediaApplication;
    }

    public static DetectedFace toEntity(Map<Object, Object> modelRequest,List<Integer> startTimeList, List<Integer> arriveTimeList) {
        LocalDateTime arriveAt = KoreaLocalDateTime.datTimeListToLocalDateTime(startTimeList);
        LocalDateTime leaveAt = KoreaLocalDateTime.datTimeListToLocalDateTime(arriveTimeList);
        return makeDetectedFace(modelRequest, arriveAt, leaveAt);
    }
    public static DetectedFace toEntity(Map<Object, Object> modelRequest, String startTime, String arriveTime) {
        // Time converter String to LocalDateTime
        // String example : "2023-05-10T12:34:56.000"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime arriveAt = LocalDateTime.parse(startTime, formatter);
        LocalDateTime leaveAt = LocalDateTime.parse(arriveTime, formatter);
        return makeDetectedFace(modelRequest, arriveAt, leaveAt);
    }

    private static DetectedFace makeDetectedFace(Map<Object, Object> modelRequest, LocalDateTime startAt, LocalDateTime endAt) {
        // Split data
        // 사람별 등장, 퇴장 시간
        List<Integer> arriveTimeList = (List<Integer>) modelRequest.get("startAt");
        List<Integer> leaveTimeList = (List<Integer>) modelRequest.get("leaveAt");
        LocalDateTime arriveAt = KoreaLocalDateTime.datTimeListToLocalDateTime(arriveTimeList);
        LocalDateTime leaveAt = KoreaLocalDateTime.datTimeListToLocalDateTime(leaveTimeList);

        // FPS (frames per second)
        int fps = (Integer) modelRequest.get("fps");


        // 광고별 시작 종료 시간
        // startAt, endAt are already provided as parameters
        int startFrameIndex = -1;
        int endFrameIndex = -1;

        if (!startAt.isBefore(arriveAt) && !startAt.isAfter(leaveAt)) {
            startFrameIndex = (int) Duration.between(arriveAt, startAt).getSeconds() * fps;
        }
        if (!endAt.isBefore(arriveAt) && !endAt.isAfter(leaveAt)) {
            endFrameIndex = (int) Duration.between(arriveAt, endAt).getSeconds() * fps;
        }

        // Extract staring data within the frame range
        List<Boolean> staringData = (List<Boolean>) modelRequest.get("staringData");
        List<Boolean> mappedStaringData = staringData.subList(Math.max(0, startFrameIndex), Math.min(staringData.size(), endFrameIndex));

        // Update faceCaptureCnt and entireCaptureCnt based on mappedStaringData
        int faceCaptureCnt = (int) modelRequest.get("interestFrameCnt");
        int entireCaptureCnt = (int) modelRequest.get("passedFrameCnt");

        // Optional: Recalculate based on the mapped staring data
        // Assuming interestFrameCnt is the count of true values in the staring data
        faceCaptureCnt = (int) mappedStaringData.stream().filter(b -> b).count();
        entireCaptureCnt = mappedStaringData.size();

        // Create the DetectedFace instance
        DetectedFace detectedFace = DetectedFace.builder()
                .faceCaptureCnt(faceCaptureCnt)
                .entireCaptureCnt(entireCaptureCnt)
                .staring(mappedStaringData)
                .arriveAt(arriveAt)
                .leaveAt(leaveAt)
                .age((Integer) modelRequest.get("age"))
                .male((Integer) modelRequest.get("male") == 1)
                .fps(fps)
                .build();

        return detectedFace;
    }
}

package com.drm.server.domain.rowdetectedface;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.common.KoreaLocalDateTime;
import com.drm.server.domain.location.Location;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.handler.LongConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RowDetectedFace extends BaseTimeEntity {
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
    @JoinColumn(name = "location_id")
    private Location location;


    public static RowDetectedFace toEntity(Map<Object, Object> modelRequest, List<Integer> startTimeList, List<Integer> arriveTimeList) {
        LocalDateTime arriveAt = KoreaLocalDateTime.datTimeListToLocalDateTime(startTimeList);
        LocalDateTime leaveAt = KoreaLocalDateTime.datTimeListToLocalDateTime(arriveTimeList);

        RowDetectedFace rowDetectedFace = RowDetectedFace.builder()
                .faceCaptureCnt((Integer) modelRequest.get("interestFrameCnt"))
                .entireCaptureCnt((Integer) modelRequest.get("passedFrameCnt"))
                .staring((List<Boolean>) modelRequest.get("staringData"))
                .arriveAt(arriveAt)
                .leaveAt(leaveAt)
                .age((Integer)modelRequest.get("age"))
                .male((Integer)modelRequest.get("male")==1?true:false)
                .fps((Integer) modelRequest.get("fps"))
                .build();
        return rowDetectedFace;
    }
}

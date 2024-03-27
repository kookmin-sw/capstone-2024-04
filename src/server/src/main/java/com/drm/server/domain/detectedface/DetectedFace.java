package com.drm.server.domain.detectedface;

import com.drm.server.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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


    @Convert(converter = com.drm.server.domain.detectedface.DetectedDataConverter.class)
    private List<Integer> staring;

//    private Long mediaId; (FK)
    private int faceCaptureCnt;
    private int entireCaptureCnt;
    private boolean used;
}

package com.drm.server.domain.detectedface;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DetectedFace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detectedFaceId;

    @Column
    private LocalDateTime arriveAt;
    private LocalDateTime leaveAt;


    @Convert(converter = DetectedDataConverter.class)
    private List<Integer> staring;

//    private Long mediaId; (FK)
    private int faceCaptureCnt;
    private int entireCaptureCnt;
    private boolean used;
}

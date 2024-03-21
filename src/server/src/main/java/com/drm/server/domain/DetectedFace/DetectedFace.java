package com.drm.server.domain.detectedface;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
public class DetectedFace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detectedFaceId;

    @Column
    private LocalDateTime arriveAt;
    private LocalDateTime leaveAt;
    private List<Boolean> staring;
//    private int videoLocation; (FK)
    private int faceCaptureCnt;
    private int entireCaptureCnt;
    private boolean used;
}

package com.drm.server.domain.DetectedFace;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class DetectedFace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int detectedFaceId;

    @Column
    private LocalDateTime arriveAt;
    private LocalDateTime leaveAt;
    private boolean staring;
//    private int videoLocation; (FK)
    private int faceCaptureCnt;
}

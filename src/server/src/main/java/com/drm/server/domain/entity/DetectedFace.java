package com.drm.server.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class DetectedFace {
    @Id
    private int detectedFaceId;
    private String arriveAt;
    private String leaveAt;
    private boolean staring;
//    private int videoLocation; (FK)
    private int face_capture_cnt;
}

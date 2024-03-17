package com.drm.server.domain;

import jakarta.persistence.*;

@Entity
public class DetectedFace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int detectedFaceId;

    @Column
    private String arriveAt;
    private String leaveAt;
    private boolean staring;
//    private int videoLocation; (FK)
    private int face_capture_cnt;
}

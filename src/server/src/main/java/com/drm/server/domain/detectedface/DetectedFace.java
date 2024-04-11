package com.drm.server.domain.detectedface;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.domain.mediaApplication.MediaApplication;
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


    @Convert(converter = DataConverter.class)
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
}

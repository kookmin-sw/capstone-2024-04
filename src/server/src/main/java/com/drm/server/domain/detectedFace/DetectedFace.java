package com.drm.server.domain.detectedFace;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.handler.StringListConverter;
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
    @Convert(converter = StringListConverter.class)
    private List<Boolean> staring;
//    private int videoLocation; (FK)
    private int faceCaptureCnt;
    private int entireCaptureCnt;
    private boolean used;
}

package com.drm.server.domain.media;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
@Getter
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;

    @Column
    private String mediaLink;
    private String title;
    // FK - dashboardId
    // mediaLength
}

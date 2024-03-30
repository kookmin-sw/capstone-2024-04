package com.drm.server.domain.media;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.controller.dto.request.MediaRequest;
import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.mediaApplication.MediaApplication;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Media extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;

    @Column
    private String mediaLink;
    private String title;
    private String description;
    // FK - dashboardId
    @OneToOne
    @JoinColumn(name = "dashboard_id")
    private Dashboard dashboard;
    // mediaLength

    @OneToMany(mappedBy = "media")
    private List<MediaApplication> mediaApplicationList = new ArrayList<>();

    public static Media toEntity(MediaRequest.Create mediaRequest, String mediaLink,Dashboard dashboard){
        return Media.builder().mediaLink(mediaLink).title(mediaRequest.getAdvertisementTitle()).description(mediaRequest.getAdvertisementDescription()).dashboard(dashboard).build();
    }

}

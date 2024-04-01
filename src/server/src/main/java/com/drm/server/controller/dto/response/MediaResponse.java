package com.drm.server.controller.dto.response;

import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.media.Media;
import lombok.*;

@RequiredArgsConstructor
public class MediaResponse {
    @Getter
    @Setter
    public static class MediaInfo{
        private Long mediaId;

        private String mediaLink;
        private String title;
        private String description;

        public MediaInfo(Dashboard dashboard) {
            this.mediaId = dashboard.getMedia().getMediaId();
            this.mediaLink = dashboard.getMedia().getMediaLink();
            this.title = dashboard.getMedia().getTitle();
            this.description = dashboard.getMedia().getDescription();
        }

        public MediaInfo(Media media) {
            this.mediaId = media.getMediaId();
            this.mediaLink = media.getMediaLink();
            this.title = media.getTitle();
            this.description = media.getDescription();
        }
    }
    @Getter
    @Setter
    @Builder
    public static class MediaAndDashboardInfo{
        private MediaInfo mediaInfo;
        private DashboardResponse.DashboardInfo dashboardInfo;
    }
}

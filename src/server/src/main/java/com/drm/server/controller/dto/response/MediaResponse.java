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
        private DashboardResponse.DashboardInfo dashboard;

        public MediaInfo(Dashboard dashboard) {
            this.mediaId = dashboard.getMedia().getMediaId();
            this.mediaLink = dashboard.getMedia().getMediaLink();
            this.title = dashboard.getMedia().getTitle();
            this.description = dashboard.getMedia().getDescription();
            this.dashboard = new DashboardResponse.DashboardInfo(dashboard);
        }

        public MediaInfo(Media media) {
            this.mediaId = media.getMediaId();
            this.mediaLink = media.getMediaLink();
            this.title = media.getTitle();
            this.description = media.getDescription();
            this.dashboard = new DashboardResponse.DashboardInfo(media.getDashboard());
        }
    }

}

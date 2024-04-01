package com.drm.server.controller.dto.response;

import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.media.Media;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@RequiredArgsConstructor
public class MediaResponse {
    @Getter
    @Setter
    public static class MediaInfo{
        @Schema(description = "광고 id",example = "1")
        private Long mediaId;
        @Schema(description = "광고 이미지 링크",example = "https://kr.object.ncloudstorage.com/k-eum/image/..")
        private String mediaLink;
        @Schema(description = "광고 제목",example = "이것은 광고 제목")
        private String title;
        @Schema(description = "광고 설명",example = "이것은 광고 설명")
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

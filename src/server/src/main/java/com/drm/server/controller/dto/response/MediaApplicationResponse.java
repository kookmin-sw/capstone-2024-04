package com.drm.server.controller.dto.response;

import com.drm.server.common.KoreaLocalDateTime;
import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.location.Location;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.mediaApplication.Status;
import lombok.Getter;
import lombok.Setter;

public class MediaApplicationResponse {
    @Getter
    @Setter
    public static class MediaApplicationInfo{
        private Long mediaApplicationId;
        private String startDate;
        private String endDate;
        private Status status;
        private LocationResponse.LocationInfo locationInfo;

        public MediaApplicationInfo(MediaApplication mediaApplication, Location location) {
            this.mediaApplicationId = mediaApplication.getMediaApplicationId();
            this.startDate = mediaApplication.getStartDate().toString();
            this.endDate = mediaApplication.getEndDate().toString();
            this.status = mediaApplication.getStatus();
            this.locationInfo = new LocationResponse.LocationInfo(location);
        }
    }
    @Getter
    @Setter

    public static class TotalApplicationInfo{
        private MediaResponse.MediaInfo mediaInfo;
        private DashboardResponse.DashboardInfo dashboardInfo;
        private MediaApplicationInfo mediaApplicationInfo;

        public TotalApplicationInfo(Media media, Dashboard dashboard, MediaApplication mediaApplication, Location location) {
            this.mediaInfo = new MediaResponse.MediaInfo(media);
            this.dashboardInfo = new DashboardResponse.DashboardInfo(dashboard);
            this.mediaApplicationInfo = new MediaApplicationInfo(mediaApplication,location);
        }
    }
}

package com.drm.server.controller.dto.response;

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
        private UserResponse.UserInfo userInfo;

        public MediaApplicationInfo(MediaApplication mediaApplication) {
            this.mediaApplicationId = mediaApplication.getMediaApplicationId();
            this.startDate = mediaApplication.getStartDate().toString();
            this.endDate = mediaApplication.getEndDate().toString();
            this.status = mediaApplication.getStatus();
            this.locationInfo = new LocationResponse.LocationInfo(mediaApplication.getLocation());
            this.userInfo = new UserResponse.UserInfo(mediaApplication.getMedia().getDashboard().getUser().getUserId(), "", mediaApplication.getMedia().getDashboard().getUser().getCompany());
        }
    }
    @Getter
    @Setter
    public static class TotalApplicationInfo{
        private MediaResponse.MediaInfo mediaInfo;
        private MediaApplicationInfo mediaApplicationInfo;

        public TotalApplicationInfo(Media media, MediaApplication mediaApplication) {
            this.mediaInfo = new MediaResponse.MediaInfo(media);
            this.mediaApplicationInfo = new MediaApplicationInfo(mediaApplication);
        }
    }


}

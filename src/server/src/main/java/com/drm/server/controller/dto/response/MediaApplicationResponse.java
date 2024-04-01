package com.drm.server.controller.dto.response;

import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.mediaApplication.Status;
import lombok.Getter;
import lombok.Setter;

public class MediaApplicationResponse {
    @Getter
    @Setter
    public static class MediaApplicationInfo{
        private Long applicationId;
        private String startDate;
        private String endDate;
        private Status status;
        private LocationResponse.LocationInfo location;
        private UserResponse.UserInfo user;

        public MediaApplicationInfo(MediaApplication mediaApplication) {
            this.applicationId = mediaApplication.getMediaApplicationId();
            this.startDate = mediaApplication.getStartDate().toString();
            this.endDate = mediaApplication.getEndDate().toString();
            this.status = mediaApplication.getStatus();
            this.location = new LocationResponse.LocationInfo(mediaApplication.getLocation());
            this.user = new UserResponse.UserInfo(mediaApplication.getMedia().getDashboard().getUser().getUserId(), "", mediaApplication.getMedia().getDashboard().getUser().getCompany());
        }
    }
    @Getter
    @Setter
    public static class TotalApplicationInfo{
        private MediaResponse.MediaInfo media;
        private MediaApplicationInfo application;

        public TotalApplicationInfo(MediaApplication mediaApplication) {
            this.media = new MediaResponse.MediaInfo(mediaApplication.getMedia());
            this.application = new MediaApplicationInfo(mediaApplication);
        }
    }


}


package com.drm.server.controller.dto.response;

import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.mediaApplication.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class MediaApplicationResponse {
    @Getter
    @Setter
    public static class MediaApplicationInfo{
        @Schema(description = "신청 id")
        private Long applicationId;
        @Schema(description = "등록 날짜",example = "2024-04-05")
        private String startDate;
        @Schema(description = "마감날짜",example = "2024-04-15")
        private String endDate;
        @Schema(description = "지원상태",example = "WAITING")
        private Status status;
        private LocationResponse.LocationInfo location;
        private UserResponse.UserInfo user;

        public MediaApplicationInfo(MediaApplication mediaApplication) {
            this.applicationId = mediaApplication.getMediaApplicationId();
            this.startDate = mediaApplication.getStartDate().toString();
            this.endDate = mediaApplication.getEndDate().toString();
            this.status = mediaApplication.getStatus();
            this.location = new LocationResponse.LocationInfo(mediaApplication.getLocation());
            this.user = new UserResponse.UserInfo(mediaApplication.getMedia().getDashboard().getUser());
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


package com.drm.server.controller.dto.request;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Valid
public class MediaRequest {
    @Getter
    @Setter
    public static class Create{
        private String dashboardTitle;
        private String dashboardDescription;
        private String advertisementTitle;
        private String advertisementDescription;
        private Long locationId;
        private String startDate;
        private String endDate;


    }
}

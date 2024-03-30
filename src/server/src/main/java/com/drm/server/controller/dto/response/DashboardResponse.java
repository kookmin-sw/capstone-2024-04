package com.drm.server.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class DashboardResponse {
    @Builder
    @Getter
    @Setter
    public static class DashboardInfo{
        private String title;
        private String description;


    }
}

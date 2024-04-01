package com.drm.server.controller.dto.response;

import com.drm.server.domain.dashboard.Dashboard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class DashboardResponse {
    @Getter
    @Setter
    public static class DashboardInfo{
        private String title;
        private String description;

        public DashboardInfo(Dashboard dashboard) {
            this.title = dashboard.getTitle();
            this.description = dashboard.getDescription();
        }
    }
}

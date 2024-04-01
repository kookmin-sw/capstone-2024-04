package com.drm.server.controller.dto.response;

import com.drm.server.domain.dashboard.Dashboard;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class DashboardResponse {
    @Getter
    @Setter
    public static class DashboardInfo{
        @Schema(description = "가장 큰 단위의 대시보드 제목",example = "대시보드 제목")
        private String title;
        @Schema(description = "가장 큰 단위의 대시보드 설명",example = "대시보드 설명")
        private String description;

        public DashboardInfo(Dashboard dashboard) {
            this.title = dashboard.getTitle();
            this.description = dashboard.getDescription();
        }
    }
}

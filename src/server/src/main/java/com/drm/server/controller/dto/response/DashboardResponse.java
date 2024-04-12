package com.drm.server.controller.dto.response;

import com.drm.server.domain.dashboard.Dashboard;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

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

    @Getter
    @Setter
    public static class DashboardAdvertInfo{
        @Schema(description = "시간대별 관심도", example = "[20,31,50,20,30,6,20,31,50,20,30,6]")
        private List<Long> interestPerHour;

        @Schema(description = "디스플레이 관심도", example = "[27.3, 20.1, 7.5]")
        private Map<Long, Float> interestPerDisplay;

        @Schema(description = "관심 인구 성비", example = "55")
        private int malePercent;

//        @Schema()
        // 유동인구 나이대
        // ? 어떻게 구현

        @Schema(description = "관심 시간", example = "3.1")
        private float stareAvgTime;

    }
}

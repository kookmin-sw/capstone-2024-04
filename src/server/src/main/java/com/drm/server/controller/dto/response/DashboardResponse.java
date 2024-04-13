package com.drm.server.controller.dto.response;

import com.drm.server.domain.dashboard.Dashboard;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

public class DashboardResponse {
    @Getter
    @Setter
    public static class DashboardInfo{
        @Schema(description = "가장 큰 단위의 대시보드 제목",example = "대시보드 제목")
        private String title;
        @Schema(description = "가장 큰 단위의 대시보드 설명",example = "대시보드 설명")
        private String description;

        @Builder
        public DashboardInfo(Dashboard dashboard){
            this.title = dashboard.getTitle();
            this.description = dashboard.getDescription();
        }
        @Builder
        public DashboardInfo(String title, String description){
            this.title = title;
            this.description = description;
        }
    }

    @Getter
    @Setter
    public static class DashboardDataInfo {
        @Schema(description = "시간당 관심을 표현한 사람 수 (0시 - 12시 까지)", example = "[20, 31, 50, 20, 30, 6, 20, 31, 50, 20, 30, 6]")
        private List<Long> hourlyInterestedCount;

        @Schema(description = "시간당 포착된 사람 수 (0시 - 12시 까지)", example = "[15, 28, 45, 20, 25, 7, 19, 30, 48, 18, 27, 5]")
        private List<Long> hourlyPassedCount;

        @Schema(description = "전체 포착된 사람 수", example = "254")
        private Long totalPeopleCount;

        @Schema(description = "해당 광고 평균 시청 시간", example = "3.1")
        private float avgStaringTime;

        @Schema(description = "해당 광고 평균 시청 나이 (0.0 ~ F )", example = "27")
        private float avgAge;

        @Schema(description = "관심을 표현한 남자의 인원 수", example = "150")
        private Long maleInterestCnt;

        @Schema(description = "관심을 표현한 여자의 인원 수", example = "104")
        private Long femaleInterestCnt;

        @Schema(description = "집계된 남자의 인원 수", example = "200")
        private Long maleCnt;

        @Builder
        public DashboardDataInfo() {
            this.hourlyInterestedCount = Collections.emptyList();
            this.hourlyPassedCount = Collections.emptyList();
            this.totalPeopleCount = 0L;
            this.avgStaringTime = 0.0f;
            this.avgAge = 0.0f;
            this.maleInterestCnt = 0L;
            this.femaleInterestCnt = 0L;
            this.maleCnt = 0L;
        }
    }
    @Getter
    @Setter
    @Builder
    public static class RegisteredMediaAppInfo {
        @Schema(description = "광고 집행 위치", example = "미래관 4층 디스플레이")
        private String address;

        @Schema(description = "thanks ")
        private Long str;
    }
}

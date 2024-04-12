package com.drm.server.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Valid
public class MediaRequest {
    @Getter
    @Setter
    @Builder
    public static class Create{
        @Schema(description = "대시보드 제목",example = "이거슨 대시보드 제목")
        private String dashboardTitle;
        @Schema(description = "대시보드 설명",example = "이거슨 대시보드 설명")
        private String dashboardDescription;
        @Schema(description = "광고 제목",example = "이거슨 광고 제목")
        private String advertisementTitle;
        @Schema(description = "광고 설명",example = "이거슨 광고 설명")
        private String advertisementDescription;
        @Positive(message = "장소id는 양수여야 합니다")
        private Long locationId;
        @Schema(description = "시작날짜",example = "2024-04-03")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형태 yyyy-MM-dd ")
        private String startDate;
        @Schema(description = "마감날짜",example = "2024-04-05")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형태 yyyy-MM-dd ")
        private String endDate;


    }
}

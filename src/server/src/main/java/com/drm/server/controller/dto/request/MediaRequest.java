package com.drm.server.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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
        @Positive(message = "장소id는 양수여야 합니다")
        private Long locationId;
        @Schema(name = "시작날짜",example = "2024-04-03")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형태 yyyy-MM-dd ")
        private String startDate;
        @Schema(name = "마감날짜",example = "2024-04-05")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형태 yyyy-MM-dd ")
        private String endDate;


    }
}

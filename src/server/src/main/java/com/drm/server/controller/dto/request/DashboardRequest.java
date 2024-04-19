package com.drm.server.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Valid
public class DashboardRequest {
    @Getter
    @Setter
    public static class dataPerDay{
        @Schema(description = "일별 데이터 조회",example = "2024-04-03")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형태 yyyy-MM-dd ")
        private String date;
        public dataPerDay(){
        }
    }
}

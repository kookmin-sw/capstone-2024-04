package com.drm.server.controller.dto.request;

import com.drm.server.domain.mediaApplication.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Valid
public class ApplyRequest {
    @Builder
    @Getter
    @Setter
    public static class Create{
        @Schema(description = "디스플레이 위치 id")
        private Long locationId;
        @Schema(description = "시작날짜",example = "2024-04-03")
        private String startDate;
        @Schema(description = "마감날짜",example = "2024-04-05")
        private String endDate;
    }
    @Getter
    @Setter
    public static class UpdateStatus{
        @Schema(description = "광고 신청 Id 리스트", example = "[1,4,5]")
        private List<Long> applyId;
        @Schema(description = "신청 상태",example = "ACCEPT")
        private Status status;
    }

    @Getter
    @Setter
    public static class MediaApplicationList{
        @Schema(description = "광고 신청 Id 리스트", example = "[1,4,5]")
        private List<Long> applyId;
    }

}

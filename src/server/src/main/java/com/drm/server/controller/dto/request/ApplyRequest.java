package com.drm.server.controller.dto.request;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Valid
public class ApplyRequest {
    @Builder
    @Getter
    @Setter
    public static class Create{

        private Long locationId;
        private String startDate;
        private String endDate;
    }
}

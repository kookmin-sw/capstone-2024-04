package com.drm.server.controller.dto.request;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Valid
public class MediaRequest {
    @Getter
    @Setter
    public static class Create{
        private Long dashBoardId;
        private String title;

    }
}

package com.drm.server.controller.dto.request;

import jakarta.validation.Valid;

@Valid
public class MediaRequest {
    public static class Create{
        private Long dashBoardId;
        private String title;

    }
}

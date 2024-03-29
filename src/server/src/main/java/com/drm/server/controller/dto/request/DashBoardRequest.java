package com.drm.server.controller.dto.request;

import jakarta.validation.Valid;

@Valid
public class DashBoardRequest {
    public static class Create{
        private String title;
        private String discription;
    }
}

package com.drm.server.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserResponse {
    @Builder
    @Getter
    @Setter
    public static class UserInfo{
        private Long userId;
        private String email;

        public UserInfo(Long userId, String email) {
            this.userId = userId;
            this.email = email;
        }
    }

}

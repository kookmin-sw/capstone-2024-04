package com.drm.server.controller.dto.response;

import com.drm.server.common.enums.Authority;
import com.drm.server.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserResponse {

    @Getter
    @Setter
    @Schema(description = "유저 정보")
    public static class UserInfo{
        @Schema(description = "유저 pk",example = "1")
        private Long userId;
        @Schema(description = "이메일",example = "sdkf@gmai.com")
        private String email;
        @Schema(description = "회사",example = "(주)국민KM")
        private String company;
        private String profileImage;

        public UserInfo(User user) {
            this.userId = user.getUserId();
            this.email = user.getEmail();
            this.company = user.getCompany();
            this.profileImage = user.getProfileImage();
        }
    }

    @Builder
    @Setter
    @Getter
    @AllArgsConstructor
    @Schema(description = "토큰 정보")
    public static class TokenInfo {
        private Long userId;
        @Schema(description = "토큰 타입" ,example ="Bearer" )
        private String grantType;
        @Schema(description = "엑세스 토큰")
        private String accessToken;
        @Schema(description = "리프레시 토큰")
        private String refreshToken;
        @Schema(description = "토큰 만료 시간")
        private Long refreshTokenExpirationTime;
        @Schema(description = "권한",example = "ADMIN, USER")
        private Authority authority;
        private UserInfo userInfo;
    }
    public static TokenInfo toTokenInfo(String BEARER_TYPE, Long userId, String accessToken, String refreshToken, Long REFRESH_TOKEN_EXPIRE_TIME, Authority authority){

        return TokenInfo.builder()
                .grantType(BEARER_TYPE)
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .authority(authority)
                .build();
    }
}

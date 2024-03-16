package com.drm.server.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
@Valid
public class UserRequest {
    @Getter
    @Setter
    @Schema(description = "인증 번호 전송을 위한 이메일 요청")
    public static class EmailRequest {
        @NotEmpty(message = "이메일을 입력해주세요")
        @Email
        @Schema(description = "이메일", nullable = false, example = "abc@gmail.com")
        private String email;
    }
    @Getter
    @Setter
    public static class AuthRequest {
        @NotEmpty(message = "이메일을 입력해주세요")
        @Email
        @Schema(description = "이메일", nullable = false, example = "abc@gmail.com")
        private String email;
        @NotEmpty(message = "인증번호를 입력해주세요")
        @Schema(description = "인증번호", nullable = false, example = "010716")
        private String authCode;
    }
}

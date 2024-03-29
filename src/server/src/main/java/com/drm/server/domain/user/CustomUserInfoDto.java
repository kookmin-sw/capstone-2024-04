package com.drm.server.domain.user;

import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomUserInfoDto {
    Long userId;
    String email;
    String password;
    boolean deleted;

}

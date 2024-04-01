package com.drm.server.domain.user;

import com.drm.server.common.enums.Authority;
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
    Authority authority;

}

package com.drm.server.controller;

import com.drm.server.controller.dto.request.MediaRequest;
import com.drm.server.domain.user.CustomUserDetails;
import com.drm.server.domain.user.User;
import com.drm.server.service.MediaService;
import com.drm.server.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/dashboard/media")
@RequiredArgsConstructor
@Tag(name = "Media",description = "광고 등록 관련 api")
public class MediaController {
    private final UserService userService;
    private final MediaService mediaService;
    @PostMapping(path = "",consumes =  {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createMedia(@RequestPart(value = "request") @Valid MediaRequest.Create create, @AuthenticationPrincipal CustomUserDetails userDetails){
        User getUser =userService.getUser(userDetails.getUsername());
        mediaService.createMedia(create, getUser);
    }
}

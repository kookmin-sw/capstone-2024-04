package com.drm.server.controller;

import com.drm.server.controller.dto.request.ApplyRequest;
import com.drm.server.domain.media.Media;
import com.drm.server.service.MediaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard/media/")
@Tag(name = "Apply")
@RequiredArgsConstructor
public class ApplyController {
    private final MediaService mediaService;
    @PostMapping("{mediaId}/apply")
    public ResponseEntity<?> apply (@PathVariable Long mediaId, @RequestBody ApplyRequest.Create request){
    }
}


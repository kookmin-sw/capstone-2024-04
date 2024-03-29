package com.drm.server.service;

import com.drm.server.controller.dto.request.MediaRequest;
import com.drm.server.controller.dto.request.ModelRequest;
import com.drm.server.domain.dashboard.DashboardRepository;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.media.MediaRepository;
import com.drm.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final DashboardRepository dashboardRepository;
    private final MediaRepository mediaRepository;
    public void updateMediaData(Long mediaId, boolean interest){

    }

    public Long getMediaIdFromPlaylist(Long cameraId){
        return 0L;
    }

    public void createMedia(MediaRequest.Create create, User getUser) {
        
    }
    public Media findById(Long mediaId){
        return mediaRepository.findById(mediaId).orElseThrow(() -> new IllegalArgumentException("Invalid mediaId"));
    }
}

package com.drm.server.service;

import com.drm.server.domain.location.Location;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.mediaApplication.MediaApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MediaApplicationService {
    private final MediaApplicationRepository mediaApplicationRepository;

    public MediaApplication createMediaApplication(Media media, Location location,String startDate,String endDate){
        MediaApplication mediaApplication = MediaApplication.toEntity(startDate, endDate, media, location);
        return mediaApplicationRepository.save(mediaApplication);
    }
}

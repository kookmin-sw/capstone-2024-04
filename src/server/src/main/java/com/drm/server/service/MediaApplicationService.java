package com.drm.server.service;

import com.drm.server.domain.location.Location;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.mediaApplication.MediaApplicationRepository;
import com.drm.server.domain.mediaApplication.Status;
import com.drm.server.domain.user.User;
import com.drm.server.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaApplicationService {
    private final MediaApplicationRepository mediaApplicationRepository;

    public MediaApplication createMediaApplication(Media media, Location location,String startDate,String endDate){
        MediaApplication mediaApplication = MediaApplication.toEntity(startDate, endDate, media, location);
        return mediaApplicationRepository.save(mediaApplication);
    }
    public void deleteMediaApplication(Long mediaId, Long mediaApplicationId, User user){
        MediaApplication mediaApplication = findById( mediaApplicationId);
        if(mediaApplication.getMedia().getMediaId() != mediaId) throw new IllegalArgumentException("mediaId가 잘못되었습니다");
        verifyApplication(mediaApplication, user);
        mediaApplicationRepository.delete(mediaApplication);
    }

    public MediaApplication updateStatus(Long mediaApplicationId, Status status) {
        MediaApplication mediaApplication =findById(mediaApplicationId);
        boolean available = mediaApplicationRepository.hasOverlappingApplications(mediaApplication.getStartDate(), mediaApplication.getEndDate(),mediaApplication.getLocation());
        if(available) throw new IllegalArgumentException("해당 날짜와 장소에는 이미 광고가 등록되어있습니다");
        mediaApplication.updateStatus(status);
        return mediaApplicationRepository.save(mediaApplication);
    }
    public MediaApplication findById(Long mediaApplicationId){
        MediaApplication mediaApplication = mediaApplicationRepository.findById(mediaApplicationId).orElseThrow(() -> new IllegalArgumentException("Invalid applicationId"));
        return mediaApplication;
    }
    public void verifyApplication (MediaApplication mediaApplication,User user){
        if(mediaApplication.getMedia().getDashboard().getUser()!=user)
            throw new ForbiddenException("해당 신청에 접근 권한이 없습니다");
        if(mediaApplication.getStatus() != Status.WAITING) throw new ForbiddenException("신청 대기일때만 삭제 가능합니다");
    }
    public List<MediaApplication> findAllApplications(){
        List<MediaApplication> mediaApplications = mediaApplicationRepository.findAll();
        return mediaApplications;
    }

    public List<MediaApplication> findByMedia(Media getMedia) {
        List<MediaApplication> mediaApplications = mediaApplicationRepository.findByMedia(getMedia).orElse(Collections.emptyList());
        return mediaApplications;
    }
}

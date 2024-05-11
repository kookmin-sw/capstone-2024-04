package com.drm.server.service;

import com.drm.server.common.FileDto;
import com.drm.server.controller.dto.request.MediaRequest;
import com.drm.server.controller.dto.request.ModelRequest;
import com.drm.server.controller.dto.response.MediaResponse;
import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.dashboard.DashboardRepository;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.media.MediaRepository;
import com.drm.server.domain.mediaApplication.MediaApplicationRepository;
import com.drm.server.domain.mediaApplication.Status;
import com.drm.server.domain.user.User;
import com.drm.server.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.ion.NullValueException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaService {
    private final MediaRepository mediaRepository;
    private final FileService fileService;
    private final MediaApplicationRepository mediaApplicationRepository;


    @Transactional
    public Media createMedia(MediaRequest.Create create, Dashboard dashboard, MultipartFile multipartFile) {
        FileDto fileDto = fileService.uploadFile("image",multipartFile);
        Media media = Media.toEntity(create, fileDto.getUploadFileName(),fileDto.getUploadFileUrl(), dashboard);
       return mediaRepository.save(media);
    }

    @Transactional
    public Media createMockMedia(MediaRequest.Create create, Dashboard dashboard, MultipartFile multipartFile){
        FileDto fileDto = null;
        Media media = Media.toEntity(create, "fakefile.png", "C://fake_d", dashboard);
        return mediaRepository.save(media);
    }

    public List<MediaResponse.MediaInfo> findByDashboard(List<Dashboard> dashboards, Status status, Pageable pageable){
        if (status == null ){
            return dashboards.stream().map(MediaResponse.MediaInfo::new).collect(Collectors.toList());
        }
        List<Media> acceptHasDatatMediaList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        dashboards.forEach(dashboard -> {
            if(mediaApplicationRepository.existsByMediaAndStartDateBeforeAndStatusIsNullOrStatus(dashboard.getMedia(),today,status)){
                acceptHasDatatMediaList.add(dashboard.getMedia());

            }
        });
        return acceptHasDatatMediaList.stream().map(MediaResponse.MediaInfo::new).collect(Collectors.toList());
    }

    public Media findOneMediaByDashboard(Dashboard dashboard){
        Media media = mediaRepository.findByDashboard(dashboard).orElseThrow(() ->
                new NullValueException("MEDIA NOT EXISTS (SEARCHED BY DASHBOARD " + dashboard.getDashboardId() + ")"));
        return media;
    }
    public Media findById(Long mediaId,User user){
        Media media =mediaRepository.findById(mediaId).orElseThrow(() -> new IllegalArgumentException("Invalid mediaId"));
        if(media.getDashboard().getUser()!=user) throw new ForbiddenException("해당 유저가 등록한 광고가 아닙니다");
        return media;
    }

    public void deleteAll() {
        mediaRepository.deleteAll();
    }
}

package com.drm.server.service;

import com.drm.server.common.FileDto;
import com.drm.server.controller.dto.request.MediaRequest;
import com.drm.server.controller.dto.request.ModelRequest;
import com.drm.server.controller.dto.response.MediaResponse;
import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.dashboard.DashboardRepository;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.media.MediaRepository;
import com.drm.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final MediaRepository mediaRepository;
    private final FileService fileService;
    public void updateMediaData(Long mediaId, boolean interest){

    }

    public Long getMediaIdFromPlaylist(Long cameraId){
        return 0L;
    }
    @Transactional
    public Media createMedia(MediaRequest.Create create, Dashboard dashboard, MultipartFile multipartFile) {
        FileDto fileDto = fileService.uploadFile(multipartFile);
        Media media = Media.toEntity(create, fileDto.getUploadFileName(),fileDto.getUploadFileUrl(), dashboard);
       return mediaRepository.save(media);
    }
    public List<MediaResponse.MediaInfo> findByDashboard(List<Dashboard> dashboards){
        List<MediaResponse.MediaInfo> mediaResponses = dashboards.stream().map(MediaResponse.MediaInfo::new).collect(Collectors.toList());
        return mediaResponses;
    }
    public Media findById(Long mediaId){
        return mediaRepository.findById(mediaId).orElseThrow(() -> new IllegalArgumentException("Invalid mediaId"));
    }
}

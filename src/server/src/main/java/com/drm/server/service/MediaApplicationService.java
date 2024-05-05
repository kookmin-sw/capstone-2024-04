package com.drm.server.service;

import com.drm.server.common.KoreaLocalDateTime;
import com.drm.server.controller.dto.response.MediaApplicationResponse;
import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.location.Location;
import com.drm.server.domain.location.LocationRepository;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.mediaApplication.MediaApplicationRepository;
import com.drm.server.domain.mediaApplication.Status;
import com.drm.server.domain.playlist.PlayListRepository;
import com.drm.server.domain.user.User;
import com.drm.server.exception.ForbiddenException;
import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.ion.NullValueException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.drm.server.domain.mediaApplication.Status.WAITING;

@Service
@RequiredArgsConstructor
public class MediaApplicationService {
    private final MediaApplicationRepository mediaApplicationRepository;
    private final LocationRepository locationRepository;
    private final PlayListRepository playListRepository;
    private final DailyMediaBoardService dailyMediaBoardService;

    public MediaApplication createMediaApplication(Media media, Location location, String startDate,String endDate){
        if(KoreaLocalDateTime.stringToLocalDateTime(startDate).isAfter(KoreaLocalDateTime.stringToLocalDateTime(endDate) ))throw new IllegalArgumentException("시작 시간이 끝나는 시간 보다 늦을수 없습니다");
        MediaApplication mediaApplication = MediaApplication.toEntity(startDate, endDate, media, location);
        return mediaApplicationRepository.save(mediaApplication);
    }
    public void deleteMediaApplication(Long mediaId, Long mediaApplicationId, User user){
        MediaApplication mediaApplication = findById( mediaApplicationId);
        verifyMedia(mediaApplication, mediaId);
        deleteVerify(mediaApplication, user);
        mediaApplicationRepository.delete(mediaApplication);
    }

    public List<MediaApplicationResponse.MediaApplicationInfo> updateStatus(List<Long> mediaApplicationIds, Status status) {

        List<MediaApplicationResponse.MediaApplicationInfo> infos = new ArrayList<>();
        mediaApplicationIds.forEach(applyid -> {
            MediaApplication mediaApplication = mediaApplicationRepository.findById(applyid).orElseThrow(() -> new IllegalArgumentException("Invalid applyId"));
            boolean available = mediaApplicationRepository.hasOverlappingApplications(mediaApplication.getStartDate(), mediaApplication.getEndDate(),mediaApplication.getLocation());
            if(available) throw new IllegalArgumentException("해당 날짜와 장소에는 이미 광고가 등록되어있습니다");
            mediaApplication.updateStatus(status);
            mediaApplicationRepository.save(mediaApplication);
            MediaApplicationResponse.MediaApplicationInfo mediaApplicationInfo = new MediaApplicationResponse.MediaApplicationInfo(mediaApplication);
            infos.add(mediaApplicationInfo);
        });
        return infos;

    }
    public MediaApplication findById(Long mediaApplicationId){
        MediaApplication mediaApplication = mediaApplicationRepository.findById(mediaApplicationId).orElseThrow(() -> new IllegalArgumentException("Invalid applicationId"));
        return mediaApplication;
    }

    public List<MediaApplication> findAllApplications(){
        List<MediaApplication> mediaApplications = mediaApplicationRepository.findAll();
        return mediaApplications;
    }

    public List<MediaApplication> findByMedia(Media getMedia) {
        List<MediaApplication> mediaApplications = mediaApplicationRepository.findByMedia(getMedia).orElse(Collections.emptyList());
        return mediaApplications;
    }

    /**
     * 모델에서 카메라번호를 입력받으면 해당 카메라와 패핑되는 장소 조회, 장소와 날짜 데이터로 playlist 조회 playlist에 mediaApplication 조회
     * @param cameraId
     * @param today
     * @return
     */
    public MediaApplication findByCameraIdAndDate(int cameraId, LocalDateTime today){
        Location getLocation = locationRepository.findByCameraId(cameraId).orElseThrow(() -> new IllegalArgumentException("Invalid cameraId"));
        MediaApplication mediaApplication = playListRepository.findMediaApplicationsByLocationAndCreateDate(getLocation, today).orElseThrow(() -> new IllegalArgumentException("광고가 걸려있지 않습니다"));

        return mediaApplication;

    }
    public void verifyMedia(MediaApplication mediaApplication, Long mediaId ){
        if(mediaApplication.getMedia().getMediaId() != mediaId) throw new IllegalArgumentException("mediaId가 잘못되었습니다");
    }
    public void verifyUser(MediaApplication mediaApplication, User user){
        if(!mediaApplication.getMedia().getDashboard().getUser().equals(user)) throw new ForbiddenException("해당 신청에 접근 권한이 없습니다");
    }

    public void deleteVerify(MediaApplication mediaApplication, User user){
        verifyUser(mediaApplication,user);
        // media_application user 권한 검증 메소드의 재사용을 위해 아래의 라인 수정함
        // ACCEPT 인 경우에 아래의 throw acception 을 뱉지 않도록 수정하는 것이 verify 의 목적에 맞다고 판단함. -
        // > accept의 경우 삭제되면 안되기때문에 해당 verify는 삭제방지용으로 했던것 , accept가 된 상태에서 Return해버리면 accept가 된 신청목록 삭제됨
        if(!mediaApplication.getStatus().equals(WAITING)) throw new ForbiddenException("신청 대기일때만 삭제 가능합니다");
    }

    public List<MediaApplication> findMediaAppsByLocation(Location location) {
       return mediaApplicationRepository.findAllByLocation(location).orElseThrow(() -> new NullValueException());
    }

    public void deleteAll() {
        mediaApplicationRepository.deleteAll();
    }

    public List<MediaApplication> findByDashBoards(List<Dashboard> dashboards,Status status) {
        List<Media> mediaList = new ArrayList<>();
        dashboards.forEach(dashboard -> {
            mediaList.add(dashboard.getMedia());
        });
        if(status == null ) {
            return mediaApplicationRepository.findByMediaInOrderByCreateDateDesc(mediaList).orElse(Collections.emptyList());
        }
        LocalDate currentDate = LocalDate.now();
        return mediaApplicationRepository.findByMediaInAndDashboardData(mediaList,currentDate,status).orElse(Collections.emptyList());
    }
}

package com.drm.server.service;

import com.drm.server.domain.dailyMediaBoard.DailyMediaBoard;
import com.drm.server.domain.location.Location;
import com.drm.server.domain.location.LocationRepository;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.mediaApplication.MediaApplicationRepository;
import com.drm.server.domain.playlist.PlayList;
import com.drm.server.domain.playlist.PlayListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayListService {
    private final MediaApplicationRepository mediaApplicationRepository;
    private final PlayListRepository playListRepository;
    private final LocationRepository locationRepository;

    private final DailyMediaBoardService dailyMediaBoardService;
    private final DailyDetailBoardService dailyDetailBoardService;

    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "0/5 * * * * ?")
    public List<PlayList> updatePlayList(){
        LocalDate today = LocalDate.now();
//        오늘 날짜의 신청리스트 조회
        List<MediaApplication> mediaApplications = mediaApplicationRepository.findAcceptedApplicationsForToday( today).orElse(Collections.emptyList());
//        추가 안된 광고 추가
        List<MediaApplication> unUploadPlayList = unUploadPlayList(today,mediaApplications);
        List<PlayList> playLists = unUploadPlayList.stream()
                .peek(mediaApplication ->{
                    DailyMediaBoard dailyMediaBoard =  dailyMediaBoardService.createDailyBoard(mediaApplication,today);
                    dailyDetailBoardService.createDetailBoard(dailyMediaBoard);
                })
                .map(PlayList::new).collect(Collectors.toList());
        List<PlayList> newPlayList = playListRepository.saveAll(playLists);
        updateBroadCasting();
        return newPlayList;
    }
    public List<PlayList> todayList(Long locationId){
        Location location = locationRepository.findById(locationId).orElseThrow(() -> new IllegalArgumentException("invalid locationId"));
        LocalDate currentDate = LocalDate.now();
        return playListRepository.findByLocationAndCreateDateContaining(location,currentDate.atStartOfDay()).orElse(Collections.emptyList());
    }

    private List<MediaApplication> unUploadPlayList(LocalDate localDate, List<MediaApplication> mediaApplications){
        List<MediaApplication> unUploadPlayList = new ArrayList<>();
        mediaApplications.forEach(mediaApplication -> {
            if(!playListRepository.existsByCreateDateAndMediaApplications(localDate.atStartOfDay(),mediaApplication)){
                unUploadPlayList.add(mediaApplication);
            }
        });
        // currentDate 에 해당되는 MediaAplication 의 daily Board 들 각각 생성
        return unUploadPlayList;

    }

    public MediaApplication getMediaApplicationFromPlaylist(Long locationId, LocalDateTime time){
        // 해당 location(camera) 에서 해당 일-분-초에 틀어지고 있는 mediaAplication 반환
        // 구현 필요
        MediaApplication mediaApp = mediaApplicationRepository.findFirstByOrderByCreateDateDesc();;
        return mediaApp;
    }

    public MediaApplication getMediaApplicationFromPlaylistId(Long playListId){
        PlayList playList = playListRepository.findById(playListId).get();
        MediaApplication mediaApplication = playList.getMediaApplication();
        return mediaApplication;
    }

    // test 코드용 호출
    public void testUpdatePlayList(){
        updatePlayList();
    }
    public List<PlayList> updateBroadCasting(){
        LocalDate currentDate = LocalDate.now();
        List<Location> locations = locationRepository.findAll();
        locations.forEach(location -> {
//            위치, 오늘날짜의 플레이리스트면서 실행중인 개수
            int broadCastingCount = playListRepository.existsByLocationAndPostingIsTrue(location, currentDate.atStartOfDay());
//            위치, 오늘날짜의 플레이리스트들 조회
            List<PlayList> playLists = playListRepository.findByLocationAndCreateDateContaining(location, currentDate.atStartOfDay()).orElse(Collections.emptyList());
            log.info("{} : {}  장소 광고 실행 여부 =  {}개 송출중",location.getLocationId(),location.getAddress(),broadCastingCount);
//            한 장소에 실행중인 광고가 하나 이상인 경우 전부 초기화 시키고 제일 먼저 있는 것만 true
            if(broadCastingCount > 1) {
                verifySingleBroadCast(playLists);
//            실행중인 플레이리스트도 없으면서 플레이리스타 존재하는 경우
            }else if(broadCastingCount == 0 && playLists.size() != 0) {
                PlayList unBroadCasting = playLists.get(0);
                unBroadCasting.brodCasting();
                playListRepository.save(unBroadCasting);
            }
        });
        return playListRepository.findByBroadCasting(currentDate.atStartOfDay());
    }
    public void verifySingleBroadCast(List<PlayList> todayList){
            todayList.forEach(playList -> playList.unBroadCasting());
            todayList.get(0).brodCasting();
            playListRepository.saveAll(todayList);


    }
}

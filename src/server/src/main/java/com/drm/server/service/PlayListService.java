package com.drm.server.service;

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

    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "0/5 * * * * ?")
    public List<PlayList> updatePlayList(){
        LocalDate currentDate = LocalDate.now();
//        오늘 날짜의 신청리스트 조회
        List<MediaApplication> mediaApplications = mediaApplicationRepository.findAcceptedApplicationsForToday( currentDate).orElse(Collections.emptyList());
//        추가 안된 광고 추가
        List<MediaApplication> unUploadPlayList = unUploadPlayList(currentDate,mediaApplications);
        List<PlayList> playLists = unUploadPlayList.stream()
                .peek(mediaApplication -> dailyMediaBoardService.createDailyBoard(mediaApplication,currentDate))
                .map(PlayList::new).collect(Collectors.toList());
        List<PlayList> newPlayList = playListRepository.saveAll(playLists);
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

    public MediaApplication getMediaAplicationFromPlaylist(Long locationId, LocalDateTime time){
        // 해당 location(camera) 에서 해당 일-분-초에 틀어지고 있는 mediaAplication 반환
        // 구현 필요
        MediaApplication mediaApp = mediaApplicationRepository.findFirstByOrderByCreateDateDesc();;
        return mediaApp;
    }

    // test 코드용 호출
    public void testUpdatePlayList(){
        updatePlayList();
    }
    public List<PlayList> updateBroadCasting(){
        LocalDate currentDate = LocalDate.now();
        List<Location> locations = locationRepository.findAll();
        locations.forEach(location -> {
//            플레이리스트가 실행중인게 없을 경우
            boolean isposting = playListRepository.existsByLocationAndPostingIsTrue(location, currentDate.atStartOfDay());
            List<PlayList> playLists = playListRepository.findFirstFalseByLocationAndCreateDateOrderByCreateDateAsc(location, currentDate.atStartOfDay()).orElse(Collections.emptyList());
            log.info(String.valueOf(isposting));
//            실행중인 플레이리스트도 없으면서 플레이리스타 존재하는 경우
            if(!isposting && playLists.size() != 0) {

                PlayList unBroadCasting = playLists.get(0);
                unBroadCasting.brodcasting();
                playListRepository.save(unBroadCasting);
            }
        });
        return playListRepository.findByBroadCasting(currentDate.atStartOfDay());
    }
}

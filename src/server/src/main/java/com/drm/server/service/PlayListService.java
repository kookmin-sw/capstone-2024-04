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
        List<MediaApplication> mediaApplications = mediaApplicationRepository.findAcceptedApplicationsForToday( currentDate).orElse(Collections.emptyList());
        log.info(String.valueOf(mediaApplications.stream().count()));
        List<MediaApplication> unUploadPlayList = verifyPlayList(currentDate,mediaApplications);
        List<PlayList> playLists = unUploadPlayList.stream()
                .peek(mediaApplication -> dailyMediaBoardService.createDailyBoard(mediaApplication,currentDate))
                .map(PlayList::new).collect(Collectors.toList());
        return playListRepository.saveAll(playLists);
    }
    public List<PlayList> todayList(Long locationId){
        Location location = locationRepository.findById(locationId).orElseThrow(() -> new IllegalArgumentException("invalid locationId"));
        LocalDate currentDate = LocalDate.now();
        return playListRepository.findByLocationAndCreateDateContaining(location,currentDate.atStartOfDay()).orElse(Collections.emptyList());
    }

    private List<MediaApplication> verifyPlayList(LocalDate localDate, List<MediaApplication> mediaApplications){
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
}

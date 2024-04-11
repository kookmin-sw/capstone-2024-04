package com.drm.server.service;

import com.drm.server.domain.location.Location;
import com.drm.server.domain.location.LocationRepository;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.mediaApplication.MediaApplicationRepository;
import com.drm.server.domain.playlist.PlayList;
import com.drm.server.domain.playlist.PlayListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.processing.Find;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "0/5 * * * * ?")
    public List<PlayList> updatePlayList(){
        LocalDate currentDate = LocalDate.now();
        List<MediaApplication> mediaApplications = mediaApplicationRepository.findAcceptedApplicationsForToday( currentDate).orElse(Collections.emptyList());
        log.info(String.valueOf(mediaApplications.stream().count()));
        verifyPlayList(currentDate,mediaApplications);
        List<PlayList> playLists = mediaApplications.stream().map(PlayList::new).collect(Collectors.toList());
        return playListRepository.saveAll(playLists);
    }
    public List<PlayList> todayList(Long locationId){
        Location location = locationRepository.findById(locationId).orElseThrow(() -> new IllegalArgumentException("invalid locationId"));
        LocalDate currentDate = LocalDate.now();
        return playListRepository.findByLocationAndCreateDateContaining(location,currentDate.atStartOfDay()).orElse(Collections.emptyList());
    }

    private void verifyPlayList(LocalDate localDate, List<MediaApplication> mediaApplications){
        mediaApplications.forEach(mediaApplication -> {
            if(playListRepository.existsByCreateDateAndMediaApplications(localDate.atStartOfDay(),mediaApplication))
                throw new IllegalArgumentException("해당 플레이리스트는 이미 등록되어있습니다");
        });
    }
}

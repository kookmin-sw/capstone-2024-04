package com.drm.server.service;

import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.mediaApplication.MediaApplicationRepository;
import com.drm.server.domain.playlist.PlayList;
import com.drm.server.domain.playlist.PlayListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayListService {
    private final MediaApplicationRepository mediaApplicationRepository;
    private final PlayListRepository playListRepository;

    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "0/5 * * * * ?")
    private void updatePlayList(){
        LocalDate currentDate = LocalDate.now();
        List<MediaApplication> mediaApplications = mediaApplicationRepository.findAcceptedApplicationsForToday( currentDate).orElse(Collections.emptyList());
        log.info(String.valueOf(mediaApplications.stream().count()));
        List<PlayList> playLists = mediaApplications.stream().map(PlayList::new).collect(Collectors.toList());
        playListRepository.saveAll(playLists);
    }
}

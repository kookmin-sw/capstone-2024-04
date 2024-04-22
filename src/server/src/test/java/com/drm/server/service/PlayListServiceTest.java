package com.drm.server.service;

import com.drm.server.domain.mediaApplication.MediaApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class PlayListServiceTest {
    private final PlayListService playListService;

    @Autowired
    public PlayListServiceTest(PlayListService playListService){
        this.playListService = playListService;
    }
    @Test
    public void playListGetTest(){
        LocalDateTime time = LocalDateTime.now();
        Long id = 0L;

        MediaApplication mediaApp = playListService.getMediaAplicationFromPlaylist(id, time);

    }

    @Test
    public void updateTest(){
        playListService.testUpdatePlayList();
    }
}

package com.drm.server.service;

import com.drm.server.domain.dashboard.DashboardRepository;
import com.drm.server.domain.location.Location;
import com.drm.server.domain.mediaApplication.MediaApplication;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MediaApplicationServiceTest {
    private final DashboardService dashboardService;
    private final DetectedDataService detectedDataService;

    private final DailyMediaBoardService dailyMediaBoardService;
    private final PlayListService playListService;
    private final LocationService locationService;
    private final MediaApplicationService mediaApplicationService;
    private final DashboardRepository dashboardRepository;
    private final UserService userService;
    private final MediaService mediaService;

    @Autowired
    public MediaApplicationServiceTest(DashboardService dashboardService, PlayListService playListService, DetectedDataService detectedDataService, DailyMediaBoardService dailyMediaBoardService, MediaApplicationService mediaApplicationService, LocationService locationService, UserService userService, MediaService mediaService, DashboardRepository dashboardRepository){
        this.dailyMediaBoardService = dailyMediaBoardService;
        this.locationService = locationService;
        this.playListService = playListService;
        this.mediaApplicationService = mediaApplicationService;
        this.dashboardService = dashboardService;
        this.detectedDataService = detectedDataService;
        this.userService = userService;
        this.dashboardRepository = dashboardRepository;
        this.mediaService = mediaService;
    }

    @Test
    public void getByMediaTest(){
        Location location = locationService.findById(1L);
        List<MediaApplication> mediaApplicationList = mediaApplicationService.findMediaAppsByLocation(location);

        assertEquals(mediaApplicationList.size(), 1);
    }
}

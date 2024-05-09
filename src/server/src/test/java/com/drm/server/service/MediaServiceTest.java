package com.drm.server.service;

import com.drm.server.controller.dto.request.MediaRequest;
import com.drm.server.controller.dto.response.UserResponse;
import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.dashboard.DashboardRepository;
import com.drm.server.domain.location.Location;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.mediaApplication.Status;
import com.drm.server.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class MediaServiceTest {
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
    public MediaServiceTest(DashboardService dashboardService, PlayListService playListService, DetectedDataService detectedDataService, DailyMediaBoardService dailyMediaBoardService, MediaApplicationService mediaApplicationService, LocationService locationService, UserService userService, MediaService mediaService, DashboardRepository dashboardRepository){
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
    @Transactional
//    @Transactional
    void updateMediaData() throws IOException {
        //        SetupManually();
        // given
        // 회원 가입 세팅
        String email = "test@email";
        Optional<User> userOptional = userService.getUserByEmail(email);
        User user = new User();
        if(userOptional.isEmpty()){
            UserResponse.UserInfo userInfo = userService.createUser(email, "1234", "drm");
            user = userService.getUser(userInfo.getUserId());
        }
        else{
            user = userOptional.get();
        }

        // 미디어 생성 요청 세팅
        MockMultipartFile file = new MockMultipartFile("image",
                "translate.png",
                "image/png",
                new FileInputStream("/Users/dongguk/Desktop/동국자료/해외여행/CES/translate.png"));

        locationService.createLocation(1, "2호선 1번 칸");
        locationService.createLocation(2, "2호선 2번 칸");
        locationService.createLocation(3, "2호선 3번 칸");

        Location location = locationService.findByCameraId(1);
        Location location2 = locationService.findByCameraId(2);
        Location location3 = locationService.findByCameraId(3);
//        String title = LocalDateTime.now() + " maxmax chicken board";

        String title = "maxmax bb chicken num1 board";
        LocalDate endDate = LocalDate.now().plusDays(2);
        String formatEndDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));        // 미디어 / 대시보드  -> 세팅 및 생성

        MediaRequest.Create mediaRequest = MediaRequest.Create.builder().advertisementDescription("this is awesome").advertisementTitle("Good Fish")
                .startDate("2024-04-07").endDate(formatEndDate).locationId(1L).build();

//        MediaRequest.Create mediaRequest2 = MediaRequest.Create.builder().advertisementDescription("No Way").advertisementTitle("Chagne to Rap Concert")
//                .startDate("2024-04-07").endDate(formatEndDate).locationId(1L).build();


        Dashboard ds = dashboardService.createDashboard(user);
        Dashboard ds2 = dashboardService.createDashboard(user);

        Media media =  mediaService.createMedia(mediaRequest, ds, file);
        log.info("CREATED MEDIA URL : " + media.getMediaLink());
//        Media media =  mediaService.createMockMedia(mediaRequest, ds, file);
//        Media media2 =  mediaService.createMockMedia(mediaRequest2, ds2, file);

        // 여러개의 광고 집행 단위가 존재 가정
        MediaApplication mediaApplication = mediaApplicationService.createMediaApplication(media, location ,mediaRequest.getStartDate(),mediaRequest.getEndDate());
//        MediaApplication mediaApplication2_sec = mediaApplicationService.createMediaApplication(media2, location ,mediaRequest.getStartDate(),mediaRequest.getEndDate());

        MediaApplication mediaApplication2 = mediaApplicationService.createMediaApplication(media, location2 ,mediaRequest.getStartDate(),mediaRequest.getEndDate());
        MediaApplication mediaApplication3 = mediaApplicationService.createMediaApplication(media, location3 ,mediaRequest.getStartDate(),mediaRequest.getEndDate());

        // 광고 승락
        List<MediaApplication> mediaApps = mediaApplicationService.findByMedia(media);
        List<Long> mediaAppIds = mediaApps.stream().map(mediaApplication1 -> mediaApplication1.getMediaApplicationId()).collect(Collectors.toList());

//        List<MediaApplication> mediaApps2 = mediaApplicationService.findByMedia(media2);
//        List<Long> mediaAppIds2 = mediaApps2.stream().map(mediaApplication1 -> mediaApplication1.getMediaApplicationId()).collect(Collectors.toList());

        mediaApplicationService.updateStatus(mediaAppIds, Status.ACCEPT);
//        mediaApplicationService.updateStatus(mediaAppIds2, Status.ACCEPT);

        playListService.testUpdatePlayList();
    }
}
package com.drm.server.service;

import com.drm.server.controller.dto.request.MediaRequest;
import com.drm.server.controller.dto.request.ModelRequest;
import com.drm.server.controller.dto.response.DashboardResponse;
import com.drm.server.controller.dto.response.UserResponse;
import com.drm.server.domain.dailyMediaBoard.DailyMediaBoard;
import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.dashboard.DashboardRepository;
import com.drm.server.domain.location.Location;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.mediaApplication.Status;
import com.drm.server.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
public class DashboardServiceTest {
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
    public DashboardServiceTest(DashboardService dashboardService, PlayListService playListService, DetectedDataService detectedDataService, DailyMediaBoardService dailyMediaBoardService, MediaApplicationService mediaApplicationService, LocationService locationService, UserService userService, MediaService mediaService, DashboardRepository dashboardRepository){
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

    // given
    // when
    // then

    public void SetupManually() throws IOException {
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

//        locationService.createLocation(1, "2호선 1번 칸");
//        locationService.createLocation(2, "2호선 2번 칸");
//        locationService.createLocation(3, "2호선 3번 칸");

        Location location = locationService.findById(1L);
        Location location2 = locationService.findById(2L);
        Location location3 = locationService.findById(3L);
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

    @AfterEach
    public void Teardown(){

    }

    @Test
//    @Disabled
    @Transactional
    // 특정 광고에 대한 요약 데이터 조회하는 테스트
    public void getBoardPerAdSummaryTest() throws IOException {
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

//        locationService.createLocation(1, "2호선 1번 칸");
//        locationService.createLocation(2, "2호선 2번 칸");
//        locationService.createLocation(3, "2호선 3번 칸");

        Location location = locationService.findById(1L);
        Location location2 = locationService.findById(2L);
        Location location3 = locationService.findById(3L);
//        String title = LocalDateTime.now() + " maxmax chicken board";

        String title = "maxmax bb chicken num1 board";
        LocalDate endDate = LocalDate.now().plusDays(2);
        String formatEndDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));        // 미디어 / 대시보드  -> 세팅 및 생성

        MediaRequest.Create mediaRequest = MediaRequest.Create.builder().advertisementDescription("this is awesome").advertisementTitle("Good Fish")
                .startDate("2024-04-07").endDate(formatEndDate).locationId(1L).build();

        Dashboard ds = dashboardService.createDashboard(user);

        Media media =  mediaService.createMedia(mediaRequest, ds, file);
        log.info("CREATED MEDIA URL : " + media.getMediaLink());

        // 여러개의 광고 집행 단위가 존재 가정
        MediaApplication mediaApplication = mediaApplicationService.createMediaApplication(media, location ,mediaRequest.getStartDate(),mediaRequest.getEndDate());
        MediaApplication mediaApplication2 = mediaApplicationService.createMediaApplication(media, location2 ,mediaRequest.getStartDate(),mediaRequest.getEndDate());
        MediaApplication mediaApplication3 = mediaApplicationService.createMediaApplication(media, location3 ,mediaRequest.getStartDate(),mediaRequest.getEndDate());

        // 광고 승락
        List<MediaApplication> mediaApps = mediaApplicationService.findByMedia(media);
        List<Long> mediaAppIds = mediaApps.stream().map(mediaApplication1 -> mediaApplication1.getMediaApplicationId()).collect(Collectors.toList());


        mediaApplicationService.updateStatus(mediaAppIds, Status.ACCEPT);

        playListService.testUpdatePlayList();

        DashboardResponse.DashboardDataInfo info = dashboardService.getDashboardWithDataById(user.getUserId(), ds.getDashboardId());
        assertEquals(3, info.getMediaAppsCnt());

    }

    @Test
//    @Disabled
    @Transactional
    // 특정 유저가 집행한 광고 대시보드 리스트 받아오는 테스트
    // 작성 완료
    public void getDashBoardListTest() throws IOException {
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

//        locationService.createLocation(1, "2호선 1번 칸");
//        locationService.createLocation(2, "2호선 2번 칸");
//        locationService.createLocation(3, "2호선 3번 칸");

        Location location = locationService.findById(1L);
        Location location2 = locationService.findById(2L);
        Location location3 = locationService.findById(3L);
//        String title = LocalDateTime.now() + " maxmax chicken board";

        String title = "maxmax bb chicken num1 board";
        LocalDate endDate = LocalDate.now().plusDays(2);
        String formatEndDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));        // 미디어 / 대시보드  -> 세팅 및 생성

        MediaRequest.Create mediaRequest = MediaRequest.Create.builder().advertisementDescription("this is awesome").advertisementTitle("Good Fish")
                .startDate("2024-04-07").endDate(formatEndDate).locationId(1L).build();
        MediaRequest.Create mediaRequest2 = MediaRequest.Create.builder().advertisementDescription("this is awesome 22").advertisementTitle("Good Fish 22")
                .startDate("2024-04-10").endDate(formatEndDate).locationId(2L).build();
        MediaRequest.Create mediaRequest3 = MediaRequest.Create.builder().advertisementDescription("this is awesome33").advertisementTitle("Good Fish 33")
                .startDate("2024-04-12").endDate(formatEndDate).locationId(3L).build();
        Dashboard ds = dashboardService.createDashboard(user);

        Media media =  mediaService.createMedia(mediaRequest, ds, file);
        log.info("CREATED MEDIA URL : " + media.getMediaLink());

        // 여러개의 광고 집행 단위가 존재 가정
        MediaApplication mediaApplication = mediaApplicationService.createMediaApplication(media, location ,mediaRequest.getStartDate(),mediaRequest.getEndDate());
        MediaApplication mediaApplication2 = mediaApplicationService.createMediaApplication(media, location2 ,mediaRequest.getStartDate(),mediaRequest.getEndDate());
        MediaApplication mediaApplication3 = mediaApplicationService.createMediaApplication(media, location3 ,mediaRequest.getStartDate(),mediaRequest.getEndDate());

        // 광고 승락
        List<MediaApplication> mediaApps = mediaApplicationService.findByMedia(media);
        List<Long> mediaAppIds = mediaApps.stream().map(mediaApplication1 -> mediaApplication1.getMediaApplicationId()).collect(Collectors.toList());


        mediaApplicationService.updateStatus(mediaAppIds, Status.ACCEPT);

        playListService.testUpdatePlayList();

        List<DashboardResponse.RegisteredMediaAppInfo> infos = dashboardService.getRegisteredBoardsById(user.getUserId(), ds.getDashboardId());
        assertEquals(infos.size(), 3);
        for(DashboardResponse.RegisteredMediaAppInfo info : infos){
            assertEquals(info.getEndDate(), formatEndDate);
        }


    }




    @Test
    @Disabled
    // 광고 + 광고 집행 단위 + 날짜 형태로 데이터 조회하는 테스트
    // 작성 완료
    public void getBoardPerDaysTest() throws IOException {
        // when
        // 해당 유저가 생성한 대시보드 조회
//        LocalDate date = LocalDate.now();
//        DailyMediaBoard board = dailyMediaBoardService.findDailyBoardByDateAndApplication(testApp, date);
//
//        // then
//        // 조회된 대시보드 값 확인
//        assertEquals(0L, board.getMaleCnt());
//        assertEquals(0F, board.getAvgAge());


    }

    @Test
    public void getLocationDataTest() throws IOException {
        this.SetupManually();
        // when
        DashboardResponse.LocationDataInfo info = dashboardService.getDashboardPerLocation(1L);

        log.info("info getMediaAppsCnt : " + info.getMediaAppsCnt());
        log.info("info getPassedPeopleCntPerDay : " + info.getPassedPeopleCntPerDay().toString());

    }

    @Test
    @Transactional
    public void getFilterDashboardTest() throws IOException{
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

//        locationService.createLocation(1, "2호선 1번 칸");
//        locationService.createLocation(2, "2호선 2번 칸");
//        locationService.createLocation(3, "2호선 3번 칸");

        Location location = locationService.findById(1L);
        Location location2 = locationService.findById(2L);
        Location location3 = locationService.findById(3L);
//        String title = LocalDateTime.now() + " maxmax chicken board";

        String title = "maxmax bb chicken num1 board";
        LocalDate endDate = LocalDate.now().plusDays(2);
        String formatEndDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));        // 미디어 / 대시보드  -> 세팅 및 생성

        MediaRequest.Create mediaRequest = MediaRequest.Create.builder().advertisementDescription("this is awesome").advertisementTitle("Good Fish")
                .startDate("2024-04-07").endDate(formatEndDate).locationId(1L).build();

        Dashboard ds = dashboardService.createDashboard(user);

        Media media =  mediaService.createMedia(mediaRequest, ds, file);
        log.info("CREATED MEDIA URL : " + media.getMediaLink());

        // 여러개의 광고 집행 단위가 존재 가정
        MediaApplication mediaApplication = mediaApplicationService.createMediaApplication(media, location ,mediaRequest.getStartDate(),mediaRequest.getEndDate());

        MediaApplication mediaApplication2 = mediaApplicationService.createMediaApplication(media, location2 ,mediaRequest.getStartDate(),mediaRequest.getEndDate());
        MediaApplication mediaApplication3 = mediaApplicationService.createMediaApplication(media, location3 ,mediaRequest.getStartDate(),mediaRequest.getEndDate());

        // 광고 승락
        List<MediaApplication> mediaApps = mediaApplicationService.findByMedia(media);
        List<Long> mediaAppIds = mediaApps.stream().map(mediaApplication1 -> mediaApplication1.getMediaApplicationId()).collect(Collectors.toList());


        mediaApplicationService.updateStatus(mediaAppIds, Status.ACCEPT);
//        mediaApplicationService.updateStatus(mediaAppIds2, Status.ACCEPT);

        playListService.testUpdatePlayList();

        List<Boolean> testBool = new ArrayList<Boolean>(Collections.nCopies(7, false));
        testBool.set(0, true);
        testBool.set(1, true);
        DashboardResponse.DashboardDetailDataInfo info =  dashboardService.getDashboardFiltered(user.getUserId(), ds.getDashboardId(), true, true, testBool);
    }

}

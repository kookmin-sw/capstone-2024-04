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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
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

    @Test
//    @Transactional
    public void getDashBoardsTest(){
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

        MediaRequest.Create media = MediaRequest.Create.builder().dashboardTitle("maxmax chicken board").dashboardDescription("this is awesome")
                        .startDate("2024-04-01").endDate("2024-04-10").locationId(0L).build();
        MediaRequest.Create media1 = MediaRequest.Create.builder().dashboardTitle("bread advert").dashboardDescription("memorable bread!")
                .startDate("2024-04-10").endDate("2024-04-15").locationId(0L).build();
        MediaRequest.Create media2 = MediaRequest.Create.builder().dashboardTitle("music festival").dashboardDescription("sound so big")
                .startDate("2024-04-17").endDate("2024-04-22").locationId(0L).build();

        List<MediaRequest.Create> testMediaList = new ArrayList<>();
        testMediaList.add(media);
        testMediaList.add(media1);
        testMediaList.add(media2);

        Dashboard ds = new Dashboard();
        for(MediaRequest.Create testMedia : testMediaList){
            ds = dashboardService.createDashboard(testMedia, user);
        }

//        // when
//        List<DashboardResponse.DashboardDataInfo> dashboardList = dashboardService.findDashboardsByUser(user.getUserId());
//        DashboardResponse.DashboardDataInfo dashboard = dashboardService.findDashboardById(user.getUserId(), ds.getDashboardId());
//
//        // then
//        // 개별 findDashboardById 작동 확인
//        assertEquals(dashboard.getTitle(), testMediaList.get(testMediaList.size()-1).getDashboardTitle());
//        assertEquals(dashboard.getDescription(), testMediaList.get(testMediaList.size()-1).getDashboardDescription());
//        // 하나씩 Loop 돌면서 확인
//        for(int i=1; i<= testMediaList.size(); i++){
//            DashboardResponse.DashboardDataInfo info = dashboardList.get(dashboardList.size()-i);
//            assertEquals(info.getTitle(), testMediaList.get(testMediaList.size() - i).getDashboardTitle());
//            assertEquals(info.getDescription(), testMediaList.get(testMediaList.size() - i).getDashboardDescription());
//        }
//
//        // remove
//        for(MediaRequest.Create testMedia : testMediaList){
//            dashboardService.deleteDashboardByTitle(testMedia.getDashboardTitle());
//        }
    }

    @Test
//    @Transactional
    // 특정 광고에 대한 요약 데이터 조회하는 테스트
    // 로직 자체는 동작
    // 카프카 데이터 넣어서 잘 계산, 조회하는지만 검증 필요
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
        Location location = locationService.findById(1L);
        Location location2 = locationService.findById(2L);
        Location location3 = locationService.findById(3L);
//        String title = LocalDateTime.now() + " maxmax chicken board";
        String title = "maxmax bb chicken num1 board";

        // 미디어 / 대시보드  -> 세팅 및 생성
        MediaRequest.Create mediaRequest = MediaRequest.Create.builder().dashboardTitle(title).dashboardDescription("this is awesome")
                .startDate("2024-04-07").endDate("2024-04-20").locationId(1L).build();

        Dashboard ds = dashboardService.createDashboard(mediaRequest, user);
        Media media =  mediaService.createMockMedia(mediaRequest, ds, file);

        // 여러개의 광고 집행 단위가 존재 가정
        MediaApplication mediaApplication = mediaApplicationService.createMediaApplication(media, location ,mediaRequest.getStartDate(),mediaRequest.getEndDate());
        MediaApplication mediaApplication2 = mediaApplicationService.createMediaApplication(media, location2 ,mediaRequest.getStartDate(),mediaRequest.getEndDate());
        MediaApplication mediaApplication3 = mediaApplicationService.createMediaApplication(media, location3 ,mediaRequest.getStartDate(),mediaRequest.getEndDate());
        // 광고 승락
        List<MediaApplication> mediaApps = mediaApplicationService.findByMedia(media);
        List<Long> mediaAppIds = mediaApps.stream().map(mediaApplication1 -> mediaApplication1.getMediaApplicationId()).collect(Collectors.toList());
        mediaApplicationService.updateStatus(mediaAppIds, Status.ACCEPT);

        playListService.testUpdatePlayList();
        // 해당 광고에 대한 daily_media_board 테이블 데이터가 요구됨.
        // CLI 쿼리로 직접 집어넣어서 테스트 하는 방식으로.

        // when
        // 해당 유저가 집행한 광고 데이터 모아서 조회
        DashboardResponse.DashboardDataInfo info = dashboardService.getDashboardWithDataById(user.getUserId(), ds.getDashboardId());



        // then
        // 조회된 대시보드 값 확인
        assertEquals(info.getMaleCnt(), 0L);
    }

    @Test
    @Transactional
    // 특정 유저가 집행한 광고 대시보드 리스트 받아오는 테스트
    // 작성 완료
    public void getDashBoardListTest() throws IOException {
        // given
        // 회원 가입 세팅
        String email = "ert1306@naver.com";
        Optional<User> userOptional = userService.getUserByEmail(email);
        User user = new User();
        if(userOptional.isEmpty()){
            UserResponse.UserInfo userInfo = userService.createUser(email, "dkdk", "drm");
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
        Location location = locationService.findById(1L);
//        String title = LocalDateTime.now() + " maxmax chicken board";
        String title = "maxmax chicken num1 board";

        // 미디어 / 대시보드  -> 1,2,3번 세팅 및 생성
        MediaRequest.Create mediaRequest = MediaRequest.Create.builder().dashboardTitle(title).dashboardDescription("this is awesome")
                .startDate("2024-04-07").endDate("2024-04-16").locationId(1L).build();
        Dashboard ds = dashboardService.createDashboard(mediaRequest, user);

        String title2 = "maxmax chicken num2 board";


        MediaRequest.Create mediaRequest2 = MediaRequest.Create.builder().dashboardTitle(title2).dashboardDescription("this is awesome")
                .startDate("2024-04-02").endDate("2024-04-18").locationId(1L).build();
        Dashboard ds2 = dashboardService.createDashboard(mediaRequest2, user);

        String title3 = "maxmax chicken num3 board";


        MediaRequest.Create mediaRequest3 = MediaRequest.Create.builder().dashboardTitle(title3).dashboardDescription("this is awesome")
                .startDate("2024-04-05").endDate("2024-04-19").locationId(1L).build();
        Dashboard ds3 = dashboardService.createDashboard(mediaRequest3, user);

        Media media =  mediaService.createMockMedia(mediaRequest, ds, file);
        Media media2 =  mediaService.createMockMedia(mediaRequest2, ds2, file);
        Media media3 =  mediaService.createMockMedia(mediaRequest3, ds3, file);

        // MeidaApplication 은 해당 테스트와는 무관
//        MediaApplication mediaApplication = mediaApplicationService.createMediaApplication(media, location ,mediaRequest.getStartDate(),mediaRequest.getEndDate());

        // when
        // 해당 유저가 생성한 대시보드 조회
        List<DashboardResponse.DashboardInfo> dashboardList = dashboardService.getDashboardsByUserId(user.getUserId());


        // then
        // 조회된 대시보드 값 확인
        assertEquals(dashboardList.get(0).getTitle(), title);
        assertEquals(dashboardList.get(1).getTitle(), title2);
        assertEquals(dashboardList.get(2).getTitle(), title3);
    }

    @Test
    @Transactional
    // 특정 유저가 집행한 광고 집행 단위 리스트 여러개 받아오는 테스트
    // 작성 완료
    public void getBoardListTest() throws IOException {
        // given
        // 회원 가입 세팅
        String email = "ert1306@naver.com";
        Optional<User> userOptional = userService.getUserByEmail(email);
        User user = new User();
        if(userOptional.isEmpty()){
            UserResponse.UserInfo userInfo = userService.createUser(email, "dkdk", "drm");
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

        Location location = locationService.findById(1L);
        Location location2 = locationService.findById(2L);
        Location location3 = locationService.findById(3L);

//        String title = LocalDateTime.now() + " maxmax chicken board";
        String title = "maxmax chicken num1 board";

        // 미디어 / 대시보드  -> 1,2,3번 세팅 및 생성
        MediaRequest.Create mediaRequest = MediaRequest.Create.builder().dashboardTitle(title).dashboardDescription("this is awesome")
                .startDate("2024-04-07").endDate("2024-04-20").locationId(1L).build();
        MediaRequest.Create mediaRequest2 = MediaRequest.Create.builder().dashboardTitle(title).dashboardDescription("this is awesome")
                .startDate("2024-04-01").endDate("2024-04-25").locationId(2L).build();
        MediaRequest.Create mediaRequest3 = MediaRequest.Create.builder().dashboardTitle(title).dashboardDescription("this is awesome")
                .startDate("2024-04-04").endDate("2024-04-22").locationId(3L).build();

        Dashboard ds = dashboardService.createDashboard(mediaRequest, user);
        Media media =  mediaService.createMockMedia(mediaRequest, ds, file);

        MediaApplication mediaApplication = mediaApplicationService.createMediaApplication(media, location ,mediaRequest.getStartDate(),mediaRequest.getEndDate());
        MediaApplication mediaApplication2 = mediaApplicationService.createMediaApplication(media, location2 ,mediaRequest2.getStartDate(),mediaRequest2.getEndDate());
        MediaApplication mediaApplication3 = mediaApplicationService.createMediaApplication(media, location3 ,mediaRequest3.getStartDate(),mediaRequest3.getEndDate());



        // when
        // 해당 유저가 생성한 대시보드 조회
        List<DashboardResponse.DashboardInfo> dashboardList = dashboardService.getDashboardsByUserId(user.getUserId());
        List<DashboardResponse.RegisteredMediaAppInfo> infos = dashboardService.getRegisteredBoardsById(user.getUserId(), ds.getDashboardId());


        // then
        // 조회된 대시보드 값 확인
        assertEquals(infos.get(0).getAddress(), location.getAddress());
        assertEquals(infos.get(1).getAddress(), location2.getAddress());
        assertEquals(infos.get(2).getAddress(), location3.getAddress());


        assertEquals(infos.get(0).getMediaApplicationId(), mediaApplication.getMediaApplicationId());
        assertEquals(infos.get(1).getMediaApplicationId(), mediaApplication2.getMediaApplicationId());
        assertEquals(infos.get(2).getMediaApplicationId(), mediaApplication3.getMediaApplicationId());
    }


    @Test
    // 광고 + 광고 집행 단위 + 날짜 형태로 데이터 조회하는 테스트
    // 작성 완료
    public void getBoardPerDaysTest() throws IOException {
        // given
        // 회원 가입 세팅
        String email = "ert1306@naver.com";
        Optional<User> userOptional = userService.getUserByEmail(email);
        User user = new User();
        if(userOptional.isEmpty()){
            UserResponse.UserInfo userInfo = userService.createUser(email, "dkdk", "drm");
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
        Location location = locationService.findById(1L);
//        String title = LocalDateTime.now() + " maxmax chicken board";
        String title = "maxmax chicken num1 board";

        // 미디어 / 대시보드  -> 세팅, 생성
        MediaRequest.Create mediaRequest = MediaRequest.Create.builder().dashboardTitle(title).dashboardDescription("this is awesome")
                .startDate("2024-04-07").endDate("2024-04-20").locationId(1L).build();
        Dashboard ds = dashboardService.createDashboard(mediaRequest, user);
        Media media =  mediaService.createMockMedia(mediaRequest, ds, file);

        MediaApplication mediaApplication = mediaApplicationService.createMediaApplication(media, location ,mediaRequest.getStartDate(),mediaRequest.getEndDate());
        MediaApplication testApp = mediaApplicationService.findById(1L);

        List<MediaApplication> mediaApps = mediaApplicationService.findByMedia(media);
        List<Long> mediaAppIds = mediaApps.stream().map(mediaApplication1 -> mediaApplication1.getMediaApplicationId()).collect(Collectors.toList());
        mediaApplicationService.updateStatus(mediaAppIds, Status.ACCEPT);

        playListService.testUpdatePlayList();

        // when
        // 해당 유저가 생성한 대시보드 조회
        LocalDate date = LocalDate.now();
        DailyMediaBoard board = dailyMediaBoardService.findDailyBoardByDateAndApplication(testApp, date);

        // then
        // 조회된 대시보드 값 확인
        assertEquals(0L, board.getMaleCnt());
        assertEquals(0F, board.getAvgAge());


    }
}

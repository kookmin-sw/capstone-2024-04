package com.drm.server.service;

import com.drm.server.controller.dto.request.MediaRequest;
import com.drm.server.controller.dto.request.ModelRequest;
import com.drm.server.controller.dto.response.DashboardResponse;
import com.drm.server.controller.dto.response.UserResponse;
import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.dashboard.DashboardRepository;
import com.drm.server.domain.location.Location;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.mediaApplication.Status;
import com.drm.server.domain.user.User;
import com.google.api.client.http.MultipartContent;
import jakarta.mail.Multipart;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DashboardServiceTest {
    private final DashboardService dashboardService;
    private final DetectedDataService detectedDataService;

    private final PlayListService playListService;
    private final LocationService locationService;
    private final MediaApplicationService mediaApplicationService;
    private final DashboardRepository dashboardRepository;
    private final UserService userService;
    private final MediaService mediaService;

    @Autowired
    public DashboardServiceTest(DashboardService dashboardService, PlayListService playListService, DetectedDataService detectedDataService, MediaApplicationService mediaApplicationService, LocationService locationService, UserService userService, MediaService mediaService, DashboardRepository dashboardRepository){
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
    @Transactional
    public void getDashBoardsTest(){
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

        // when
        List<DashboardResponse.DashboardInfo> dashboardList = dashboardService.findDashboardsByUser(user.getUserId());
        DashboardResponse.DashboardInfo dashboard = dashboardService.findDashboardById(user.getUserId(), ds.getDashboardId());

        // then
        // 개별 findDashboardById 작동 확인
        assertEquals(dashboard.getTitle(), testMediaList.get(testMediaList.size()-1).getDashboardTitle());
        assertEquals(dashboard.getDescription(), testMediaList.get(testMediaList.size()-1).getDashboardDescription());
        // 하나씩 Loop 돌면서 확인
        for(int i=1; i<= testMediaList.size(); i++){
            DashboardResponse.DashboardInfo info = dashboardList.get(dashboardList.size()-i);
            assertEquals(info.getTitle(), testMediaList.get(testMediaList.size() - i).getDashboardTitle());
            assertEquals(info.getDescription(), testMediaList.get(testMediaList.size() - i).getDashboardDescription());
        }

        // remove
        for(MediaRequest.Create testMedia : testMediaList){
            dashboardService.deleteDashboardByTitle(testMedia.getDashboardTitle());
        }
    }

    @Test
//    @Transactional
    public void getBoardPerAdSummaryTest() throws IOException {
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
        String title = LocalDateTime.now() + " maxmax chicken board";
        MediaRequest.Create media = MediaRequest.Create.builder().dashboardTitle(title).dashboardDescription("this is awesome")
                .startDate("2024-04-02").endDate("2024-04-13").locationId(0L).build();

        List<MediaRequest.Create> testMediaList = new ArrayList<>();
        testMediaList.add(media);

        // 대시보드 생성
        Dashboard ds = new Dashboard();
        for(MediaRequest.Create testMedia : testMediaList){
            ds = dashboardService.createDashboard(testMedia, user);
        }
        // 미디어 생성
        // 올린 것으로 가정하고 진행
        MockMultipartFile file = new MockMultipartFile("image",
                "translate.png",
                "image/png",
                new FileInputStream("/Users/dongguk/Desktop/동국자료/해외여행/CES/translate.png"));
        Media mediaEntity = mediaService.createMockMedia(media, ds, file);
        
        // mediaApplication(광고 집행)
        // locationId = 1 (import.sql 로 미리 생성)
        Location location = locationService.findById(2L);
        MediaApplication medApp = mediaApplicationService.createMediaApplication(mediaEntity, location,  "2024-04-04", "2024-04-15");

        // 승인 가정
        List<Long> medAppIdList = new ArrayList<>();
        medAppIdList.add(medApp.getMediaApplicationId());
        mediaApplicationService.updateStatus(medAppIdList, Status.ACCEPT);

        // PlayList 가 해당일 board 생성 가정
        playListService.testUpdatePlayList();

        // 데이터 들어오는 상황
        LocalDateTime time = LocalDateTime.now();
        List<Boolean> boolList = new ArrayList<>();

        // 랜덤하게 Initalized 된 데이터가 들어오는 경우
        Random random = new Random();
        int totalFrameCnt = random.nextInt(10) + 10;
        int interestFrameCnt = 0;
        for (int i = 0; i < totalFrameCnt; i++) {
            boolean randomBool = random.nextBoolean();
            if(randomBool) { interestFrameCnt += 1;}
            boolList.add(randomBool);
        }
        ModelRequest modelRequest = ModelRequest.builder()
                .cameraId(2L)
                .arriveTime(time.minusSeconds(20))
                .leaveTime(time)
                .presentFrameCnt(totalFrameCnt)
                .interestFrameCnt(interestFrameCnt)
                .frameData(boolList)
                .build();



        // when
        detectedDataService.processDetectedData(modelRequest);

        // then

        // remove
//        mediaApplicationService.deleteMediaApplication(mediaEntity.getMediaId(), medApp.getMediaApplicationId(), user);


    }

    @Test
    public void getBoardListTest(){
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

        MediaRequest.Create media = MediaRequest.Create.builder().dashboardTitle("maxmax chicken board").dashboardDescription("this is awesome")
                .startDate("2024-04-01").endDate("2024-04-10").locationId(0L).build();
        Dashboard ds = dashboardService.createDashboard(media, user);

        // 집행 기간 별 데이터가 필요함.

        // when
        List<DashboardResponse.DashboardInfo> dashboardList = dashboardService.findRegisteredBoardsById(0L, ds.getDashboardId());

        // then
    }

    @Test
    public void getBoardPerDaysTest(){
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

        MediaRequest.Create media = MediaRequest.Create.builder().dashboardTitle("maxmax chicken board").dashboardDescription("this is awesome")
                .startDate("2024-04-01").endDate("2024-04-10").locationId(0L).build();
        Dashboard ds = dashboardService.createDashboard(media, user);

        // when
        Long dashboardId = ds.getDashboardId();
        Long boardId = 0L;
        List<DashboardResponse.DashboardInfo> dashboardList = dashboardService.getDayBoards(0L, dashboardId, boardId);

        // then
    }
}

package com.drm.server.service;

import com.drm.server.controller.dto.request.MediaRequest;
import com.drm.server.controller.dto.request.ModelRequest;
import com.drm.server.controller.dto.response.DashboardResponse;
import com.drm.server.controller.dto.response.UserResponse;
import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.dashboard.DashboardRepository;
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
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DashboardServiceTest {
    DashboardService dashboardService;
    DetectedDataService detectedDataService;

    DashboardRepository dashboardRepository;
    UserService userService;
    MediaService mediaService;

    @Autowired
    public DashboardServiceTest(DashboardService dashboardService, DetectedDataService detectedDataService, UserService userService, MediaService mediaService, DashboardRepository dashboardRepository){
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
        String email = "ert1306@naver.com";
        User user = userService.getUserByEmail(email);

        if(user == null){
            UserResponse.UserInfo userInfo = userService.createUser(email, "dkdk", "drm");
            user = userService.getUser(userInfo.getUserId());
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
    @Transactional
    public void getBoardPerAdSummaryTest() throws IOException {
        // given
        // 회원 가입 세팅
        String email = "ert1306@naver.com";
        User user = userService.getUserByEmail(email);

        if(user == null){
            UserResponse.UserInfo userInfo = userService.createUser(email, "dkdk", "drm");
            user = userService.getUser(userInfo.getUserId());
        }
        // 미디어 생성 요청 세팅
        MediaRequest.Create media = MediaRequest.Create.builder().dashboardTitle("maxmax chicken board").dashboardDescription("this is awesome")
                .startDate("2024-04-01").endDate("2024-04-10").locationId(0L).build();

        List<MediaRequest.Create> testMediaList = new ArrayList<>();
        testMediaList.add(media);

        // 대시보드 생성
        Dashboard ds = new Dashboard();
        for(MediaRequest.Create testMedia : testMediaList){
            ds = dashboardService.createDashboard(testMedia, user);
        }
        // 미디어 생성
        MockMultipartFile file = new MockMultipartFile("image",
                "translate.png",
                "image/png",
                new FileInputStream("/Users/dongguk/Desktop/동국자료/해외여행/CES"));
        mediaService.createMedia(media, ds, file);

        // 데이터 들어오는 상황
        LocalDateTime time = LocalDateTime.now();
        List<Boolean> intList = new ArrayList<>();

        // 랜덤하게 Initalized 된 데이터가 들어오는 경우
        Random random = new Random();
        int totalFrameCnt = random.nextInt(10) + 10;
        int interestFrameCnt = 0;
        for (int i = 0; i < totalFrameCnt; i++) {
            boolean randomBool = random.nextBoolean();
            if(randomBool) { interestFrameCnt += 1;}
            intList.add(randomBool);
        }
        ModelRequest modelRequest = ModelRequest.builder()
                .cameraId(3L)
                .arriveTime(time.minusSeconds(20))
                .leaveTime(time)
                .presentFrameCnt(totalFrameCnt)
                .interestFrameCnt(interestFrameCnt)
                .frameData(intList)
                .build();

        // when
        detectedDataService.processDetectedData(modelRequest);





        // when
        DashboardResponse.DashboardInfo dashboardList = dashboardService.findDashboardById(0L, ds.getDashboardId());

        // then

    }

    @Test
    public void getBoardListTest(){
        // given
        String email = "ert1306@naver.com";
        User user = userService.getUserByEmail(email);
        if(user == null){
            UserResponse.UserInfo userInfo = userService.createUser(email, "dkdk", "drm");
            user = userService.getUser(userInfo.getUserId());
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
        String email = "ert1306@naver.com";
        User user = userService.getUserByEmail(email);
        if(user == null){
            UserResponse.UserInfo userInfo = userService.createUser(email, "dkdk", "drm");
            user = userService.getUser(userInfo.getUserId());
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

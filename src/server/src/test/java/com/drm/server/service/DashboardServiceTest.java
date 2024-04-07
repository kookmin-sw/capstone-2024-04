package com.drm.server.service;

import com.drm.server.controller.dto.request.MediaRequest;
import com.drm.server.controller.dto.response.DashboardResponse;
import com.drm.server.controller.dto.response.UserResponse;
import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DashboardServiceTest {
    DashboardService dashboardService;
    UserService userService;

    @Autowired
    public DashboardServiceTest(DashboardService dashboardService, UserService userService){
        this.dashboardService = dashboardService;
        this.userService = userService;
    }

    // given
    // when
    // then

    @Test
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
        Dashboard ds = dashboardService.createDashboard(media, user);

        // when
        List<DashboardResponse.DashboardInfo> dashboardList = dashboardService.findDashboardsByUser(user);

        // then
        DashboardResponse.DashboardInfo info = dashboardList.get(dashboardList.size()-1);
        assertEquals(info.getTitle(), "maxmax chicken board");
        assertEquals(info.getDescription(), "this is awesome");

        // remove
    }

    @Test
    public void getBoardPerAdSummaryTest(){
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
        DashboardResponse.DashboardInfo dashboardList = dashboardService.findDashboardById(0L, dashboardId);

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
        List<DashboardResponse.DashboardInfo> dashboardList = dashboardService.findRegisteredBoardsById(0L, dashboardId);

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

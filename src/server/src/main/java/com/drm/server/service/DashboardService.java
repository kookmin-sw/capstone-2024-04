package com.drm.server.service;

import com.drm.server.controller.dto.request.MediaRequest;
import com.drm.server.controller.dto.response.DashboardResponse;
import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.dashboard.DashboardRepository;
import com.drm.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.ion.NullValueException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {
    private final DashboardRepository dashboardRepository;
    private final UserService userService;

    public Dashboard createDashboard(MediaRequest.Create create, User user){
        if(dashboardRepository.existsByTitle(create.getDashboardTitle())){
            throw new IllegalArgumentException("FAILED : TITLE ALREADY EXISTS");
        }
        Dashboard dashboard = Dashboard.toEntity(create.getDashboardTitle(), create.getDashboardDescription(), user);
        dashboardRepository.save(dashboard);
        String msg = "DASHBOARD CREATED :" + Long.toString(dashboard.getDashboardId());
        log.info(msg);
        return dashboard;
    }

    public void deleteDashboardById(Long dashboardId){
        dashboardRepository.deleteById(dashboardId);
        String msg = "DASHBOARD DELETED :" + Long.toString(dashboardId);
        log.info(msg);
    }

    public void deleteDashboardByTitle(String title){
        dashboardRepository.deleteByTitle(title);
        String msg = "DASHBOARD DELETED :" + title;
        log.info(msg);
    }

    public List<Dashboard> findByUser(User user){
        List<Dashboard> dashboards = dashboardRepository.findByUser(user).orElse(Collections.emptyList());
        return dashboards;
    }


    public List<DashboardResponse.DashboardInfo> findDashboardsByUser(Long userId) {
        User user  = userService.getUser(userId);
        List<Dashboard> dashboards = dashboardRepository.findAllByUser(user);
        List<DashboardResponse.DashboardInfo> responses = dashboards.stream().map(DashboardResponse.DashboardInfo::new).collect(Collectors.toList());
        String msg = "DASHBOARD SEARCHED MADE BY USER:" + Long.toString(userId);
        log.info(msg);
        return responses;
    }

    public DashboardResponse.DashboardInfo findDashboardById(Long userId, Long dashboardId) {
        Optional<Dashboard> dashboard = dashboardRepository.findById(dashboardId);
        if(dashboard.isEmpty()){
            throw new NullValueException("DASHBOARD NOT EXISTS" + Long.toString(dashboardId));
        }
        DashboardResponse.DashboardInfo response = new DashboardResponse.DashboardInfo(dashboard.get());
        return response;
    }

    public List<DashboardResponse.DashboardInfo> findRegisteredBoardsById(Long userId, Long dashboardId) {
        List<DashboardResponse.DashboardInfo> responses = null;
        return responses;
    }

    public List<DashboardResponse.DashboardInfo> getDayBoards(Long userId, Long dashboardId, Long boardId) {
        List<DashboardResponse.DashboardInfo> responses = null;
        return responses;
    }
}

package com.drm.server.service;

import com.drm.server.controller.dto.request.MediaRequest;
import com.drm.server.controller.dto.response.DashboardResponse;
import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.dashboard.DashboardRepository;
import com.drm.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardRepository dashboardRepository;

    public Dashboard createDashboard(MediaRequest.Create create, User user){
        Dashboard dashboard = Dashboard.toEntity(create.getDashboardTitle(), create.getDashboardDescription(), user);
        return dashboardRepository.save(dashboard);
    }
    public List<Dashboard> findByUser(User user){
        List<Dashboard> dashboards = dashboardRepository.findByUser(user).orElse(Collections.emptyList());
        return dashboards;
    }


    public List<DashboardResponse.DashboardInfo> findDashboardsByUser(User getUser) {
        List<DashboardResponse.DashboardInfo> responses = null;
        return responses;
    }

    public DashboardResponse.DashboardInfo findDashboardById(Long userId, Long dashboardId) {
        DashboardResponse.DashboardInfo response = null;
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

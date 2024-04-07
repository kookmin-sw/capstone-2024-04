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


    public List<DashboardResponse> findDashboardsByUser(User getUser) {
    }

    public DashboardResponse findDashboardById(Long userId, Long dashboardId) {
    }

    public List<DashboardResponse> findRegisteredBoardsById(Long userId, Long dashboardId) {
    }

    public List<DashboardResponse> getDayBoards(Long userId, Long dashboardId, Long boardId) {
    }
}

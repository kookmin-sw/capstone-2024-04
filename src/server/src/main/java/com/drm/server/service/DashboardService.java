package com.drm.server.service;

import com.drm.server.controller.dto.request.MediaRequest;
import com.drm.server.controller.dto.response.DashboardResponse;
import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.dashboard.DashboardRepository;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardRepository dashboardRepository;

    public Dashboard createDashboard(MediaRequest.Create create, User user){
        Dashboard dashboard = Dashboard.toEntity(create.getDashboardTitle(), create.getDashboardDescription(), user);
        return dashboardRepository.save(dashboard);
    }
}
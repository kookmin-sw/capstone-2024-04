package com.drm.server.service;

import com.drm.server.controller.dto.request.MediaRequest;
import com.drm.server.controller.dto.response.DashboardResponse;
import com.drm.server.domain.dailyMediaBoard.DailyMediaBoard;
import com.drm.server.domain.dashboard.Dashboard;
import com.drm.server.domain.dashboard.DashboardRepository;
import com.drm.server.domain.location.Location;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.user.User;
import com.drm.server.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.ion.NullValueException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {
    private final DashboardRepository dashboardRepository;
    private final DailyMediaBoardService dailyMediaBoardService;
    private final UserService userService;
    private final MediaService mediaService;
    private final MediaApplicationService mediaApplicationService;
    private final LocationService locationService;

    public Dashboard createDashboard( User user){
        Dashboard dashboard = Dashboard.toEntity(user);
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


    public List<Dashboard> findByUser(User user){
        List<Dashboard> dashboards = dashboardRepository.findByUser(user).orElse(Collections.emptyList());
        return dashboards;
    }

    // 특정 유저가 등록한 광고들(dashboard) 반환 로직
    public List<DashboardResponse.DashboardInfo> getDashboardsByUserId(Long userId) {
        User user  = userService.getUser(userId);
        List<Dashboard> dashboards = dashboardRepository.findAllByUser(user).orElseThrow(() -> new NullValueException("DASHBOARD NOT EXISTS"));
        List<DashboardResponse.DashboardInfo> dashboardInfos = new ArrayList<>();

        for(Dashboard dashboard : dashboards){
            Media media = mediaService.findOneMediaByDashboard(dashboard);
            DashboardResponse.DashboardInfo info = new DashboardResponse.DashboardInfo(dashboard,media.getMediaLink());
            dashboardInfos.add(info);
        }
        String msg = "DASHBOARD SEARCHED MADE BY USER:" + Long.toString(userId);
        log.info(msg);
        return dashboardInfos;
    }

    // 특정 광고 - 대시보드에 들어가는 데이터 반환 로직
    public DashboardResponse.DashboardDataInfo getDashboardWithDataById(Long userId, Long dashboardId) {
        // 해당 광고(media) 를 집행한 광고 집행 이벤트들을 모두 조회
        List<MediaApplication> mediaAppList = findMediaApplicationByDashboardId(userId, dashboardId);
        // 조회된 광고 집행 이벤트들에서 Daily 별 수집된 데이터를 합산하여 Dto Response 리턴
        DashboardResponse.DashboardDataInfo response = calculateDataPerDashboard(mediaAppList);
        String Message = "DASHBOARD SUCCESSFULLY RETURN : " + Long.toString(dashboardId);
        log.info(Message);
        return response;
    }


    // 특정 광고에 대해 광고 집행된 이벤트들 반환
    public List<DashboardResponse.RegisteredMediaAppInfo> getRegisteredBoardsById(Long userId, Long dashboardId) {
        List<MediaApplication> mediaAppList = findMediaApplicationByDashboardId(userId, dashboardId);
        List<DashboardResponse.RegisteredMediaAppInfo> registeredMediaAppInfos = new ArrayList<>();
        for(MediaApplication app : mediaAppList){
            Location location = app.getLocation();
            String address = location.getAddress();
            DashboardResponse.RegisteredMediaAppInfo info = DashboardResponse.RegisteredMediaAppInfo.builder().address(address)
                    .mediaApplicationId(app.getMediaApplicationId()).build();
            registeredMediaAppInfos.add(info);
        }
        return registeredMediaAppInfos;
    }

    // 특정 광고 + 특정 일에 대한 집행 결과 데이터 반환
    public DashboardResponse.DashboardDataInfo getDayBoards(Long userId, Long mediaAplicationId, LocalDate date) {
        // 광고 집행 단위가 유저의 것인지 확인
        User user = userService.getUser(userId);
        MediaApplication mediaApplication = mediaApplicationService.findById(mediaAplicationId);
        mediaApplicationService.deleteVerify(mediaApplication, user);
        // 일별 데이터 조회
        DailyMediaBoard board = dailyMediaBoardService.findDailyBoardByDateAndApplication(mediaApplication, date);

        // Board 로 Dashboard response dto 만들어서 반환
        DashboardResponse.DashboardDataInfo response = new DashboardResponse.DashboardDataInfo(board);
        return response;
    }

    // 대시보드 -> 광고(media) -> 광고 집행(media Application) 이벤트 반환
    public List<MediaApplication> findMediaApplicationByDashboardId(Long userId, Long dashboardId){
        verifyUser(userId, dashboardId);
        Dashboard dashboard = dashboardRepository.findById(dashboardId).orElseThrow(() -> new NullValueException("DASHBOARD NOT EXISTS"));
        Media media = mediaService.findOneMediaByDashboard(dashboard);
        List<MediaApplication> mediaAppList = mediaApplicationService.findByMedia(media);
        return mediaAppList;
    }

    // 대시보드가 유저가 소유한 것인지 확인
    private void verifyUser(Long userId, Long dashboardId) {
        Dashboard board = dashboardRepository.findById(dashboardId).get();
        if(board.getUser().getUserId() != userId){
            throw new ForbiddenException("해당 조회에 접근 권한이 없습니다");
        }
    }
    // 일별로 저장되어 있는 광고 결과 데이터를 합치기
    public DashboardResponse.DashboardDataInfo calculateDataPerDashboard(List<MediaApplication> mediaAppList){
        DashboardResponse.DashboardDataInfo boardInfo = new DashboardResponse.DashboardDataInfo();

        for (MediaApplication mediaApp : mediaAppList){
            List<DailyMediaBoard> boards = dailyMediaBoardService.findDailyBoardByMediaApplication(mediaApp);
            for(DailyMediaBoard board : boards){
                // 시간별 관심 데이터 합치기
                // 시간별 유동인구 데이터 합치기
                boardInfo.addHourlyPassedCount(board.getHourlyPassedCount());
                boardInfo.addHourlyInterestedCount(board.getHourlyInterestedCount());
                // 수치 합치기
                boardInfo.setFemaleInterestCnt(boardInfo.getFemaleInterestCnt() + board.getFemaleInterestCnt());
                boardInfo.setMaleCnt(boardInfo.getMaleCnt()+ board.getMaleCnt());
                boardInfo.setMaleInterestCnt(boardInfo.getMaleInterestCnt() + board.getMaleInterestCnt());
                // 평균값 계산 및 합치기
                Long newTotalPeopleCnt = boardInfo.getTotalPeopleCount() + board.getTotalPeopleCount();
                if(newTotalPeopleCnt > 0) {
                    boardInfo.setAvgAge((boardInfo.getAvgAge() * boardInfo.getTotalPeopleCount() + board.getAvgAge() * board.getTotalPeopleCount()) /
                            newTotalPeopleCnt);
                    boardInfo.setAvgStaringTime((boardInfo.getAvgStaringTime() * boardInfo.getTotalPeopleCount() + board.getAvgStaringTime() * board.getTotalPeopleCount())
                            / newTotalPeopleCnt);
                }
                boardInfo.setTotalPeopleCount(newTotalPeopleCnt);
            }
        }
        return boardInfo;
    }
}

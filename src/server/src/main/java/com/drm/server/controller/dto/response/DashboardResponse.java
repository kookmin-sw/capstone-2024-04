package com.drm.server.controller.dto.response;

import com.drm.server.domain.dailyMediaBoard.DailyMediaBoard;
import com.drm.server.domain.dashboard.Dashboard;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.primitives.Longs.asList;

@Slf4j
public class DashboardResponse {
    @Getter
    @Setter
    public static class DashboardInfo{
        @Schema(description = "가장 큰 단위의 대시보드 제목",example = "대시보드 제목")
        private String title;
        @Schema(description = "가장 큰 단위의 대시보드 설명",example = "대시보드 설명")
        private String description;

        @Schema(description = "dashboard Id 값", example = "0")
        private Long dashboardId;

        @Schema(description = "해당 대시보드에 대응하는 광고의 url", example = "dk.net.test1.png")
        private String mediaUrl;

        @Builder
        public DashboardInfo(Dashboard dashboard, String mediaUrl){
            this.title = title;
            this.description = description;
            this.dashboardId = dashboardId;
            this.mediaUrl = mediaUrl;
        }
    }

    @Getter
    @Setter
    public static class DashboardDataInfo {
        @Schema(description = "시간당 관심을 표현한 사람 수 (0시 - 23시 까지)", example = "[20, 31, 50, 20, 30, 6, 20, 31, 50, 20, 30, 6, 20, 31, 50, 20, 30, 6, 20, 31, 50, 20, 30, 6]")
        private List<Long> hourlyInterestedCount;

        @Schema(description = "시간당 포착된 사람 수 (0시 - 23시 까지)", example = "[15, 28, 45, 20, 25, 7, 19, 30, 48, 18, 27, 5, 20, 31, 50, 20, 30, 6, 20, 31, 50, 20, 30, 6]")
        private List<Long> hourlyPassedCount;

        @Schema(description = "전체 포착된 사람 수", example = "254")
        private Long totalPeopleCount;

        @Schema(description = "해당 광고 평균 시청 시간", example = "3.1")
        private float avgStaringTime;

        @Schema(description = "해당 광고 평균 시청 나이 (0.0 ~ F )", example = "27.2")
        private float avgAge;

        @Schema(description = "관심을 표현한 남자의 인원 수", example = "150")
        private Long maleInterestCnt;

        @Schema(description = "관심을 표현한 여자의 인원 수", example = "104")
        private Long femaleInterestCnt;

        @Schema(description = "집계된 남자의 인원 수", example = "200")
        private Long maleCnt;

        @Builder
        public DashboardDataInfo() {
            this.hourlyInterestedCount = new ArrayList<>(asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0));
            this.hourlyPassedCount =  new ArrayList<>(asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0));
            this.totalPeopleCount = 0L;
            this.avgStaringTime = 0;
            this.avgAge = 0;
            this.maleInterestCnt = 0L;
            this.femaleInterestCnt = 0L;
            this.maleCnt = 0L;
        }

        public DashboardDataInfo(DailyMediaBoard board){
            this.hourlyInterestedCount = board.getHourlyInterestedCount();
            this.hourlyPassedCount = board.getHourlyPassedCount();
            this.femaleInterestCnt = board.getFemaleInterestCnt();
            this.maleInterestCnt = board.getMaleInterestCnt();
            this.totalPeopleCount = board.getTotalPeopleCount();
            this.avgAge = board.getAvgAge();
            this.avgStaringTime = board.getAvgStaringTime();
            this.maleCnt = board.getMaleCnt();
        }

        public void addHourlyPassedCount(List<Long> boardList){
            this.hourlyPassedCount = calculateHourListDataPerHour(this.hourlyPassedCount, boardList);
        }

        public void addHourlyInterestedCount(List<Long> boardList) {
            this.hourlyInterestedCount = calculateHourListDataPerHour(this.hourlyInterestedCount, boardList);
        }
    }

    @Getter
    @Setter
    // Location 에 대한 유동인구 정보 DTO
    public static class LocationDataInfo {
        @Schema(description = "해당 장소 집계된, 집행된 광고의 날짜 수", example = "25")
        private Long mediaAppsCnt;

        @Schema(description = "평균 유동인구 수(일별)", example = "26024")
        private Long passedPeopleCntPerDay;

        @Schema(description = "시간당 평균 유동인구 수(리스트 형태)", example = "[1500, 280, 452, 204, 255, 72, 19, 30, 48, 185, 271, 54, 203, 314, 505, 204, 310, 62, 204, 351, 503, 20, 30, 6]")
        private List<Long> passedPeopleListPerHour;

//        @Schema(description = "시간대별 평균 나이대", example = "[15, 28, 45, 20, 25, 7, 19, 30, 48, 18, 27, 5, 20, 31, 50, 20, 30, 6, 20, 31, 50, 20, 30, 6]")
//        private List<Long> avgAgeListPerHour;

        @Schema(description = "유동 인구 남자의 비율(성비) 0 ~ 100 사이의 숫자", example = "64")
        private Long avgMaleRatio;

        public LocationDataInfo(){
            this.mediaAppsCnt = 0L;
            this.passedPeopleCntPerDay = 0L;
            this.passedPeopleListPerHour = new ArrayList<>(asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0));
            this.avgMaleRatio = 0L;
        }

        public void addMediaAppsCnt(){
            this.mediaAppsCnt++;
        }

        // 해당 장소 방문 인원 수 집계(평균 - 일별)
        public void addPassedPeopleCntPerDay(Long count){
            this.passedPeopleCntPerDay = this.passedPeopleCntPerDay * (this.mediaAppsCnt - 1) + count / this.mediaAppsCnt;
        }

        // 평균이므로 나눠주는 연산 필요함 -> 전체 일 수 덧셈 이후에 나누기
        public void addPassedPeopleCntPerHour(List<Long> boardList){
            List<Long> summedList = calculateHourListDataPerHour(this.passedPeopleListPerHour, boardList);
            this.passedPeopleListPerHour = divideHourListData(summedList, this.mediaAppsCnt);
        }

        public void addAvgMaleRatio(Long maleCnt, Long totalCnt){
            // 기존까지의 avgMale 비율 + 1일치 데이터 avgMale / 전체 일 수
            Long avgMaleNum = (long)((double)maleCnt / totalCnt * 100);
            this.avgMaleRatio = (this.avgMaleRatio * (this.mediaAppsCnt - 1) + avgMaleNum) / this.mediaAppsCnt;
        }

        public void updateDtoWithBoardData(Long totalCnt, List<Long> boardList, Long maleCnt){
            if(totalCnt == 0) {
                log.info("UPDATING : NO PEOPLE DATA FROM THE DAILY BOARD");
                return;
            }
            // daily board data (일별) 들어온다.
            // add Apps Cnt 가 먼저 실행되야 한다. 그렇지 않으면 avgMaleRatio 계산 시에 0으로 나눠주게 되어 에러가 발생한다.
            this.addMediaAppsCnt();
            this.addPassedPeopleCntPerDay(totalCnt);
            this.addPassedPeopleCntPerHour(boardList);
            this.addAvgMaleRatio(maleCnt, totalCnt);
        }


    }


    @Getter
    @Setter
    @Builder
    public static class RegisteredMediaAppInfo {
        @Schema(description = "광고 집행 위치", example = "미래관 4층 디스플레이")
        private String address;

        @Schema(description = "광고에 대한 설명")
        private String description;

        @Schema(description = "광고 집행 Id")
        private Long mediaApplicationId;
    }
    public static List<Long> calculateHourListDataPerHour(List<Long> updateList, List<Long> inputList){
        if(updateList.size() != inputList.size()) throw new IllegalStateException("HOUR PASSED DATA LIST SIZE IS DIFFERENT");
        for(int i=0; i<inputList.size(); i++){
            updateList.set(i, inputList.get(i) + updateList.get(i));
        }
        return updateList;
    }

    public static List<Long> divideHourListData(List<Long> updateList, Long cnt){
        if(cnt == 0) throw new IllegalArgumentException("DIVIDE CNT IS ZERO");
        for(int i=0; i<updateList.size(); i++){
            Long new_data = updateList.get(i)/ cnt;
            updateList.set(i, new_data);
        }
        return updateList;
    }
}



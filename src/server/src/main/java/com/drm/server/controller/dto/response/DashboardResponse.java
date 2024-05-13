package com.drm.server.controller.dto.response;

import com.drm.server.domain.dailyDetailBoard.DailyDetailBoard;
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
    public static class DashboardDetailDataInfo {
        @Schema(description = "필터링된 인원 중 전체 포착된 사람 수", example = "254")
        private Long totalPeopleCount;

        @Schema(description = "필터링된 인원 중 관심을 표현한 인원 수", example = "104")
        private Long InterestPeopleCnt;

        @Schema(description = "필터링된 인원 중 해당 광고 평균 시청 시간", example = "3.1")
        private float avgStaringTime;

        @Schema(description = "필터링된 인원의 광고 관심도", example = "12.1")
        private float attentionRatio;

        @Builder
        public DashboardDetailDataInfo(){
            this.totalPeopleCount = 0L;
            this.InterestPeopleCnt = 0L;
            this.avgStaringTime = 0F;
            this.attentionRatio = 0F;
        }

        public void addDetailDataInfo(DashboardDetailDataInfo inputInfo){
            // divide by 0 를 막기 위해 우선적으로 더해주는 것이 좋음.
            this.totalPeopleCount += inputInfo.getTotalPeopleCount();
            this.InterestPeopleCnt += inputInfo.getInterestPeopleCnt();
            this.avgStaringTime = calculateStaringTime(inputInfo.getAvgStaringTime(), inputInfo.getInterestPeopleCnt());
            this.attentionRatio = calculateAttentionRatio();
        }
        public void addDetailData(DailyDetailBoard board){
            this.totalPeopleCount += board.getTotalPeopleCount();
            this.InterestPeopleCnt += board.getInterestCount();
            this.avgStaringTime = calculateStaringTime(board.getAvgStaringTime(), board.getInterestCount());
            this.attentionRatio = calculateAttentionRatio();

        }
        private float calculateStaringTime(float staringTime, Long inputInterestCnt){
            return (this.avgStaringTime * (this.InterestPeopleCnt - inputInterestCnt) + staringTime) / this.InterestPeopleCnt;
        }
        private float calculateAttentionRatio(){
            if(totalPeopleCount == 0) {
                return 0F;
            }
            else if(totalPeopleCount < 0) {
                throw new IllegalArgumentException("ERR : TOTAL PEOPLE CNT IS NEGATIVE");
            }
            else{
                return this.InterestPeopleCnt / this.totalPeopleCount;
            }
        }
    }

    @Getter
    @Setter
    public static class DashboardDataInfo {
        @Schema(description = "해당 광고에 대해 집계된, 집행된 광고의 날짜 수", example = "25")
        private Long mediaAppsCnt;
        @Schema(description = "시간당 관심을 표현한 사람 수 (0시 - 23시 까지)", example = "[20, 31, 50, 20, 30, 6, 20, 31, 50, 20, 30, 6, 20, 31, 50, 20, 30, 6, 20, 31, 50, 20, 30, 6]")
        private List<Long> hourlyInterestedCount;

        @Schema(description = "시간당 포착된 사람 수 (0시 - 23시 까지)", example = "[15, 28, 45, 20, 25, 7, 19, 30, 48, 18, 27, 5, 20, 31, 50, 20, 30, 6, 20, 31, 50, 20, 30, 6]")
        private List<Long> hourlyPassedCount;

        @Schema(description = "나이대별 관심을 보인 사람 수(10대 - 90대)", example = "[15, 28, 45, 20, 25, 7, 19, 30, 48]")
        private List<Long> interestedPeopleAgeRangeCount;

        @Schema(description = "시간당 평균 응시 횟수(0시 - 23시까지)", example = "[2.7, 3.6, 3.3, 9.2, 1.1, 1.2, 2.7, 3.6, 3.3, 9.2, 1.1, 1.2, 2.7, 3.6, 3.3, 9.2, 1.1, 1.2, 2.7, 3.6, 3.3, 9.2, 1.1, 1.2]")
        private List<Float> hourlyAvgStaringTime;
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
            this.hourlyInterestedCount = new ArrayList<>(Collections.nCopies(24, 0L));
            this.hourlyPassedCount =  new ArrayList<>(Collections.nCopies(24, 0L));
            this.hourlyAvgStaringTime = new ArrayList<>(Collections.nCopies(24,0F));
            this.interestedPeopleAgeRangeCount = new ArrayList<>(Collections.nCopies(10, 0L));
            this.totalPeopleCount = 0L;
            this.avgStaringTime = 0;
            this.avgAge = 0;
            this.maleInterestCnt = 0L;
            this.femaleInterestCnt = 0L;
            this.maleCnt = 0L;
            this.mediaAppsCnt = 0L;
        }

        public DashboardDataInfo(DailyMediaBoard board){
            // interest age add
            this.hourlyInterestedCount = board.getHourlyInterestedCount();
            this.hourlyPassedCount = board.getHourlyPassedCount();
            this.femaleInterestCnt = board.getFemaleInterestCnt();
            this.maleInterestCnt = board.getMaleInterestCnt();
            this.totalPeopleCount = board.getTotalPeopleCount();
            this.avgAge = board.getAvgAge();
            this.avgStaringTime = board.getAvgStaringTime();
            this.maleCnt = board.getMaleCnt();
        }
        public void addMediaAppsCnt(){
            this.mediaAppsCnt++;
        }
        public void addHourlyAvgStaringTime(List<Float> boardList){
            List<Float> summedList = DashboardCalculator.sumHourListDataPerHour(this.hourlyAvgStaringTime, boardList);
            this.hourlyAvgStaringTime = DashboardCalculator.divideHourListData(summedList, this.mediaAppsCnt);

        }
        public void addHourlyPassedCount(List<Long> boardList){
            this.hourlyPassedCount = DashboardCalculator.sumHourListDataPerHour(this.hourlyPassedCount, boardList);
        }

        public void addHourlyInterestedCount(List<Long> boardList) {
            this.hourlyInterestedCount = DashboardCalculator.sumHourListDataPerHour(this.hourlyInterestedCount, boardList);
        }
        public void addHourlyInterestedPeopleAgeRange(List<Long> boardList){
            this.interestedPeopleAgeRangeCount = DashboardCalculator.sumHourListDataPerHour(this.interestedPeopleAgeRangeCount, boardList);
        }
        public void updateDtoWithBoardData(Long totalPeopleCntData, List<Long> interestedPeopleAgeListData, List<Long> hourlyPassedData, List<Long> hourlyInterestData,
                                     List<Float> avgStaringTimeListData, Float avgAgeData, Float avgStaringData,
                                           Long maleInterestCntData, Long femaleInterestCntData, Long maleCntData){
            // 광고에 대해 해당 광고 집행 횟수 +1
            this.addMediaAppsCnt();
            // 시간별 관심 데이터 합치기
            // 시간별 유동인구 데이터 합치기
            this.addHourlyPassedCount(hourlyPassedData);
            this.addHourlyInterestedCount(hourlyInterestData);
            // 관심 가진 사람의 나이대별 집계
            this.addHourlyInterestedPeopleAgeRange(interestedPeopleAgeListData);
            // * avgStaring 은 Float 연산
            this.addHourlyAvgStaringTime(avgStaringTimeListData);
            // 수치 합치기
            this.setFemaleInterestCnt(this.getFemaleInterestCnt() + femaleInterestCntData);
            this.setMaleCnt(this.getMaleCnt()+ maleCntData);
            this.setMaleInterestCnt(this.getMaleInterestCnt() + maleInterestCntData);
            // 평균값 계산 및 합치기
            Long newTotalPeopleCnt = this.getTotalPeopleCount() + totalPeopleCntData;
            if(newTotalPeopleCnt > 0) {
                this.setAvgAge((this.getAvgAge() * this.getTotalPeopleCount() + avgAgeData * totalPeopleCntData) / newTotalPeopleCnt);
                this.setAvgStaringTime((this.getAvgStaringTime() * this.getTotalPeopleCount() + avgStaringData * totalPeopleCntData)
                        / newTotalPeopleCnt);
            }
            this.setTotalPeopleCount(newTotalPeopleCnt);
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

        @Schema(description = "나이대별 방문자 수(10대 - 90대)", example = "[15, 28, 45, 20, 25, 7, 19, 30, 48]")
        private List<Long> totalAgeRangeCount;

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
            List<Long> summedList = DashboardCalculator.sumHourListDataPerHour(this.passedPeopleListPerHour, boardList);
            this.passedPeopleListPerHour = DashboardCalculator.divideHourListData(summedList, this.mediaAppsCnt);
        }

        public void addAvgMaleRatio(Long maleCnt, Long totalCnt){
            // 기존까지의 avgMale 비율 + 1일치 데이터 avgMale / 전체 일 수
            Long avgMaleNum = (long)((double)maleCnt / totalCnt * 100);
            this.avgMaleRatio = (this.avgMaleRatio * (this.mediaAppsCnt - 1) + avgMaleNum) / this.mediaAppsCnt;
        }
        private void addTotalAveRangeCnt(List<Long> visitorAgeList) {
            this.totalAgeRangeCount = DashboardCalculator.sumHourListDataPerHour(this.totalAgeRangeCount, visitorAgeList);
        }
        public void updateDtoWithBoardData(Long totalCnt, List<Long> boardList, List<Long> visitorAgeList, Long maleCnt){
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
            this.addTotalAveRangeCnt(visitorAgeList);
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

}



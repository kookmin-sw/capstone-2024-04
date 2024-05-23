package com.drm.server.domain.dailyMediaBoard;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.handler.FloatConverter;
import com.drm.server.handler.LongConverter;
import com.drm.server.domain.mediaApplication.MediaApplication;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DailyMediaBoard extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaDataId;

    // 전체 포착된 사람 수
    @Column
    private Long totalPeopleCount;

    // 시간당 관심을 표현한 사람 수 (0시 - 12시 까지)
    @Convert(converter = LongConverter.class)
    private List<Long> hourlyPassedCount;

    // 시간당 포착된 사람 수 (0시 - 12시 까지)
    @Convert(converter = LongConverter.class)
    private List<Long> hourlyInterestedCount;

    @Convert(converter = FloatConverter.class)
    private List<Float> hourlyAvgStaringTime;

    @Convert(converter = LongConverter.class)
    private List<Long> interestedAgeRangeCount;

    @Convert(converter = LongConverter.class)
    private List<Long> totalAgeRangeCount;

    @ManyToOne
    @JoinColumn(name = "mediaApplicationId")
    private MediaApplication mediaApplication;



    // 관심을 표현한 남자의 인원 수
    private Long maleInterestCnt;
    // 관심을 표현한 여자의 인원 수
    private Long femaleInterestCnt;
    // 집계된 남자의 인원 수
    private Long maleCnt;

    // 해당 광고 평균 시청 시간
    private float avgStaringTime;
    // 해당 광고 평균 시청 나이 (0.0 ~ F )
    private float avgAge;

//    시간별 유동 인구 추가
    public void addHourlyPassedCount(int hour) {
        this.hourlyPassedCount.set(hour, this.hourlyPassedCount.get(hour) + 1);
    }
//   시간별 관심 인원 추가
    public void addHourlyInterestedCount(int hour) {
        this.hourlyInterestedCount.set(hour, this.hourlyInterestedCount.get(hour) + 1);
    }
//    시간별 평균 시선시간
// fps > 1 인 상황에 대응하기 위해 CaptureCount * (1/fps) 를 곱해주는 것으로 수정
// ex: fps = 25 인 경우 -> CaptureCount 10 일시 -> 10 * (1/25) = 0.4초 응시
    public void updateHourlyAvgStaringTime(int hour, float staringTime, int fps) {
        float staringCalculatedTime = staringTime * (1/fps);
        this.hourlyAvgStaringTime.set(hour, ((this.hourlyAvgStaringTime.get(hour) *  this.getHourlyInterestedCount().get(hour) + staringCalculatedTime)
                / (this.getHourlyInterestedCount().get(hour) + 1)  ));
    }

//    연령대별 관심 수
    public void updateInterestedAgeRangeCount(int ageRange) {
        this.interestedAgeRangeCount.set(ageRange - 1, this.interestedAgeRangeCount.get(ageRange  - 1) + 1);
    }
// 연령대별 유동인구 수
    public void addTotalAgeRangeCount(int ageRange) {
        this.totalAgeRangeCount.set(ageRange - 1, this.totalAgeRangeCount.get(ageRange - 1) + 1);
    }

    public void addMaleCnt() {
        this.maleCnt +=1;
    }

    public void addMaleInterestCnt() {
        this.maleInterestCnt += 1;
    }

    public void addFemaleInterestCnt() {
        this.femaleInterestCnt +=1;
    }

    public void addTotalPeopleCount() {
        this.totalPeopleCount +=1;
    }
    // 평균응시시간은 전체 관심있는 인원으로 평균값설정
    // fps > 1 인 상황에 대응하기 위해 CaptureCount * (1/fps) 를 곱해주는 것으로 수정
    // ex: fps = 25 인 경우 -> CaptureCount 10 일시 -> 10 * (1/25) = 0.4초 응시
    public void updateAvgStaringTime(int faceCaptureCount, int fps) {
        float faceCaptureSec = faceCaptureCount * (1/fps);
        this.avgStaringTime = ((this.getAvgStaringTime() * (this.maleInterestCnt + this.femaleInterestCnt))+faceCaptureSec) / ((this.maleInterestCnt + this.femaleInterestCnt)+ 1);
    }

    public void updateAvgAge(int age) {
        this.avgAge = ((this.avgAge * this.totalPeopleCount) + age)/(this.totalPeopleCount +1);
    }
    public static DailyMediaBoard toEntity(MediaApplication mediaApplication){
        return DailyMediaBoard.builder().totalPeopleCount(0L)
                .hourlyInterestedCount( new ArrayList<>(Collections.nCopies(24, 0L)))
                .hourlyPassedCount( new ArrayList<>(Collections.nCopies(24, 0L)))
                .hourlyAvgStaringTime(new ArrayList<>(Collections.nCopies(24,0F)))
                .totalAgeRangeCount(new ArrayList<>(Collections.nCopies(9,0L)))
                .interestedAgeRangeCount(new ArrayList<>(Collections.nCopies(9,0L)))
                .mediaApplication(mediaApplication)
                .maleInterestCnt(0L).femaleInterestCnt(0L).maleCnt(0L)
                .avgStaringTime(0F).avgAge(0F)
                .build();
    }

}

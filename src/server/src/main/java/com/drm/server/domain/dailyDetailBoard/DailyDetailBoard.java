package com.drm.server.domain.dailyDetailBoard;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.domain.dailyMediaBoard.DailyMediaBoard;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.handler.FloatConverter;
import com.drm.server.handler.LongConverter;
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
public class DailyDetailBoard extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyDetailBoard;

    @Column
    private int ageRange;
    private Long totalPeopleCount;
    private Long interestCount;
    private boolean male;
    private float avgStaringTime;

    // 시간당 관심을 표현한 사람 수 (0시 - 12시 까지)
    @Convert(converter = LongConverter.class)
    private List<Long> hourlyPassedCount;

    // 시간당 포착된 사람 수 (0시 - 12시 까지)
    @Convert(converter = LongConverter.class)
    private List<Long> hourlyInterestedCount;

    @Convert(converter = FloatConverter.class)
    private List<Float> hourlyAvgStaringTime;

    @ManyToOne
    @JoinColumn(name = "daily_media_board_id")
    private DailyMediaBoard dailyMediaBoard;

    public void addInterestCount() {
        this.interestCount+=1;
    }

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
    public void updateAvgStaringTime(int faceCaptureCount, int fps) {
        float faceCaptureSec = faceCaptureCount * (1/fps);
        this.avgStaringTime = ((this.getAvgStaringTime() * (this.totalPeopleCount)+faceCaptureSec) / ((this.totalPeopleCount)+ 1));
    }



    public void addTotalPeopleCount() {
        this.totalPeopleCount+=1;
    }

    public static DailyDetailBoard toEntity(int ageRange,boolean male,DailyMediaBoard dailyMediaBoard){
        return DailyDetailBoard.builder()
                .ageRange(ageRange)
                .totalPeopleCount(0L)
                .interestCount(0L)
                .male(male)
                .avgStaringTime(0F)
                .hourlyPassedCount( new ArrayList<>(Collections.nCopies(24, 0L)))
                .hourlyInterestedCount( new ArrayList<>(Collections.nCopies(24, 0L)))
                .hourlyAvgStaringTime(new ArrayList<>(Collections.nCopies(24,0F)))
                .dailyMediaBoard(dailyMediaBoard)
                .build();
    }




}

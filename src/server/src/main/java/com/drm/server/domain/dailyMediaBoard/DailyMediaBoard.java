package com.drm.server.domain.dailyMediaBoard;

import com.drm.server.domain.detectedface.DataConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DailyMediaBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaDataId;

    // 전체 포착된 사람 수
    @Column
    private Long totalPeopleCount;

    // 시간당 관심을 표현한 사람 수 (0시 - 12시 까지)
    @Convert(converter = DataConverter.class)
    private List<Long> hourlyPassedCount;

    // 시간당 포착된 사람 수 (0시 - 12시 까지)
    @Convert(converter = DataConverter.class)
    private List<Long> hourlyInterestedCount;


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
}

package com.drm.server.domain.dailyMediaBoard;

import com.drm.server.domain.detectedface.DataConverter;
import com.drm.server.domain.mediaApplication.MediaApplication;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DailyMediaBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaDataId;

    @Column
    private LocalDate date;
    // 전체 포착된 사람 수
    @Column
    private Long totalPeopleCount;

    // 시간당 관심을 표현한 사람 수 (0시 - 12시 까지)
    @Convert(converter = DataConverter.class)
    private List<Long> hourlyPassedCount;

    // 시간당 포착된 사람 수 (0시 - 12시 까지)
    @Convert(converter = DataConverter.class)
    private List<Long> hourlyInterestedCount;

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
}

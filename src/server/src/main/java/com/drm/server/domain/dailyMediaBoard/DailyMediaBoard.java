package com.drm.server.domain.dailyMediaBoard;

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

    @Column
    private Long totalPeopleCount;
    private List<Long> hourlyPassedCount;
    private List<Long> hourlyInterestedCount;
    private float avgMale;
    private float avgAge;
}

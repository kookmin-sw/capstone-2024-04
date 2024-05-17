package com.drm.server.domain.dailyDetailBoard;

import com.drm.server.domain.dailyMediaBoard.DailyMediaBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DailyDetailBoardRepository extends JpaRepository< DailyDetailBoard,Long> {
    Optional<DailyDetailBoard> findByDailyMediaBoardAndAgeRangeAndMaleAndCreateDateBetween(DailyMediaBoard dailyMediaBoard, int ageRange,boolean male,LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<DailyDetailBoard> findByDailyMediaBoardAndAgeRangeAndMale(DailyMediaBoard dailyMediaBoard, int i, boolean male);
}

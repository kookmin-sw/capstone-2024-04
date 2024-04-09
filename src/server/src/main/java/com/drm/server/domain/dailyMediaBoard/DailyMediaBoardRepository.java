package com.drm.server.domain.dailyMediaBoard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyMediaBoardRepository extends JpaRepository<DailyMediaBoard, Long> {
    Optional<DailyMediaBoard> findByMediaIdAndDate(Long mediaId, LocalDate date);
}

package com.drm.server.domain.dailyMediaBoard;

import com.drm.server.domain.mediaApplication.MediaApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyMediaBoardRepository extends JpaRepository<DailyMediaBoard, Long> {

    Optional<DailyMediaBoard> findByMediaApplicationAndCreateDateBetween(MediaApplication application, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<DailyMediaBoard> findByMediaApplication(MediaApplication mediaApplication);

}

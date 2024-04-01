package com.drm.server.domain.mediaApplication;

import com.drm.server.domain.location.Location;
import com.drm.server.domain.media.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MediaApplicationRepository extends JpaRepository<MediaApplication,Long> {
    Optional<List<MediaApplication>> findByMedia(Media media);
    @Query("SELECT CASE WHEN COUNT(m.mediaApplicationId) > 0 THEN true ELSE false END FROM MediaApplication m " +
            "WHERE (:startDate BETWEEN m.startDate AND m.endDate " +
            "OR :endDate BETWEEN m.startDate AND m.endDate " +
            "OR m.startDate BETWEEN :startDate AND :endDate " +
            "OR m.endDate BETWEEN :startDate AND :endDate) " +
            "AND m.location = :location " +
            "AND m.status = 'ACCEPT'")
    boolean hasOverlappingApplications(@Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate,
                                       @Param("location") Location location);

    @Query("SELECT ma FROM MediaApplication ma " +
            "WHERE ma.status = 'ACCEPT' " +
            "AND :today BETWEEN ma.startDate AND ma.endDate")
    Optional<List<MediaApplication>> findAcceptedApplicationsForToday(
            @Param("today") LocalDate today
    );
}
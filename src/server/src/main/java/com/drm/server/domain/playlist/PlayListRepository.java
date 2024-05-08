package com.drm.server.domain.playlist;

import com.drm.server.domain.location.Location;
import com.drm.server.domain.mediaApplication.MediaApplication;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlayListRepository extends JpaRepository<PlayList,Long> {
    @Query("SELECT DISTINCT p.mediaApplication FROM PlayList p WHERE p.location = :location AND DATE(p.createDate) = DATE(:createDate)")
    Optional<MediaApplication> findMediaApplicationsByLocationAndCreateDate(@Param("location") Location location, @Param("createDate") LocalDateTime createDate);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM PlayList p WHERE DATE(p.createDate) = DATE(:date) AND p.mediaApplication = :mediaApplications")
    boolean existsByCreateDateAndMediaApplications(@Param("date") LocalDateTime date, @Param("mediaApplications") MediaApplication mediaApplications);

    @Query("SELECT p FROM PlayList p WHERE p.location = :location AND DATE(p.createDate) = Date(:today)")
    Optional<List<PlayList>> findByLocationAndCreateDateContaining(@Param("location") Location location, @Param("today") LocalDateTime today);

    @Query("SELECT CASE WHEN COUNT(pl) > 0 THEN true ELSE false END " +
            "FROM PlayList pl " +
            "WHERE pl.location = :location " +
            "AND DATE(pl.createDate) = Date(:today)"+
            "AND pl.posting = true")
    boolean existsByLocationAndPostingIsTrue(@Param("location") Location location, @Param("today") LocalDateTime today);

    @Query("SELECT pl FROM PlayList pl " +
            "WHERE pl.location = :location " +
            "AND DATE(pl.createDate) = Date(:today) " +
            "AND pl.posting = false " +
            "ORDER BY pl.createDate ASC")
    Optional<List<PlayList>> findFirstFalseByLocationAndCreateDateOrderByCreateDateAsc(
            @Param("location") Location location,
            @Param("today") LocalDateTime today);
    @Query("SELECT pl FROM PlayList pl " +
            "WHERE DATE(pl.createDate) = Date(:today) " +
            "AND pl.posting = true " )
    List<PlayList> findByBroadCasting(@Param("today") LocalDateTime today);

}


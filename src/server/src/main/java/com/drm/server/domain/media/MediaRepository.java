package com.drm.server.domain.media;

import com.drm.server.domain.dashboard.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    Media findByTitle(String titleName);
    Boolean existsByTitle(String title);

    Media findByDashboard(Dashboard dashboard);
}

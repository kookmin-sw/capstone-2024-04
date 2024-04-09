package com.drm.server.domain.dashboard;

import com.drm.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    Optional<List<Dashboard>> findByUser(User user);
    List<Dashboard> findAllByUser(User user);

    boolean existsByTitle(String title);

    void deleteByTitle(String title);
}

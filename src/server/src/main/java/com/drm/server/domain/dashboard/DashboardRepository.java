package com.drm.server.domain.dashboard;

import com.drm.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    Optional<List<Dashboard>> findByUserOrderByCreateDateDesc(User user);
    Optional<List<Dashboard>> findAllByUser(User user);


}

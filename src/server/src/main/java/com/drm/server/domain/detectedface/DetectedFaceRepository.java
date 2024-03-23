package com.drm.server.domain.detectedface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DetectedFaceRepository extends JpaRepository<DetectedFace, Long> {
    DetectedFace findFirstByOrderByDetectedFaceIdDesc();
}

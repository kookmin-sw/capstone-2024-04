package com.drm.server.domain.detectedface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectedFaceRepository extends JpaRepository<DetectedFace, Long> {
    DetectedFace findFirstByOrderByDetectedFaceIdDesc();
}

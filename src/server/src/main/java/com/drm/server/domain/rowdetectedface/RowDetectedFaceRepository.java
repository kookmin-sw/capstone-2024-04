package com.drm.server.domain.rowdetectedface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RowDetectedFaceRepository extends JpaRepository<RowDetectedFace, Long> {
}

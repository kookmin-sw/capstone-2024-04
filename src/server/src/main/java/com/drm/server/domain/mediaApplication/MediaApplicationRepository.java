package com.drm.server.domain.mediaApplication;

import com.drm.server.domain.media.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MediaApplicationRepository extends JpaRepository<MediaApplication,Long> {
    Optional<List<MediaApplication>> findByMedia(Media media);
}

package com.drm.server.domain.mediaApplication;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.playlist.PlayList;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MediaApplication extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaApplicationId;

    @Column
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "media_id")
    private Media media;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;



    @OneToOne(mappedBy = "mediaApplication")
    private PlayList playList;



}

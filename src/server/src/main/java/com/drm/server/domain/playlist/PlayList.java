package com.drm.server.domain.playlist;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.domain.mediaApplication.MediaApplication;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PlayList extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playListId;

    @Column
    private boolean posting;
//    private int sequence;
//    private LocalTime postingTime;

    @OneToOne
    @JoinColumn(name = "media_application_id")
    private MediaApplication mediaApplication;

}

package com.drm.server.domain.mediaApplication;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.common.KoreaLocalDateTime;
import com.drm.server.domain.location.Location;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.playlist.PlayList;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "media_id")
    private Media media;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToOne(mappedBy = "mediaApplication")
    private PlayList playList;

    public static MediaApplication toEntity(String startDate,String endDate, Media media, Location location){
        return MediaApplication.builder()
                .startDate(KoreaLocalDateTime.stringToLocalDateTime(startDate))
                .endDate(KoreaLocalDateTime.stringToLocalDateTime(endDate))
                .media(media)
                .location(location)
                .status(Status.WAITING)
                .build();
    }


}

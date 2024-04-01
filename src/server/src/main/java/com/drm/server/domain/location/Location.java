package com.drm.server.domain.location;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.domain.admin.Admin;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.playlist.PlayList;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Location extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @Column
    private String address;
    private Long cameraId;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "location")
    private List<MediaApplication> mediaApplications = new ArrayList<>();

    @OneToMany(mappedBy = "location")
    private List<PlayList> playLists = new ArrayList<>();
}

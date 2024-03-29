package com.drm.server.domain.location;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.domain.admin.Admin;
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
}

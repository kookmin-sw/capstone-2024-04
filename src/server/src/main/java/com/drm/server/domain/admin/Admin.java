package com.drm.server.domain.admin;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.common.enums.Authority;
import com.drm.server.domain.location.Location;
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
public class Admin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Column
    private String email;
    private String password;
    private Authority authority;

    @OneToMany(mappedBy = "admin")
    private List<Location> locations = new ArrayList<>();

}

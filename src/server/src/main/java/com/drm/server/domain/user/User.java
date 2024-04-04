package com.drm.server.domain.user;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.common.enums.Authority;
import com.drm.server.domain.dashboard.Dashboard;
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
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String email;
    private String password;
    private String company;
    private boolean deleted;
    @Column
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToMany(mappedBy = "user",orphanRemoval = true)
    private List<Dashboard> dashboards = new ArrayList<>();
    public static User toEntity(String email,String password,String company){
        return User.builder().email(email).password(password).company(company).authority(Authority.USER).deleted(false).build();
    }

}

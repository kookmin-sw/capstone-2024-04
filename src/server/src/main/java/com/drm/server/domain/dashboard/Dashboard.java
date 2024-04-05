package com.drm.server.domain.dashboard;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.user.User;
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
public class Dashboard extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dashboardId;

    @Column
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "dashboard",orphanRemoval = true)
    private Media media;

    public static Dashboard toEntity(String title, String description, User user){
        return Dashboard.builder().title(title).description(description).user(user).build();
    }


}

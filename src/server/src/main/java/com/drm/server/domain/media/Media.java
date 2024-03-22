package com.drm.server.domain.media;

import com.drm.server.domain.dashboard.Dashboard;
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
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;

    @Column
    private String title;
    private String mediaUrl;

    @OneToOne
    @JoinColumn(name = "dashboard_id")
    private Dashboard dashboard;

}

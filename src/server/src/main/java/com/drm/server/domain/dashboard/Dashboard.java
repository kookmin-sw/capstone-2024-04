package com.drm.server.domain.dashboard;

import com.drm.server.common.BaseTimeEntity;
import com.drm.server.handler.LongConverter;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    @Convert(converter = LongConverter.class)
    private List<Long> avgHourlyPassedCount;
    private float avgStaringTime;
    private Long maleInterestCnt;
    // 관심을 표현한 여자의 인원 수
    private Long femaleInterestCnt;



    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "dashboard",orphanRemoval = true)
    private Media media;

    public static Dashboard toEntity( User user){
        return Dashboard.builder()
                .user(user)
                .avgHourlyPassedCount( new ArrayList<>(Collections.nCopies(24, 0L)))
                .maleInterestCnt(0L).femaleInterestCnt(0L)
                .avgStaringTime(0F)
                .build();
    }

    public void setMedia(Media media) {
        this.media = media;
    }



}

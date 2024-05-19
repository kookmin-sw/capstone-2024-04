package com.drm.server.controller.dto.response;

import lombok.*;

@Getter
@Setter
public class PlayListLog {
    private Long playlistId;
    private String startTime;
    private String endTime;
    private Long locationId;

    public PlayListLog(Long playlistId, String startTime, String endTime, Long locationId) {
        this.playlistId = playlistId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.locationId = locationId;
    }
}

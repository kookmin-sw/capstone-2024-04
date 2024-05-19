package com.drm.server.controller.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class PlayListRequest {
    @Getter
    @Setter
    @Builder
    public static class PlayListLog{

    }
    @Getter
    @Setter
    @Builder
    public static class Query{
        Long locationId;
        String arriveAt;
        String leaveAt;
    }
}

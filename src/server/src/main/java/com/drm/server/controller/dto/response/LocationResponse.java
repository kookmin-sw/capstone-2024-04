package com.drm.server.controller.dto.response;

import com.drm.server.domain.location.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class LocationResponse {
    @Getter
    @Setter
    @Schema(description = "디스플레이 장소 정보")
    public static class LocationInfo{
        private Long locationId;
        private String address;

        public LocationInfo(Location location) {
            this.locationId = location.getLocationId();
            this.address = location.getAddress();
        }
    }
}

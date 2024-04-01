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
        @Schema(description = "디스플레이 Id",example = "1")
        private Long locationId;
        @Schema(description = "디스플레이 상세 주소",example = "복지관 4층 복도")
        private String address;

        public LocationInfo(Location location) {
            this.locationId = location.getLocationId();
            this.address = location.getAddress();
        }
    }
}

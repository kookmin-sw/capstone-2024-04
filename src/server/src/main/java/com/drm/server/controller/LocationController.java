package com.drm.server.controller;

import com.drm.server.common.APIResponse;
import com.drm.server.common.enums.SuccessCode;
import com.drm.server.controller.dto.response.LocationResponse;
import com.drm.server.service.LocationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/locations")
@RequiredArgsConstructor
@Tag(name = "Location")
public class LocationController {
    private final LocationService locationService;
    @GetMapping()
    public ResponseEntity<APIResponse<List<LocationResponse.LocationInfo>>> getLocations(){
        List<LocationResponse.LocationInfo> locationInfos = locationService.getLocations();
        APIResponse response = APIResponse.of(SuccessCode.SELECT_SUCCESS, locationInfos);
        return ResponseEntity.ok(response);
    }

}

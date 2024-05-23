package com.drm.server.service;

import com.drm.server.controller.dto.response.LocationResponse;
import com.drm.server.domain.location.Location;
import com.drm.server.domain.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    public Location findById(Long locationId){
        return locationRepository.findById(locationId).orElseThrow(() -> new IllegalArgumentException("Invalid locationId"));
    }
    public List<LocationResponse.LocationInfo> getLocations(){
        List<Location> locations = locationRepository.findAll();
        List<LocationResponse.LocationInfo> locationInfos = locations.stream().map(LocationResponse.LocationInfo::new).collect(Collectors.toList());
        return locationInfos;
    }

    public Location createLocation(int cameraNum, String addressStr){
        Location location = Location.builder().address(addressStr).cameraId(cameraNum).build();
        return locationRepository.save(location);
    }
    public Location findByCameraId(int cameraId){
        return locationRepository.findByCameraId(cameraId).orElseThrow(() -> new IllegalArgumentException("cannot find location by cameraId"));
    }

}

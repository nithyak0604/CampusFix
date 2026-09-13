package com.campusfix.dto;

import com.campusfix.entity.Location;

public record LocationResponse(Long id, String building, String block, String floor, String room, String details) {
    public static LocationResponse of(Location location) {
        return new LocationResponse(location.getId(), location.getBuilding(), location.getBlock(), location.getFloor(), location.getRoom(), location.getDetails());
    }
}
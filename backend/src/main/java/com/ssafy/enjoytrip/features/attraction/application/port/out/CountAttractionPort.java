package com.ssafy.enjoytrip.features.attraction.application.port.out;

public interface CountAttractionPort {
    Long countAttractions(Long contentTypeId, Long areaCode, Long siGunGuCode);
}

package com.ssafy.enjoytrip.features.attraction.application.port.out;

import com.ssafy.enjoytrip.features.attraction.domain.Attraction;

import java.util.List;

public interface SearchAttractionPort {
    List<Attraction> searchAttractionsWithPaging(Long contentTypeId, Long areaCode, Long siGunGuCode, Integer page);
    List<Attraction> searchAttractionsWithLimit(Long contentTypeId, Long areaCode, Long siGunGuCode, Long limit);
}

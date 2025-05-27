package com.ssafy.enjoytrip.features.attraction.application.port.out;

import com.ssafy.enjoytrip.features.attraction.domain.Attraction;
import com.ssafy.enjoytrip.features.attraction.domain.AttractionId;

import java.util.List;

public interface SearchAttractionPort {
    List<Attraction> searchAttractionsWithPaging(Long contentTypeId, Long areaCode, Long siGunGuCode, Integer page, Integer pageSize, String query);
    List<Attraction> searchAttractionsWithLimit(Long contentTypeId, Long areaCode, Long siGunGuCode, Long limit);
    List<Attraction> searchAttractionByIds(List<AttractionId> ids);
}

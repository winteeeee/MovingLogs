package com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.attraction.domain.Attraction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AttractionDao {
    List<Attraction> findByContentByIdAndAreaCodeAndSiGunGuCode(Long contentTypeId, Long areaCode, Long siGunGuCode, Integer limit, Long offset);
    List<Attraction> findByContentByIdAndAreaCodeAndSiGunGuCodeLimit(Long contentTypeId, Long areaCode, Long siGunGuCode, Long limit);
    Long countByContentByIdAndAreaCodeAndSiGunGuCode(Long contentTypeId, Long areaCode, Long siGunGuCode);
}

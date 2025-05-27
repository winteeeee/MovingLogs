package com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AttractionDao {
    List<Map<String, Object>> findByContentByIdAndAreaCodeAndSiGunGuCode(Long contentTypeId, Long areaCode, Long siGunGuCode, Integer limit, Long offset, String query);
    List<Map<String, Object>> findByContentByIdAndAreaCodeAndSiGunGuCodeLimit(Long contentTypeId, Long areaCode, Long siGunGuCode, Long limit);
    Long countByContentByIdAndAreaCodeAndSiGunGuCode(Long contentTypeId, Long areaCode, Long siGunGuCode, String query);
    List<Map<String, Object>> findByIds(List<String> ids);
}

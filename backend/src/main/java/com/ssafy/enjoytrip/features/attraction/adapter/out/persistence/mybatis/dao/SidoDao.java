package com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SidoDao {
    List<Map<String, Object>> findAll();
}

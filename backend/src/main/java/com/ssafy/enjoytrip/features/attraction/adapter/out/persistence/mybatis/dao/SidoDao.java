package com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.attraction.domain.Sido;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SidoDao {
    List<Sido> findAll();
}

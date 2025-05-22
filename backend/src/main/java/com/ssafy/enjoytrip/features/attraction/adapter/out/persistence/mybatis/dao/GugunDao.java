package com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.attraction.domain.Gugun;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GugunDao {
    List<Gugun> findBySidoCode(Long sidoCode);
}

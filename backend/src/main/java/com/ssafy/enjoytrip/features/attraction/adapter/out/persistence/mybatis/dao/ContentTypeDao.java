package com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.attraction.domain.ContentType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContentTypeDao {
    List<ContentType> findAll();
}

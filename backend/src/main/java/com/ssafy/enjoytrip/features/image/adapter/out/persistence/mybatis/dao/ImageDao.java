package com.ssafy.enjoytrip.features.image.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.image.domain.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ImageDao {
    int create(@Param("image") Image image);
}

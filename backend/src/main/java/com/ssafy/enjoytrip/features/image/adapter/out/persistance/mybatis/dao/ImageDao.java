package com.ssafy.enjoytrip.features.image.adapter.out.persistance.mybatis.dao;

import com.ssafy.enjoytrip.features.image.domain.Image;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageDao {
    int upsert(Image image);
}

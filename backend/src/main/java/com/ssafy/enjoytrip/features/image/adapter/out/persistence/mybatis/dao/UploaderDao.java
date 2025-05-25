package com.ssafy.enjoytrip.features.image.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.user.domain.Uid;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface UploaderDao {
    Map<String, Object> findById(@Param("uid") Uid uid);
}

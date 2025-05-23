package com.ssafy.enjoytrip.features.image.adapter.out.persistance.mybatis.dao;

import com.ssafy.enjoytrip.features.user.domain.Uid;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface AuthorDao {
    Map<String, Object> findById(Uid uid);
}

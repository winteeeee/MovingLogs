package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.user.domain.Uid;

import java.util.Map;

public interface AuthorDao {
    Map<String, Object> findById(Uid uid);
}

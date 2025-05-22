package com.ssafy.enjoytrip.features.user.adapter.out.persistence.mybatis.dao;

import com.ssafy.enjoytrip.features.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
	User findById(String id);
	int insert(User user);
	int update(User user);
	int delete(User user);
}

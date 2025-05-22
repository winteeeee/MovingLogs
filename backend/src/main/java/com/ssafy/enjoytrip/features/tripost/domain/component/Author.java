package com.ssafy.enjoytrip.features.tripost.domain.component;

import com.ssafy.enjoytrip.features.user.domain.Uid;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Author {
	private Uid uid;
	private String nickname;
}

package com.ssafy.enjoytrip.features.attraction.domain;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class Gugun {
	private GugunId id;
	private Long sidoCode;
	private Long gugunCode;
	private String gugunName;
}

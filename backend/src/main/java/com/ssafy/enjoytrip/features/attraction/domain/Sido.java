package com.ssafy.enjoytrip.features.attraction.domain;


import lombok.*;


@Getter
@Builder
@AllArgsConstructor
public class Sido {
	private SidoId id;
	private Long sidoCode;
	private String sidoName;
}

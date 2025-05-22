package com.ssafy.enjoytrip.features.attraction.adapter.in.web.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class AttractionResponse {
	private String id;
	private Long contentId;
	private String title;
	private String contentTypeName;
	private String sidoName;
	private String gugunName;
	private String firstImage1;
	private String firstImage2;
	private Long mapLevel;
	private Double latitude;
	private Double longitude;
	private String tel;
	private String addr1;
	private String addr2;
	private String homepage;
	private String overview;
}

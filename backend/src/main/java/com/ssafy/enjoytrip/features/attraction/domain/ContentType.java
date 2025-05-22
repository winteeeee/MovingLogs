package com.ssafy.enjoytrip.features.attraction.domain;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class ContentType {
	private ContentTypeId id;
	private String contentTypeName;
}

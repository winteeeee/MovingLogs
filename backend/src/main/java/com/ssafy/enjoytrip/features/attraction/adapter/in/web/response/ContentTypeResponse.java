package com.ssafy.enjoytrip.features.attraction.adapter.in.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ContentTypeResponse {
    private Long contentTypeId;
    private String contentTypeName;
}

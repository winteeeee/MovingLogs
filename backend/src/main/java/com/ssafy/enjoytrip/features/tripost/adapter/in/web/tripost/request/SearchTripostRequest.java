package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchTripostRequest {
    // TODO: 검증 로직
    private String type;
    private String query;
    private Integer page;
    private Integer size;
}

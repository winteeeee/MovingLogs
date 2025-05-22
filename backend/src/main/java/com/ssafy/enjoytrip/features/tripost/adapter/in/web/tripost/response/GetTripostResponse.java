package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.response;

import com.ssafy.enjoytrip.features.tripost.application.dto.TripostDetailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class GetTripostResponse {
    private TripostDetailDto tripost;
}

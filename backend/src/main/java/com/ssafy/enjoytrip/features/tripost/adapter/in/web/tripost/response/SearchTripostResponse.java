package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.response;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostListItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SearchTripostResponse {
    PageDto<TripostListItemDto> result;
}

package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.response;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostCommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
@AllArgsConstructor
public class SearchTripostCommentResponse {
    private PageDto<TripostCommentDto> result;
}

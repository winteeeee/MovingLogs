package com.ssafy.enjoytrip.features.tripost.application.port.in;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostCommentDto;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface SearchTripostCommentPagedQuery {
    PageDto<TripostCommentDto> getComments(Query query);

    @Getter
    @Builder
    @AllArgsConstructor
    class Query {
        private Uid uid;
        private TripostId tripostId;
        private Integer page;
        private Integer size;
    }
}

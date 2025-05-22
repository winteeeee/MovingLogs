package com.ssafy.enjoytrip.features.tripost.application.port.out;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostCommentDto;
import com.ssafy.enjoytrip.features.tripost.domain.TripostComment;
import com.ssafy.enjoytrip.features.tripost.domain.TripostCommentId;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;

import java.util.Optional;

public interface TripostCommentPort {
    TripostComment save(TripostComment tripostComment);
    Optional<TripostComment> getTripostComment(TripostCommentId tripostCommentId);
    Optional<PageDto<TripostCommentDto>> getPagedTripostCommentDto(TripostId tripostId, int page, int size);
}

package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.request.CreateTripostCommentRequest;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.request.SearchTripostCommentRequest;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.response.CreateTripostCommentResponse;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.response.DeleteTripostCommentResponse;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.response.SearchTripostCommentResponse;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostCommentDto;
import com.ssafy.enjoytrip.features.tripost.application.port.in.CreateTripostCommentUseCase;
import com.ssafy.enjoytrip.features.tripost.application.port.in.DeleteTripostCommentUseCase;
import com.ssafy.enjoytrip.features.tripost.application.port.in.SearchTripostCommentPagedQuery;
import com.ssafy.enjoytrip.features.tripost.domain.TripostCommentId;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.user.domain.Uid;

public class TripostCommentMapper {
    public static SearchTripostCommentPagedQuery.Query toSearchTripostCommentPagedQuery(String tripostId, String uid, SearchTripostCommentRequest request) {
        return SearchTripostCommentPagedQuery.Query.builder()
                .uid(new Uid(uid))
                .tripostId(new TripostId(tripostId))
                .page(request.getPage())
                .size(request.getSize())
                .build();
    }

    public static SearchTripostCommentResponse toSearchTripostCommentResponse(PageDto<TripostCommentDto> dto) {
        return SearchTripostCommentResponse.builder()
                .result(dto)
                .build();
    }

    public static CreateTripostCommentUseCase.Command toCreateTripostCommentCommend(String uid, CreateTripostCommentRequest request) {
        return CreateTripostCommentUseCase.Command.builder()
                .parentId(new TripostCommentId(request.getParentId()))
                .tripostId(new TripostId(request.getTripostId()))
                .uid(new Uid(uid))
                .content(request.getContent())
                .build();
    }

    public static CreateTripostCommentResponse toCreateTripostCommentResponse(CreateTripostCommentUseCase.Result result) {
        return CreateTripostCommentResponse.builder()
                .id(result.getId().getId())
                .authorName(result.getAuthorName())
                .content(result.getContent())
                .createdAt(result.getCreatedAt())
                .isAuthor(result.getIsAuthor())
                .build();
    }

    public static DeleteTripostCommentUseCase.Command toDeleteTripostCommentCommand(String uid, String tripostCommentId) {
        return DeleteTripostCommentUseCase.Command.builder()
                .tripostCommentId(new TripostCommentId(tripostCommentId))
                .uid(new Uid(uid))
                .build();
    }

    public static DeleteTripostCommentResponse toDeleteTripostCommentResponse(DeleteTripostCommentUseCase.Result result) {
        return DeleteTripostCommentResponse.builder()
                .deleted(result.isDeleted())
                .build();
    }
}

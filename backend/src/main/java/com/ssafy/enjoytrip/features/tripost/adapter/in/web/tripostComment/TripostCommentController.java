package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.common.util.SecurityUtils;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.TripostMapper;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.request.CreateTripostCommentRequest;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.request.SearchTripostCommentRequest;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.response.CreateTripostCommentResponse;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.response.DeleteTripostCommentResponse;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.response.SearchTripostCommentResponse;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostCommentDto;
import com.ssafy.enjoytrip.features.tripost.application.port.in.CreateTripostCommentUseCase;
import com.ssafy.enjoytrip.features.tripost.application.port.in.DeleteTripostCommentUseCase;
import com.ssafy.enjoytrip.features.tripost.application.port.in.SearchTripostCommentPagedQuery;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${api.prefix}/${api.version}/triposts/{tripost_id}/comments")
@RequiredArgsConstructor
@Tag(name = "TripostCommentController", description = "여행 게시글 댓글 관련 API 제공")
public class TripostCommentController {
    private final CreateTripostCommentUseCase createTripostCommentUseCase;
    private final DeleteTripostCommentUseCase deleteTripostCommentUseCase;

    private final SearchTripostCommentPagedQuery searchTripostCommentPagedQuery;

    @GetMapping
    @Operation(summary = "댓글 리스트 조회", description = "페이지, 한 페이지 당 개수에 따라 페이지 단위의 댓글 리스트를 조회한다.")
    public ResponseEntity<SearchTripostCommentResponse> searchComments(
            @PathVariable("tripost_id") String tripostId,
            @ModelAttribute SearchTripostCommentRequest request) {
        String uid = SecurityUtils.getUserUidBySecurityContextHolder();

        SearchTripostCommentPagedQuery.Query query = TripostCommentMapper.toSearchTripostCommentPagedQuery(tripostId, uid, request);
        PageDto<TripostCommentDto> dto = searchTripostCommentPagedQuery.getComments(query);
        SearchTripostCommentResponse response = TripostCommentMapper.toSearchTripostCommentResponse(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "댓글 생성", description = "새 댓글을 생성한다.")
    public ResponseEntity<CreateTripostCommentResponse> createTripostComment(
            @RequestBody CreateTripostCommentRequest request) {
        String uid = SecurityUtils.getUserUidBySecurityContextHolder();

        CreateTripostCommentUseCase.Command command = TripostCommentMapper.toCreateTripostCommentCommend(uid, request);
        CreateTripostCommentUseCase.Result result = createTripostCommentUseCase.createComment(command);
        CreateTripostCommentResponse response = TripostCommentMapper.toCreateTripostCommentResponse(result);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{tripost_comment_id}")
    @Operation(summary = "댓글 생성", description = "새 댓글을 생성한다.")
    public ResponseEntity<DeleteTripostCommentResponse> deleteTripostComment(
            @PathVariable("tripost_id") String tripostId,
            @PathVariable("tripost_comment_id") String tripostCommentId) {
        String uid = SecurityUtils.getUserUidBySecurityContextHolder();

        DeleteTripostCommentUseCase.Command command = TripostCommentMapper.toDeleteTripostCommentCommand(uid, tripostCommentId);
        DeleteTripostCommentUseCase.Result result = deleteTripostCommentUseCase.deleteComment(command);
        DeleteTripostCommentResponse response = TripostCommentMapper.toDeleteTripostCommentResponse(result);
        return ResponseEntity.ok(response);
    }
}

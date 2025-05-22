//package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment;
//
//import com.ssafy.enjoytrip.common.dto.PageDto;
//import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.TripostMapper;
//import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.request.SearchTripostCommentRequest;
//import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostComment.response.SearchTripostCommentResponse;
//import com.ssafy.enjoytrip.features.tripost.application.dto.TripostCommentDto;
//import com.ssafy.enjoytrip.features.tripost.application.port.in.CreateTripostCommentUseCase;
//import com.ssafy.enjoytrip.features.tripost.application.port.in.DeleteTripostCommentUseCase;
//import com.ssafy.enjoytrip.features.tripost.application.port.in.SearchTripostCommentPagedQuery;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/${api.prefix}/${api.version}/triposts/{tripost_id}/comments")
//@RequiredArgsConstructor
//@Tag(name = "TripostCommentController", description = "여행 게시글 댓글 관련 API 제공")
//public class TripostCommentController {
//    private final CreateTripostCommentUseCase createTripostCommentUseCase;
//    private final DeleteTripostCommentUseCase deleteTripostCommentUseCase;
//
//    private final SearchTripostCommentPagedQuery searchTripostCommentPagedQuery;
//
//    @GetMapping
//    @Operation(summary = "댓글 리스트 조회", description = "페이지, 한 페이지 당 개수에 따라 페이지 단위의 댓글 리스트를 조회한다.")
//    public ResponseEntity<SearchTripostCommentResponse> searchComments(
//            @ModelAttribute SearchTripostCommentRequest request) {
//        SearchTripostCommentPagedQuery.Query query = TripostCommentMapper.toSearchTripostCommentPagedQuery(request);
//        PageDto<TripostCommentDto> dto = searchTripostCommentPagedQuery.getComments(query);
//        SearchTripostCommentResponse response = TripostCommentMapper.toSearchTripostCommentResponse(dto);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping
//    @Operation(summary = "댓글 생성", description = "새 댓글을 생성한다.")
//    public ResponseEntity<CreateTripostCommentResponse> createTripostComment(
//            @RequestBody CreateTripostCommentRequest request) {
//        // TODO: 유저정보 시큐리티에게 받기
//        String uid = "1234";
//
//        CreateTripostCommentUseCase.Command command = TripostMapper.toCreateTripostCommentCommend(request);
//        return null;
//    }
//}

package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostLike;

import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostLike.response.CheckTripostLikedResponse;
import com.ssafy.enjoytrip.features.tripost.application.port.in.TripostLikeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/${api.prefix}/${api.version}/triposts/{tripost_id}/likes")
@RequiredArgsConstructor
@Tag(name = "TripostLikeController", description = "여행 게시글 좋아요 관련 API제공")
public class TripostLikeController {
    private final TripostLikeUseCase tripostLikeUseCase;

    @PostMapping
    @Operation(summary = "좋아요", description = "현재 사용자의 대상 여행 게시글에 좋아요 처리를 한다.")
    public ResponseEntity<Void> likeTripost(
            @PathVariable("tripost_id") String tripostId) {
        // TODO: 유저정보 시큐리티에게 받기
        String uid = "1234";

        TripostLikeUseCase.Command command = TripostLikeMapper.toTripostLikeCommand(tripostId, uid);
        tripostLikeUseCase.like(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(summary = "좋아요 취소", description = "현재 사용자의 대상 여행 게시글에 좋아요 처리를 취소한다.")
    public ResponseEntity<Void> unlikeTripost(
            @PathVariable("tripost_id") String tripostId) {
        // TODO: 유저정보 시큐리티에게 받기
        String uid = "1234";

        TripostLikeUseCase.Command command = TripostLikeMapper.toTripostLikeCommand(tripostId, uid);
        tripostLikeUseCase.unlike(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "좋아요 여부 조회", description = "현재 사용자가 대상 여행 게시글에 좋아요 처리여부를 조회한다.")
    public ResponseEntity<CheckTripostLikedResponse> checkTripostLiked(
            @PathVariable("tripost_id") String tripostId) {
        // TODO: 유저정보 시큐리티에게 받기
        String uid = "1234";

        TripostLikeUseCase.Command command = TripostLikeMapper.toTripostLikeCommand(tripostId, uid);
        boolean liked = tripostLikeUseCase.checkLiked(command);
        CheckTripostLikedResponse response = TripostLikeMapper.toCheckTripostLikedResponse(liked);
        return ResponseEntity.ok(response);
    }
}

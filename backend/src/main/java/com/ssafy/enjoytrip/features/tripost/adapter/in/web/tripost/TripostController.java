package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.common.util.SecurityUtils;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.request.*;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.response.*;
import com.ssafy.enjoytrip.features.tripost.application.dto.HotTripostDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.LatestTripostDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostDetailDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostListItemDto;
import com.ssafy.enjoytrip.features.tripost.application.port.in.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${api.prefix}/${api.version}/triposts")
@RequiredArgsConstructor
@Tag(name = "TripostController", description = "여행 게시글 관련 API 제공")
public class TripostController {
    private final CreateTripostUseCase createTripostUseCase;
    private final UpdateTripostUseCase updateTripostUseCase;
    private final DeleteTripostUseCase deleteTripostUseCase;
    private final SearchLatestTripostUseCase searchLatestTripostUseCase;
    private final SearchHotTripostUseCase searchHotTripostUseCase;

    private final GetTripostByIdQuery getTripostByIdQuery;
    private final SearchTripostPagedQuery searchTripostPagedQuery;

    @GetMapping
    @Operation(summary = "여행 게시글 리스트 조회", description = "페이지, 한 페이지의 글 개수, 검색어에 따라 페이지 단위의 여행 게시글 리스트를 조회한다.")
    public ResponseEntity<SearchTripostResponse> searchTriposts(
            @ModelAttribute SearchTripostRequest request) {
        SearchTripostPagedQuery.Query query = TripostMapper.toSearchTripostPagedQuery(request);
        PageDto<TripostListItemDto> dto = searchTripostPagedQuery.searchTriposts(query);
        SearchTripostResponse response = TripostMapper.toSearchTripostResponse(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{tripost_id}")
    @Operation(summary = "여행 게시글 조회", description = "여행 게시글을 조회한다.")
    public ResponseEntity<GetTripostResponse> getTripost(
            @PathVariable("tripost_id") String tripostId) {
        String uid = SecurityUtils.getUserUidBySecurityContextHolder();

        GetTripostByIdQuery.Query query = TripostMapper.toGetTripostByIdQuery(tripostId);
        TripostDetailDto dto = getTripostByIdQuery.findById(query);
        GetTripostResponse response = TripostMapper.toGetTripostResponse(uid, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/latest")
    @Operation(summary = "최신 여행 게시글 리스트 조회", description = "최신 여행 게시글 리스트를 조회한다")
    public ResponseEntity<List<SearchLatestTripostResponse>> getLatestTriposts(@ModelAttribute SearchLatestTripostRequest request) {
        SearchLatestTripostUseCase.Command command = TripostMapper.toSearchLatestTripostUseCaseCommand(request);
        List<LatestTripostDto> dto = searchLatestTripostUseCase.searchLatestTriposts(command);
        List<SearchLatestTripostResponse> response = TripostMapper.toSearchLatestTripostResponseList(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hot")
    @Operation(summary = "인기 여행 게시글 리스트 조회", description = "인기 여행 게시글 리스트를 조회한다.")
    public ResponseEntity<List<SearchHotTripostResponse>> getHotTriposts(@ModelAttribute SearchHotTripostRequest request) {
        SearchHotTripostUseCase.Command command = TripostMapper.toSearchHotTripostUseCaseCommand(request);
        List<HotTripostDto> dto = searchHotTripostUseCase.searchHotTriposts(command);
        List<SearchHotTripostResponse> response =  TripostMapper.toSearchHotTripostResponseList(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "여행 게시글 생성", description = "새 여행 게시글을 생성한다.")
    public ResponseEntity<CreateTripostResponse> createTripost(
            @RequestBody CreateTripostRequest request) {
        String uid = SecurityUtils.getUserUidBySecurityContextHolder();

        CreateTripostUseCase.Command command = TripostMapper.toCreateTripostCommand(uid, request);
        CreateTripostUseCase.Result result = createTripostUseCase.createTripost(command);
        CreateTripostResponse response = TripostMapper.toCreateTripostResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{tripost_id}")
    @Operation(summary = "여행 게시글 수정", description = "기존 여행 게시글을 수정한다.")
    public ResponseEntity<UpdateTripostResponse> updateTripost(
            @PathVariable("tripost_id") String tripostId,
            @RequestBody UpdateTripostRequest request) {
        String uid = SecurityUtils.getUserUidBySecurityContextHolder();

        UpdateTripostUseCase.Command command = TripostMapper.toUpdateTripostCommand(uid, tripostId, request);
        UpdateTripostUseCase.Result result = updateTripostUseCase.updateTripost(command);
        UpdateTripostResponse response = TripostMapper.toUpdateTripostResponse(result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{tripost_id}")
    @Operation(summary = "여행 게시글 삭제", description = "기존 여행 게시글을 삭제한다.")
    public ResponseEntity<DeleteTripostResponse> deleteTripost(
            @PathVariable("tripost_id") String tripostId) {
        String uid = SecurityUtils.getUserUidBySecurityContextHolder();

        DeleteTripostUseCase.Command command = TripostMapper.toDeleteTripostCommand(uid, tripostId);
        DeleteTripostUseCase.Result result = deleteTripostUseCase.deleteTripost(command);
        DeleteTripostResponse response = TripostMapper.toDeleteTripostResponse(result);
        return ResponseEntity.ok(response);
    }
}

package com.ssafy.enjoytrip.features.attraction.adapter.in.web;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.attraction.adapter.in.web.response.AttractionResponse;
import com.ssafy.enjoytrip.features.attraction.adapter.in.web.response.ContentTypeResponse;
import com.ssafy.enjoytrip.features.attraction.adapter.in.web.response.GugunResponse;
import com.ssafy.enjoytrip.features.attraction.adapter.in.web.response.SidoResponse;
import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchAttractionUseCase;
import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchContentTypeUseCase;
import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchGugunUseCase;
import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchSidoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/${api.prefix}/${api.version}/attractions")
@RequiredArgsConstructor
@Tag(name = "AttractionController", description = "회원 관련 API 제공")
public class AttractionController {
    private final SearchAttractionUseCase searchAttractionUseCase;
    private final SearchContentTypeUseCase searchContentTypeUseCase;
    private final SearchGugunUseCase searchGugunUseCase;
    private final SearchSidoUseCase searchSidoUseCase;

    @GetMapping("/content-types")
    @Operation(summary = "관광지 컨텐츠 타입 조회", description = "관광지의 컨텐츠 타입 조회")
    public ResponseEntity<List<ContentTypeResponse>> findContentTypes() {
        return ResponseEntity.ok(
                AttractionControllerMapper.toContentTypeResponseList(searchContentTypeUseCase.searchContentTypes())
        );
    }

    @GetMapping("/sidos")
    @Operation(summary = "관광지 지역 조회", description = "관광지의 지역 조회")
    public ResponseEntity<List<SidoResponse>> findSidos() {
        return ResponseEntity.ok(
            AttractionControllerMapper.toSidoResponseList(searchSidoUseCase.searchSidos())
        );
    }

    @GetMapping("/guguns")
    @Operation(summary = "관광지 구군 조회", description = "관광지의 구군 조회")
    public ResponseEntity<List<GugunResponse>> findGuguns(@RequestParam Long sidoCode) {
        return ResponseEntity.ok(
                AttractionControllerMapper.toGuGunResponseList(searchGugunUseCase.searchGuguns(sidoCode))
        );
    }

    @GetMapping
    @Operation(summary = "관광지 조회", description = "지정한 조건에 맞는 관광지 조회")
    public ResponseEntity<PageDto<AttractionResponse>> findAttractions(@RequestParam(required = false) Long contentTypeId,
                                                                       @RequestParam(required = false) Long areaCode,
                                                                       @RequestParam(required = false) Long siGunGuCode,
                                                                       @RequestParam Integer page,
                                                                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return ResponseEntity.ok(
                AttractionControllerMapper.toAttractionResponsePageDto(searchAttractionUseCase.searchAttractions(contentTypeId, areaCode, siGunGuCode, page, pageSize))
        );
    }
}
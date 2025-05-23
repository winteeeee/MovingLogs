package com.ssafy.enjoytrip.features.attraction.application;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchAttractionUseCase;
import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchContentTypeUseCase;
import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchGugunUseCase;
import com.ssafy.enjoytrip.features.attraction.application.port.in.SearchSidoUseCase;
import com.ssafy.enjoytrip.features.attraction.application.port.out.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttractionService implements SearchAttractionUseCase, SearchContentTypeUseCase, SearchGugunUseCase, SearchSidoUseCase {
    private final SearchAttractionPort searchAttractionPort;
    private final CountAttractionPort countAttractionPort;
    private final SearchContentTypePort searchContentTypePort;
    private final SearchGugunPort searchGugunPort;
    private final SearchSidoPort searchSidoPort;

    @Override
    @Transactional(readOnly = true)
    public PageDto<SearchAttractionUseCase.Result> searchAttractions(Long contentTypeId, Long areaCode, Long siGunGuCode, Integer page, Integer pageSize) {
        List<SearchAttractionUseCase.Result> content = AttractionServiceMapper.toSearchAttractionUseCaseResultList(
                searchAttractionPort.searchAttractionsWithPaging(contentTypeId, areaCode, siGunGuCode, page, pageSize)
        );
        Long totalElements = countAttractionPort.countAttractions(contentTypeId, areaCode, siGunGuCode);
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PageDto<>(
                content,
                page,
                pageSize,
                totalPages,
                totalElements,
                page > 0,
                page + 1 < totalPages
        );
    }

    @Override
    public List<SearchContentTypeUseCase.Result> searchContentTypes() {
        return AttractionServiceMapper.toSearchContentTypeUseCaseResultList(
                searchContentTypePort.searchContentTypes()
        );
    }

    @Override
    public List<SearchGugunUseCase.Result> searchGuguns(Long sidoCode) {
        return AttractionServiceMapper.toSearchGugunUseCaseResultList(searchGugunPort.searchGuguns(sidoCode));
    }

    @Override
    public List<SearchSidoUseCase.Result> searchSidos() {
        return AttractionServiceMapper.toSearchSidoUseCaseResultList(searchSidoPort.searchSidos());
    }
}

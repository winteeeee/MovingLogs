package com.ssafy.enjoytrip.features.tripost.application.port.out;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.MainPageTripostDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostDetailDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostListItemDto;
import com.ssafy.enjoytrip.features.tripost.application.port.in.SearchTripostPagedQuery;
import com.ssafy.enjoytrip.features.tripost.domain.Tripost;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;

import java.util.List;
import java.util.Optional;

public interface TripostPort {
    Tripost save(Tripost tripost);
    Optional<Tripost> getTripost(TripostId tripostId);
    int updateAllViewCount(List<TripostViewCount> tripostViewCounts);
    int recountAllLikeCount(List<TripostId> tripostIds);
    int recountCommentCount(TripostId tripostId);
    Optional<TripostDetailDto> getTripostDetailDto(TripostId tripostId);
    Optional<PageDto<TripostListItemDto>> getPagedTripostListItemDto(SearchTripostPagedQuery.QueryType type, String query, int page, int size);
    Optional<List<MainPageTripostDto>> getLatestTripostDto(Integer size);

    record TripostViewCount(TripostId tripostId, Integer viewCount) {  }
}

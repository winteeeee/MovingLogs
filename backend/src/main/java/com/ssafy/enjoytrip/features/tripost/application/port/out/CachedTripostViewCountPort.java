package com.ssafy.enjoytrip.features.tripost.application.port.out;

import com.ssafy.enjoytrip.features.tripost.domain.TripostId;

import java.util.List;
import java.util.Optional;

public interface CachedTripostViewCountPort {
    Optional<Long> createViewCount(TripostId tripostId, Long viewCount);
    Optional<Long> incrementViewCount(TripostId tripostId);
    List<TripostPort.TripostViewCount> popAllCachedViewCount();
    List<Optional<Long>> getViewCount(List<TripostId> tripostIds);
}

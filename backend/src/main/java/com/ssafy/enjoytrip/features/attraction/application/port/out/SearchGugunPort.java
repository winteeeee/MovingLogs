package com.ssafy.enjoytrip.features.attraction.application.port.out;

import com.ssafy.enjoytrip.features.attraction.domain.Gugun;

import java.util.List;

public interface SearchGugunPort {
    List<Gugun> searchGuguns(Long sidoCode);
}

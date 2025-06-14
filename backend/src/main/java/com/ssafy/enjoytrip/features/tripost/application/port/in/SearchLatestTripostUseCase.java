package com.ssafy.enjoytrip.features.tripost.application.port.in;

import com.ssafy.enjoytrip.features.tripost.application.dto.LatestTripostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public interface SearchLatestTripostUseCase {
    List<LatestTripostDto> searchLatestTriposts(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private Integer size;
    }
}

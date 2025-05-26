package com.ssafy.enjoytrip.features.tripost.application.port.in;

import com.ssafy.enjoytrip.features.tripost.application.dto.HotTripostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public interface SearchHotTripostUseCase {
    List<HotTripostDto> searchHotTriposts(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private Integer size;
    }
}

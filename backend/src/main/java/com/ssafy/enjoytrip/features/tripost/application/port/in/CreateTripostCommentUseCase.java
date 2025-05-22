package com.ssafy.enjoytrip.features.tripost.application.port.in;

import com.ssafy.enjoytrip.features.tripost.domain.TripostCommentId;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public interface CreateTripostCommentUseCase {
    Result createComment(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private TripostId tripostId;
        private Uid uid;
        private String content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    class Result {
        private TripostCommentId id;
    }
}

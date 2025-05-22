package com.ssafy.enjoytrip.features.tripost.application.port.in;

import com.ssafy.enjoytrip.features.tripost.domain.TripostCommentId;
import com.ssafy.enjoytrip.features.user.domain.Role;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


public interface DeleteTripostCommentUseCase {
    Result deleteComment(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private Uid uid;
        private Role role;
        private TripostCommentId tripostCommentId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    class Result {
        private boolean deleted;
    }
}

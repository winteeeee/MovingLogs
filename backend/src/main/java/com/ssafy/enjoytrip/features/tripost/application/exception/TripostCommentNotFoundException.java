package com.ssafy.enjoytrip.features.tripost.application.exception;

import com.ssafy.enjoytrip.features.tripost.domain.TripostCommentId;

public class TripostCommentNotFoundException extends RuntimeException {
    public TripostCommentNotFoundException(TripostCommentId tripostCommentId) {
        super("TripostComment not found [id: " + tripostCommentId.getId() + "]");
    }
}

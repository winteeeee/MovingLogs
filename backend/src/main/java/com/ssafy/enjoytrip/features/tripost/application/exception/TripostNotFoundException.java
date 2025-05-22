package com.ssafy.enjoytrip.features.tripost.application.exception;

import com.ssafy.enjoytrip.features.tripost.domain.TripostId;

public class TripostNotFoundException extends RuntimeException {
    public TripostNotFoundException(TripostId tripostId) {
        super("Tripost not found [id: " + tripostId.getId() + "]");
    }
}

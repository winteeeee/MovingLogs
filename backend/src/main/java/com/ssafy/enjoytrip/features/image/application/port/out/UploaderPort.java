package com.ssafy.enjoytrip.features.image.application.port.out;

import com.ssafy.enjoytrip.features.image.domain.component.Uploader;
import com.ssafy.enjoytrip.features.user.domain.Uid;

import java.util.Optional;

public interface UploaderPort {
    Optional<Uploader> getAuthor(Uid uid);
}

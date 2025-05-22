package com.ssafy.enjoytrip.features.tripost.application.port.out;

import com.ssafy.enjoytrip.features.tripost.domain.component.Author;
import com.ssafy.enjoytrip.features.user.domain.Uid;

import java.util.Optional;

public interface AuthorPort {
    Optional<Author> getAuthor(Uid uid);
}

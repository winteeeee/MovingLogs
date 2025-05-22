package com.ssafy.enjoytrip.features.attraction.application.port.out;

import com.ssafy.enjoytrip.features.attraction.domain.ContentType;

import java.util.List;

public interface SearchContentTypePort {
    List<ContentType> searchContentTypes();
}

package com.ssafy.enjoytrip.features.image.application.port.out;

import com.ssafy.enjoytrip.features.image.domain.Image;


public interface ImagePort {
    int create(Image image);
}

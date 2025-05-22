package com.ssafy.enjoytrip.features.tripost.domain.component;

import com.ssafy.enjoytrip.features.image.domain.ImageId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RouteSnapshotImage {
    private ImageId imageId;
    private Integer seq;
}

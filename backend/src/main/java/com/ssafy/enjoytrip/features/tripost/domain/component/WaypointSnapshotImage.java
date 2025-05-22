package com.ssafy.enjoytrip.features.tripost.domain.component;

import com.ssafy.enjoytrip.features.image.domain.ImageId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaypointSnapshotImage {
    private ImageId imageId;
    private Integer seq;
}

package com.ssafy.enjoytrip.features.tripost.domain.component;

import com.ssafy.enjoytrip.common.util.UuidFactory;
import com.ssafy.enjoytrip.features.attraction.domain.AttractionId;
import lombok.*;

import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RouteSnapshot {
    private RouteSnapshotId id;
    private AttractionId attractionId;
    private List<RouteSnapshotImage> images;
    private Integer seq;

    public static RouteSnapshot of(
            AttractionId attractionId,
            List<RouteSnapshotImage> images,
            Integer seq
    ) {
        return RouteSnapshot.of(
                UuidFactory.newId(RouteSnapshotId::new),
                attractionId,
                images,
                seq
        );
    }

    public static RouteSnapshot of(
            RouteSnapshotId id,
            AttractionId attractionId,
            List<RouteSnapshotImage> images,
            Integer seq
    ) {
        return RouteSnapshot.builder()
                .id(id)
                .attractionId(attractionId)
                .images(images)
                .seq(seq)
                .build();
    }
}


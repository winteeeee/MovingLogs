package com.ssafy.enjoytrip.features.tripost.domain.component;

import com.ssafy.enjoytrip.common.util.UuidFactory;
import com.ssafy.enjoytrip.features.attraction.domain.AttractionId;
import lombok.*;

import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WaypointSnapshot {
    private WaypointSnapshotId id;
    private AttractionId attractionId;
    private List<WaypointSnapshotImage> images;
    private Integer seq;

    public static WaypointSnapshot of(
            AttractionId attractionId,
            List<WaypointSnapshotImage> images,
            Integer seq
    ) {
        return WaypointSnapshot.of(
                UuidFactory.newId(WaypointSnapshotId::new),
                attractionId,
                images,
                seq
        );
    }

    public static WaypointSnapshot of(
            WaypointSnapshotId id,
            AttractionId attractionId,
            List<WaypointSnapshotImage> images,
            Integer seq
    ) {
        return WaypointSnapshot.builder()
                .id(id)
                .attractionId(attractionId)
                .images(images)
                .seq(seq)
                .build();
    }
}


package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.attraction.domain.AttractionId;
import com.ssafy.enjoytrip.features.image.domain.ImageId;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.request.CreateTripostRequest;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.request.SearchTripostRequest;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.request.UpdateTripostRequest;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.response.*;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostDetailDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostListItemDto;
import com.ssafy.enjoytrip.features.tripost.application.port.in.*;
import com.ssafy.enjoytrip.features.tripost.domain.component.WaypointSnapshot;
import com.ssafy.enjoytrip.features.tripost.domain.component.WaypointSnapshotImage;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.user.domain.Uid;

import java.util.List;

public class TripostMapper {
    public static SearchTripostPagedQuery.Query toSearchTripostPagedQuery(SearchTripostRequest request) {
        SearchTripostPagedQuery.QueryType type = SearchTripostPagedQuery.QueryType.valueOf(request.getType());

        return SearchTripostPagedQuery.Query.builder()
                .type(type)
                .query(request.getQuery())
                .page(request.getPage())
                .size(request.getSize())
                .build();
    }

    public static GetTripostByIdQuery.Query toGetTripostByIdQuery(String tripostId) {
        return GetTripostByIdQuery.Query.builder()
                .tripostId(new TripostId(tripostId))
                .build();
    }

    public static CreateTripostUseCase.Command toCreateTripostCommand(String uid, CreateTripostRequest request) {
        List<WaypointSnapshot> waypointSnapshots = request.getWaypoints().stream().map(waypointVo -> WaypointSnapshot.of(
                new AttractionId(waypointVo.attractionId()),
                waypointVo.images().stream().map(waypointImageVo -> new WaypointSnapshotImage(
                        new ImageId(waypointImageVo.imageId()),
                        waypointImageVo.seq()
                )).toList(),
                waypointVo.seq()
        )).toList();

        return CreateTripostUseCase.Command.builder()
                .uid(new Uid(uid))
                .title(request.getTitle())
                .description(request.getDescription())
                .content(request.getContent())
                .waypointSnapshots(waypointSnapshots)
                .build();
    }

    public static CreateTripostResponse toCreateTripostResponse(CreateTripostUseCase.Result result) {
        return CreateTripostResponse.builder()
                .tripostId(result.getTripostId().getId())
                .build();
    }

    public static UpdateTripostUseCase.Command toUpdateTripostCommand(String tripostId, UpdateTripostRequest request) {
        return UpdateTripostUseCase.Command.builder()
                .tripostId(new TripostId(tripostId))
                .uid(new Uid(request.getUid()))
                .title(request.getTitle())
                .description(request.getDescription())
                .content(request.getContent())
                .build();
    }

    public static UpdateTripostResponse toUpdateTripostResponse(UpdateTripostUseCase.Result result) {
        return new UpdateTripostResponse(result.getTripostId().getId());
    }

    public static DeleteTripostUseCase.Command toDeleteTripostCommand(String tripostId) {
        return DeleteTripostUseCase.Command.builder()
                .tripostId(new TripostId(tripostId))
                .build();
    }

    public static DeleteTripostResponse toDeleteTripostResponse(DeleteTripostUseCase.Result result) {
        return DeleteTripostResponse.builder()
                .deleted(result.isDeleted())
                .build();
    }

    public static GetTripostResponse toGetTripostResponse(TripostDetailDto tipost) {
        return GetTripostResponse.builder()
                .tripost(tipost)
                .build();
    }

    public static SearchTripostResponse toSearchTripostResponse(PageDto<TripostListItemDto> result) {
        return SearchTripostResponse.builder()
                .result(result)
                .build();
    }
}

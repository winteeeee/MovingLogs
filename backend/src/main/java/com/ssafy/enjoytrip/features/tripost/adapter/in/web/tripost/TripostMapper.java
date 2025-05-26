package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.attraction.domain.AttractionId;
import com.ssafy.enjoytrip.features.image.domain.ImageId;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.request.CreateTripostRequest;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.request.SearchLatestTripostRequest;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.request.SearchTripostRequest;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.request.UpdateTripostRequest;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.response.*;
import com.ssafy.enjoytrip.features.tripost.application.dto.*;
import com.ssafy.enjoytrip.features.tripost.application.port.in.*;
import com.ssafy.enjoytrip.features.tripost.domain.component.WaypointSnapshot;
import com.ssafy.enjoytrip.features.tripost.domain.component.WaypointSnapshotId;
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

    public static UpdateTripostUseCase.Command toUpdateTripostCommand(String uid, String tripostId, UpdateTripostRequest request) {
        List<WaypointSnapshot> waypointSnapshots = request.getWaypoints().stream().map(waypointVo -> WaypointSnapshot.of(
                new WaypointSnapshotId(waypointVo.id()),
                new AttractionId(waypointVo.attractionId()),
                waypointVo.images().stream().map(waypointImageVo -> new WaypointSnapshotImage(
                        new ImageId(waypointImageVo.imageId()),
                        waypointImageVo.seq()
                )).toList(),
                waypointVo.seq()
        )).toList();

        return UpdateTripostUseCase.Command.builder()
                .tripostId(new TripostId(tripostId))
                .uid(new Uid(uid))
                .title(request.getTitle())
                .description(request.getDescription())
                .content(request.getContent())
                .waypointSnapshots(waypointSnapshots)
                .build();
    }

    public static UpdateTripostResponse toUpdateTripostResponse(UpdateTripostUseCase.Result result) {
        return new UpdateTripostResponse(result.getTripostId().getId());
    }

    public static DeleteTripostUseCase.Command toDeleteTripostCommand(String uid, String tripostId) {
        return DeleteTripostUseCase.Command.builder()
                .uid(new Uid(uid))
                .tripostId(new TripostId(tripostId))
                .build();
    }

    public static DeleteTripostResponse toDeleteTripostResponse(DeleteTripostUseCase.Result result) {
        return DeleteTripostResponse.builder()
                .deleted(result.isDeleted())
                .build();
    }

    public static GetTripostResponse toGetTripostResponse(String uid, TripostDetailDto tipost) {
        return GetTripostResponse.builder()
                .tripost(tipost)
                .isAuthor(tipost.getUid().equals(uid))
                .build();
    }

    public static SearchTripostResponse toSearchTripostResponse(PageDto<TripostListItemDto> result) {
        return SearchTripostResponse.builder()
                .result(result)
                .build();
    }

    public static SearchLatestTripostUseCase.Command toSearchLatestTripostUseCaseCommand(SearchLatestTripostRequest request) {
        return SearchLatestTripostUseCase.Command.builder()
                .size(request.getSize())
                .build();
    }

    public static SearchLatestTripostResponse toSearchLatestTripostResponse(MainPageTripostDto dto) {
        return SearchLatestTripostResponse.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .createdAt(dto.getCreatedAt())
                .imageUrl(dto.getImageUrl())
                .viewCount(dto.getViewCount())
                .commentCount(dto.getCommentCount())
                .build();
    }

    public static List<SearchLatestTripostResponse> toSearchLatestTripostResponseList(List<MainPageTripostDto> dtos) {
        return dtos.stream().map(TripostMapper::toSearchLatestTripostResponse).toList();
    }
}

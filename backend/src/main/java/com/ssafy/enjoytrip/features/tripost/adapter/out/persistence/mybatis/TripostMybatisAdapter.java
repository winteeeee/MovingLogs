package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao.WaypointSnapshotDao;
import com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao.TripostDao;
import com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao.WaypointSnapshotImageDao;
import com.ssafy.enjoytrip.features.tripost.application.dto.*;
import com.ssafy.enjoytrip.features.tripost.application.port.in.SearchTripostPagedQuery;
import com.ssafy.enjoytrip.features.tripost.application.port.out.*;
import com.ssafy.enjoytrip.features.tripost.domain.Tripost;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.tripost.domain.component.Author;
import com.ssafy.enjoytrip.features.tripost.domain.component.WaypointSnapshot;
import com.ssafy.enjoytrip.features.tripost.domain.component.WaypointSnapshotId;
import com.ssafy.enjoytrip.features.tripost.domain.component.WaypointSnapshotImage;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TripostMybatisAdapter implements
        TripostPort {
    private static final Type WAYPOINT_SNAPSHOT_LIST_TYPE = new TypeToken<List<WaypointSnapshot>>(){}.getType();
    private static final Type WAYPOINT_SNAPSHOT_DTO_LIST_TYPE = new TypeToken<List<WaypointSnapshotDto>>(){}.getType();
    private static final Type WAYPOINT_IMAGE_DTO_LIST_TYPE = new TypeToken<List<WaypointImageDto>>(){}.getType();

    private final TripostDao tripostDao;
    private final WaypointSnapshotImageDao waypointSnapshotImageDao;
    private final WaypointSnapshotDao waypointSnapshotDao;

    // TODO: final 선언하고 GSON 컨픽 만들기
    private Gson gson = new Gson();

    @Override
    public Optional<PageDto<TripostListItemDto>> getPagedTripostListItemDto(SearchTripostPagedQuery.QueryType type, String query, int page, int size) {
        PageDto<Map<String, Object>> resultPage = tripostDao.toPage(page, size, new TripostDao.Criteria(type, query));

        List<TripostListItemDto> content = resultPage.getContent().stream().map(item->{
            List<WaypointSnapshotDto> waypoints = gson.fromJson((String) item.get("waypoints"), WAYPOINT_SNAPSHOT_DTO_LIST_TYPE);
            List<WaypointImageDto> images = gson.fromJson((String) item.get("images"), WAYPOINT_IMAGE_DTO_LIST_TYPE);
            return TripostListItemDto.builder()
                            .id((String) item.get("id"))
                            .title((String) item.get("title"))
                            .description((String) item.get("description"))
                            .uid((String) item.get("uid"))
                            .name((String) item.get("name"))
                            .createdAt((LocalDateTime) item.get("created_at"))
                            .commentCount((Integer) item.get("comment_count"))
                            .viewCount((Integer) item.get("view_count"))
                            .likeCount((Integer) item.get("like_count"))
                            .waypoints(waypoints)
                            .images(images)
                            .build();
        }).toList();
        PageDto<TripostListItemDto> tripostPage = PageDto.<TripostListItemDto>builder()
                .content(content)
                .page(resultPage.getPage())
                .size(resultPage.getSize())
                .totalPages(resultPage.getTotalPages())
                .totalElements(resultPage.getTotalElements())
                .hasPrevious(resultPage.getHasPrevious())
                .hasNext(resultPage.getHasNext())
                .build();
        return Optional.of(tripostPage);
    }

    @Override
    public Optional<List<LatestTripostDto>> getLatestTripostDto(Integer size) {
        List<LatestTripostDto> dtos = new ArrayList<>();
        List<Map<String, Object>> resultMaps = tripostDao.findLatestTripostDetail(size);
        for (Map<String, Object> resultMap : resultMaps) {
            LatestTripostDto dto = LatestTripostDto.builder()
                    .id((String) resultMap.get("id"))
                    .title((String) resultMap.get("title"))
                    .description((String) resultMap.get("description"))
                    .imageUrl((String) resultMap.get("image_url"))
                    .commentCount((Integer) resultMap.get("comment_count"))
                    .viewCount((Integer) resultMap.get("view_count"))
                    .createdAt(((LocalDateTime) resultMap.get("created_at")).toLocalDate())
                    .build();
            dtos.add(dto);
        }
        return Optional.of(dtos);
    }

    @Override
    public Optional<List<HotTripostDto>> getHotTripostDto(Integer size) {
        List<HotTripostDto> dtos = new ArrayList<>();
        List<Map<String, Object>> resultMaps = tripostDao.findHotTripostDetail(size);
        for (Map<String, Object> resultMap : resultMaps) {
            HotTripostDto dto = HotTripostDto.builder()
                    .id((String) resultMap.get("id"))
                    .title((String) resultMap.get("title"))
                    .author((String) resultMap.get("author"))
                    .build();
            dtos.add(dto);
        }
        return Optional.of(dtos);
    }

    @Override
    public List<String> getRelatedAttractionNames(String id) {
        return tripostDao.getRelatedAttractionNames(id);
    }

    @Override
    public Optional<Tripost> getTripost(TripostId tripostId) {
        Map<String, Object> resultMap = tripostDao.findById(tripostId);
        List<WaypointSnapshot> waypointSnapshots = gson.fromJson((String) resultMap.get("waypoints"), WAYPOINT_SNAPSHOT_LIST_TYPE);

        Tripost tripost = Tripost.builder()
                .id(new TripostId((String) resultMap.get("id")))
                .author(new Author(
                        new Uid((String) resultMap.get("uid")),
                        (String) resultMap.get("name")
                ))
                .title((String) resultMap.get("title"))
                .description((String) resultMap.get("description"))
                .content((String) resultMap.get("content"))
                .waypointSnapshots(waypointSnapshots)
                .commentCount((Integer) resultMap.get("comment_count"))
                .likeCount((Integer) resultMap.get("like_count"))
                .viewCount((Integer) resultMap.get("view_count"))
                .isDeleted(((Integer) resultMap.get("is_deleted")) == 1)
                .createdAt((LocalDateTime) resultMap.get("created_at"))
                .updatedAt((LocalDateTime) resultMap.get("updated_at"))
                .build();

        return Optional.of(tripost);
    }

    @Override
    public Optional<TripostDetailDto> getTripostDetailDto(TripostId tripostId) {
        Map<String, Object> resultMap = tripostDao.findTripostDetailById(tripostId);

        List<WaypointSnapshotDto> waypoints = Collections.emptyList();
        if (resultMap.get("waypoints") != null && !((String) resultMap.get("waypoints")).isBlank()) {
            waypoints = gson.fromJson((String) resultMap.get("waypoints"), WAYPOINT_SNAPSHOT_DTO_LIST_TYPE);
        }

        TripostDetailDto dto = TripostDetailDto.builder()
                .id((String) resultMap.get("id"))
                .uid((String) resultMap.get("uid"))
                .name((String) resultMap.get("name"))
                .title((String) resultMap.get("title"))
                .description((String) resultMap.get("description"))
                .content((String) resultMap.get("content"))
                .waypoints(waypoints)
                .commentCount((Integer) resultMap.get("comment_count"))
                .likeCount((Integer) resultMap.get("like_count"))
                .viewCount((Integer) resultMap.get("view_count"))
                .createdAt((LocalDateTime) resultMap.get("created_at"))
                .updatedAt((LocalDateTime) resultMap.get("updated_at"))
                .build();
        return Optional.of(dto);
    }

    @Override
    public Tripost save(Tripost tripost) {
        // TODO: WaypointSnapshotImage는 WaypointSnapshot에 케스케이드 걸기
        saveOrUpdateTripost(tripost);
        saveOrUpdateWaypointSnapshots(tripost);
        cleanUpObsoleteSnapshotImages(tripost);
        return tripost;
    }

    private void saveOrUpdateTripost(Tripost tripost) {
        int updated = tripostDao.update(tripost);
        if (updated == 0) {
            tripostDao.insert(tripost);
        }
    }

    private void cleanUpObsoleteSnapshotImages(Tripost tripost) {
        Map<WaypointSnapshotId, Integer> maxSeqBySnapshot = new HashMap<>();
        tripost.getWaypointSnapshots().forEach(snapshot->{
            if (snapshot.getImages() != null && !snapshot.getImages().isEmpty()) {
                int maxSeq = snapshot.getImages().stream()
                        .mapToInt(WaypointSnapshotImage::getSeq).max()
                        .orElse(0);
                maxSeqBySnapshot.put(snapshot.getId(), maxSeq);
                if (!snapshot.getImages().isEmpty()) {
                    waypointSnapshotImageDao.bulkUpsert(snapshot.getId(), snapshot.getImages());
                }
            }
        });
        List<WaypointSnapshotImageDao.Param> deleteParams = new ArrayList<>();
        maxSeqBySnapshot.forEach((snapshotId, seq) ->
                deleteParams.add(new WaypointSnapshotImageDao.Param(snapshotId, seq))
        );
        if (!deleteParams.isEmpty()) {
            waypointSnapshotImageDao.bulkDeleteBySeqGreaterThan(deleteParams);
        }
    }

    private void saveOrUpdateWaypointSnapshots(Tripost tripost) {
        int maxSeq = tripost.getWaypointSnapshots().stream()
                .mapToInt(WaypointSnapshot::getSeq).max()
                .orElse(0);
        waypointSnapshotDao.bulkUpsert(tripost.getId(), tripost.getWaypointSnapshots());
        waypointSnapshotDao.deleteBySeqGreaterThan(tripost.getId(), maxSeq);
    }

    @Override
    public int recountAllLikeCount(List<TripostId> tripostIds) {
        int updated = 0;
        if (!tripostIds.isEmpty()) {
            updated = tripostDao.recountAllLikeCountById(tripostIds);
        }
        return updated;
    }

    @Override
    public int recountCommentCount(TripostId tripostId) {
        int updated = tripostDao.recountCommentCountById(tripostId);
        return updated;
    }

    @Override
    public int updateAllViewCount(List<TripostViewCount> tripostViewCounts) {
        if (!tripostViewCounts.isEmpty()) {
            int updated = tripostDao.updateAllViewCount(tripostViewCounts);
            return updated;
        }
        return 0;
    }
}

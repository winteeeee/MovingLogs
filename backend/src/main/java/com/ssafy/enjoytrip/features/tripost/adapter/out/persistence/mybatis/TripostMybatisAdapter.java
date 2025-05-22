package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao.RouteSnapshotDao;
import com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao.TripostDao;
import com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao.RouteSnapshotImageDao;
import com.ssafy.enjoytrip.features.tripost.application.dto.RouteDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.RouteImageDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostDetailDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostListItemDto;
import com.ssafy.enjoytrip.features.tripost.application.port.in.SearchTripostPagedQuery;
import com.ssafy.enjoytrip.features.tripost.application.port.out.*;
import com.ssafy.enjoytrip.features.tripost.domain.Tripost;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.tripost.domain.component.Author;
import com.ssafy.enjoytrip.features.tripost.domain.component.RouteSnapshot;
import com.ssafy.enjoytrip.features.tripost.domain.component.RouteSnapshotId;
import com.ssafy.enjoytrip.features.tripost.domain.component.RouteSnapshotImage;
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
    private static final Type ROUTE_SNAPSHOT_LIST_TYPE = new TypeToken<List<RouteSnapshot>>(){}.getType();
    private static final Type ROUTE_DTO_LIST_TYPE = new TypeToken<List<RouteDto>>(){}.getType();
    private static final Type ROUTE_IMAGE_DTO_LIST_TYPE = new TypeToken<List<RouteImageDto>>(){}.getType();

    private final TripostDao tripostDao;
    private final RouteSnapshotImageDao routeSnapshotImageDao;
    private final RouteSnapshotDao routeSnapshotDao;

    // TODO: final 선언하고 GSON 컨픽 만들기
    private Gson gson = new Gson();

    @Override
    public Optional<PageDto<TripostListItemDto>> getPagedTripostListItemDto(SearchTripostPagedQuery.QueryType type, String query, int page, int size) {
        PageDto<Map<String, Object>> resultPage = tripostDao.toPage(page, size, new TripostDao.Criteria(type, query));

        List<TripostListItemDto> content = resultPage.getContent().stream().map(item->{
            List<RouteDto> routes = gson.fromJson((String) item.get("routes"), ROUTE_DTO_LIST_TYPE);
            List<RouteImageDto> images = gson.fromJson((String) item.get("images"), ROUTE_IMAGE_DTO_LIST_TYPE);
            return TripostListItemDto.builder()
                            .id((String) item.get("tripost_id"))
                            .title((String) item.get("title"))
                            .description((String) item.get("description"))
                            .uid((String) item.get("uid"))
                            .nickname((String) item.get("nickname"))
                            .createdAt((LocalDateTime) item.get("created_at"))
                            .commentCount((Long) item.get("comment_count"))
                            .viewCount((Long) item.get("view_count"))
                            .likeCount((Long) item.get("like_count"))
                            .routes(routes)
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
    public Optional<Tripost> getTripost(TripostId tripostId) {
        Map<String, Object> resultMap = tripostDao.findById(tripostId);
        List<RouteSnapshot> routeSnapshots = gson.fromJson((String) resultMap.get("routes"), ROUTE_SNAPSHOT_LIST_TYPE);

        Tripost tripost = Tripost.builder()
                .id(new TripostId((String) resultMap.get("tripost_id")))
                .author(new Author(
                        new Uid((String) resultMap.get("uid")),
                        (String) resultMap.get("nickname")
                ))
                .title((String) resultMap.get("title"))
                .description((String) resultMap.get("description"))
                .content((String) resultMap.get("content"))
                .routeSnapshots(routeSnapshots)
                .commentCount((Long) resultMap.get("comment_count"))
                .likeCount((Long) resultMap.get("like_count"))
                .viewCount((Long) resultMap.get("view_count"))
                .isDeleted((Boolean) resultMap.get("is_deleted"))
                .createdAt((LocalDateTime) resultMap.get("created_at"))
                .updatedAt((LocalDateTime) resultMap.get("updated_at"))
                .build();

        return Optional.of(tripost);
    }

    @Override
    public Optional<TripostDetailDto> getTripostDetailDto(TripostId tripostId) {
        Map<String, Object> resultMap = tripostDao.findTripostDetailById(tripostId);

        List<RouteDto> routes = Collections.emptyList();
        if (resultMap.get("routers") != null && !((String) resultMap.get("routers")).isBlank()) {
            routes = gson.fromJson((String) resultMap.get("routers"), ROUTE_DTO_LIST_TYPE);
        }

        TripostDetailDto dto = TripostDetailDto.builder()
                .id((String) resultMap.get("tripost_id"))
                .uid((String) resultMap.get("uid"))
                .nickname((String) resultMap.get("nickname"))
                .title((String) resultMap.get("title"))
                .content((String) resultMap.get("content"))
                .routes(routes)
                .commentCount((Long) resultMap.get("comment_count"))
                .likeCount((Long) resultMap.get("like_count"))
                .viewCount((Long) resultMap.get("view_count"))
                .createdAt((LocalDateTime) resultMap.get("created_at"))
                .updatedAt((LocalDateTime) resultMap.get("updated_at"))
                .build();
        return Optional.of(dto);
    }

    @Override
    public Tripost save(Tripost tripost) {
        // TODO: RouteSnapshotImage는 RouteSnapshot에 케스케이드 걸기
        saveOrUpdateTripost(tripost);
        cleanUpObsoleteSnapshotImages(tripost);
        saveOrUpdateRouteSnapshots(tripost);
        return tripost;
    }

    private void saveOrUpdateTripost(Tripost tripost) {
        int updated = tripostDao.update(tripost);
        if (updated == 0) {
            tripostDao.insert(tripost);
        }
    }

    private void cleanUpObsoleteSnapshotImages(Tripost tripost) {
        Map<RouteSnapshotId, Integer> maxSeqBySnapshot = new HashMap<>();
        tripost.getRouteSnapshots().forEach(snapshot->{
            int maxSeq = snapshot.getImages().stream()
                    .mapToInt(RouteSnapshotImage::getSeq).max()
                    .orElse(0);
            maxSeqBySnapshot.put(snapshot.getId(), maxSeq);
            routeSnapshotImageDao.bulkUpsert(snapshot.getId(), snapshot.getImages());
        });
        List<RouteSnapshotImageDao.Param> deleteParams = new ArrayList<>();
        maxSeqBySnapshot.forEach((snapshotId, seq) ->
                deleteParams.add(new RouteSnapshotImageDao.Param(snapshotId, seq))
        );
        if (!deleteParams.isEmpty()) {
            routeSnapshotImageDao.bulkDeleteBySeqGreaterThan(deleteParams);
        }
    }

    private void saveOrUpdateRouteSnapshots(Tripost tripost) {
        int maxSeq = tripost.getRouteSnapshots().stream()
                .mapToInt(RouteSnapshot::getSeq).max()
                .orElse(0);
        routeSnapshotDao.bulkUpsert(tripost.getId(), tripost.getRouteSnapshots());
        routeSnapshotDao.deleteBySeqGreaterThan(tripost.getId(), maxSeq);
    }

    @Override
    public int recountAllLikeCount(List<TripostId> tripostIds) {
        int updated = tripostDao.recountAllLikeCountById(tripostIds);
        return updated;
    }

    @Override
    public int recountCommentCount(TripostId tripostId) {
        int updated = tripostDao.recountCommentCountById(tripostId);
        return updated;
    }

    @Override
    public int updateAllViewCount(List<TripostViewCount> tripostViewCounts) {
        int updated = tripostDao.updateAllViewCount(tripostViewCounts);
        return updated;
    }
}

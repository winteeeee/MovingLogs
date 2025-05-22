package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao.TripostCommentDao;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostCommentDto;
import com.ssafy.enjoytrip.features.tripost.application.port.out.TripostCommentPort;
import com.ssafy.enjoytrip.features.tripost.domain.TripostComment;
import com.ssafy.enjoytrip.features.tripost.domain.TripostCommentId;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.tripost.domain.component.Author;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TripostCommentMybatisAdapter implements
        TripostCommentPort {
    private final TripostCommentDao tripostCommentDao;

    @Override
    public TripostComment save(TripostComment tripostComment) {
        int update = tripostCommentDao.update(tripostComment);
        if (update == 0) {
            tripostCommentDao.insert(tripostComment);
        }
        return tripostComment;
    }

    @Override
    public Optional<TripostComment> getTripostComment(TripostCommentId tripostCommentId) {
        Map<String, Object> resultMap = tripostCommentDao.findById(tripostCommentId);
        TripostComment tripostComment = TripostComment.builder()
                .id(new TripostCommentId((String) resultMap.get("id")))
                .tripostId(new TripostId((String) resultMap.get("tripost_id")))
                .author(new Author(
                        new Uid((String) resultMap.get("uid")),
                        (String) resultMap.get("name")
                ))
                .content((String) resultMap.get("content"))
                .isDeleted((Boolean) resultMap.get("is_deleted"))
                .createdAt((LocalDateTime) resultMap.get("created_at"))
                .updatedAt((LocalDateTime) resultMap.get("updated_at"))
                .build();
        return Optional.of(tripostComment);
    }

    @Override
    public Optional<PageDto<TripostCommentDto>> getPagedTripostCommentDto(TripostId tripostId, int page, int size) {
        PageDto<Map<String, Object>> resultPage = tripostCommentDao.toPage(page, size, new TripostCommentDao.Criteria(tripostId));

        List<TripostCommentDto> content = resultPage.getContent().stream().map(item->
            TripostCommentDto.builder()
                    .id((String) item.get("id"))
                    .uid((String) item.get("uid"))
                    .name((String) item.get("name"))
                    .content((String) item.get("content"))
                    .createdAt((LocalDateTime) item.get("created_at"))
                    .updatedAt((LocalDateTime) item.get("updated_at"))
                    .build()
        ).toList();

        PageDto<TripostCommentDto> tripostCommentPage = PageDto.<TripostCommentDto>builder()
                .content(content)
                .page(resultPage.getPage())
                .size(resultPage.getSize())
                .totalPages(resultPage.getTotalPages())
                .totalElements(resultPage.getTotalElements())
                .hasPrevious(resultPage.getHasPrevious())
                .hasNext(resultPage.getHasNext())
                .build();
        return Optional.of(tripostCommentPage);
    }
}



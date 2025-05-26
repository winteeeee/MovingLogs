package com.ssafy.enjoytrip.features.tripost.application;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostCommentDto;
import com.ssafy.enjoytrip.features.tripost.application.exception.TripostCommentNotFoundException;
import com.ssafy.enjoytrip.exception.PageNotFoundException;
import com.ssafy.enjoytrip.features.tripost.application.port.in.CreateTripostCommentUseCase;
import com.ssafy.enjoytrip.features.tripost.application.port.in.DeleteTripostCommentUseCase;
import com.ssafy.enjoytrip.features.tripost.application.port.in.SearchTripostCommentPagedQuery;
import com.ssafy.enjoytrip.features.tripost.application.port.out.AuthorPort;
import com.ssafy.enjoytrip.features.tripost.application.port.out.TripostCommentPort;
import com.ssafy.enjoytrip.features.tripost.application.port.out.TripostPort;
import com.ssafy.enjoytrip.features.tripost.domain.TripostComment;
import com.ssafy.enjoytrip.features.tripost.domain.component.Author;
import com.ssafy.enjoytrip.features.user.application.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;


@Service
@RequiredArgsConstructor
public class TripostCommentService implements
        CreateTripostCommentUseCase,
        DeleteTripostCommentUseCase,
        SearchTripostCommentPagedQuery {
    private final AuthorPort authorPort;
    private final TripostPort tripostPort;
    private final TripostCommentPort tripostCommentPort;
    // TODO: clock 컨픽 만들어야 함
    private final Clock clock;

    @Override
    @Transactional
    public CreateTripostCommentUseCase.Result createComment(CreateTripostCommentUseCase.Command command) {
        Author author = authorPort.getAuthor(command.getUid())
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다: " + command.getUid()));

        TripostComment tripostComment = TripostComment.of(
                command.getTripostId(),
                author,
                command.getContent(),
                clock
        );
        tripostCommentPort.save(tripostComment);
        tripostPort.recountCommentCount(command.getTripostId());

        return new CreateTripostCommentUseCase.Result(
                tripostComment.getId(),
                author.getName(),
                tripostComment.getContent(),
                tripostComment.getCreatedAt(),
                tripostComment.getUpdatedAt(),
                tripostComment.isOwner(command.getUid())
        );
    }

    @Override
    public DeleteTripostCommentUseCase.Result deleteComment(DeleteTripostCommentUseCase.Command command) {
        TripostComment tripostComment = tripostCommentPort.getTripostComment(command.getTripostCommentId())
                .orElseThrow(()->new TripostCommentNotFoundException(command.getTripostCommentId()));

        tripostComment.delete(clock);
        tripostCommentPort.save(tripostComment);
        return new DeleteTripostCommentUseCase.Result(tripostComment.getIsDeleted());
    }

    @Override
    public PageDto<TripostCommentDto> getComments(SearchTripostCommentPagedQuery.Query query) {
        PageDto<TripostCommentDto> dto = tripostCommentPort
                .getPagedTripostCommentDto(query.getTripostId(), query.getPage(), query.getSize())
                .orElseThrow(() -> new PageNotFoundException(query.getPage(), query.getSize()));

        dto.getContent().forEach(comment -> {
            comment.setAuthor(comment.getUid().equals(query.getUid().getId()));
        });
        return dto;
    }
}

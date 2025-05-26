package com.ssafy.enjoytrip.features.tripost.application;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.exception.PageNotFoundException;
import com.ssafy.enjoytrip.exception.UnauthorizedException;
import com.ssafy.enjoytrip.features.tripost.application.exception.TripostNotFoundException;
import com.ssafy.enjoytrip.features.tripost.application.port.in.*;
import com.ssafy.enjoytrip.features.tripost.application.port.out.*;
import com.ssafy.enjoytrip.features.tripost.domain.component.Author;
import com.ssafy.enjoytrip.features.tripost.domain.Tripost;
import com.ssafy.enjoytrip.features.user.application.exception.UserNotFoundException;
import com.ssafy.enjoytrip.features.user.domain.Role;
import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.features.tripost.application.dto.TripostDetailDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostListItemDto;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@Service
@RequiredArgsConstructor
class TripostService implements
		CreateTripostUseCase,
		DeleteTripostUseCase,
		UpdateTripostUseCase,
		SearchTripostPagedQuery,
		GetTripostByIdQuery,
		SyncTripostViewCountFromCacheUseCase {

	private final AuthorPort authorPort;
	private final TripostPort tripostPort;
	private final CachedTripostLikePort cachedTripostLikePort;
	private final CachedTripostViewCountPort cachedTripostViewCountPort;
	private final Clock clock;

	@Override
	@Transactional
	public CreateTripostUseCase.Result createTripost(CreateTripostUseCase.Command command) {
		// TODO: 예외처리 및, 예외 메시지 방식 바꾸기
		Author author = authorPort.getAuthor(command.getUid())
				.orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다: " + command.getUid()));

		Tripost tripost = Tripost.of(
				author,
				command.getTitle(),
				command.getDescription(),
				command.getContent(),
				command.getWaypointSnapshots(),
				clock
		);
		tripost = tripostPort.save(tripost);

		return new CreateTripostUseCase.Result(tripost.getId());
	}

	@Override
	@Transactional
	public DeleteTripostUseCase.Result deleteTripost(DeleteTripostUseCase.Command command) {
		Tripost tripost = tripostPort.getTripost(command.getTripostId())
				.orElseThrow(()->new TripostNotFoundException(command.getTripostId()));

		if (!tripost.isOwner(command.getUid()) && command.getRole() != Role.ROLE_ADMIN) {
			throw new UnauthorizedException("Only the owner or an admin can delete this Tripost.");
		}

		tripost.delete(clock);
		tripostPort.save(tripost);

		return new DeleteTripostUseCase.Result(tripost.getIsDeleted());
	}

	@Override
	@Transactional
	public UpdateTripostUseCase.Result updateTripost(UpdateTripostUseCase.Command command) {
		Tripost tripost = tripostPort.getTripost(command.getTripostId())
				.orElseThrow(()->new TripostNotFoundException(command.getTripostId()));

		if (!tripost.isOwner(command.getUid())) {
			throw new UnauthorizedException("Only the owner can update this Tripost.");
		}
		tripost.update(
				command.getTitle(),
				command.getDescription(),
				command.getContent(),
				command.getWaypointSnapshots(),
				clock
		);
		tripost = tripostPort.save(tripost);

		return new UpdateTripostUseCase.Result(tripost.getId());
	}

	@Override
	public TripostDetailDto findById(GetTripostByIdQuery.Query query) {
		TripostDetailDto dto = tripostPort.getTripostDetailDto(query.getTripostId())
				.orElseThrow(()->new TripostNotFoundException(query.getTripostId()));

		cachedTripostViewCountPort.incrementViewCount(query.getTripostId())
				.ifPresentOrElse(
						dto::setViewCount,
						() -> {
							cachedTripostViewCountPort.createViewCount(query.getTripostId(), dto.getViewCount()+1)
									.ifPresent(dto::setViewCount);
						}
				);
		cachedTripostLikePort.getLikeCount(query.getTripostId())
				.ifPresentOrElse(
						dto::setLikeCount,
						() -> cachedTripostLikePort.setLikeCount(query.getTripostId(), dto.getLikeCount())
				);

		return dto;
	}

	@Override
	public PageDto<TripostListItemDto> searchTriposts(SearchTripostPagedQuery.Query query) {
		PageDto<TripostListItemDto> dto = tripostPort.getPagedTripostListItemDto(query.getType(), query.getQuery(), query.getPage(), query.getSize())
				.orElseThrow(() -> new PageNotFoundException(query.getPage(), query.getSize()));
		return dto;
	}

	@Override
	public void syncViewCount() {
		List<TripostPort.TripostViewCount> tripostViewCounts = cachedTripostViewCountPort.popAllCachedViewCount();
		tripostPort.updateAllViewCount(tripostViewCounts);
	}
}

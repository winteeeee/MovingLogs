package com.ssafy.enjoytrip.features.tripost.application;

import com.ssafy.enjoytrip.features.tripost.application.port.in.TripostLikeUseCase;
import com.ssafy.enjoytrip.features.tripost.application.port.out.*;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.tripost.domain.TripostLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
class TripostLikeService implements TripostLikeUseCase {
	private final CachedTripostLikePort cachedTripostLikePort;
	private final TripostLikePort tripostLikePort;
	private final TripostPort tripostPort;

	@Override
	public void like(Command command) {
		cachedTripostLikePort.like(new TripostLike(command.getTripostId(), command.getUid()));
		cachedTripostLikePort.incrementLikeCount(command.getTripostId());
	}

	@Override
	public void unlike(Command command) {
		cachedTripostLikePort.unlike(new TripostLike(command.getTripostId(), command.getUid()));
		cachedTripostLikePort.decrementLikeCount(command.getTripostId());
	}

	@Override
	public boolean checkLiked(Command command) {
 		boolean liked = cachedTripostLikePort.checkLiked(command.getTripostId(), command.getUid())
				.orElse(tripostLikePort.check(command.getTripostId(), command.getUid()));
		return liked;
	}

	@Override
	public void syncLiked() {
		List<CachedTripostLikePort.TripostLikeState> tripostLikeStates = cachedTripostLikePort.popAllLikeState();
		List<TripostLike> creates = new ArrayList<>();
		List<TripostLike> deletes = new ArrayList<>();
		Set<TripostId> updateTargets = new HashSet<>();
		tripostLikeStates.forEach(state -> {
			TripostLike tripostLike = new TripostLike(state.tripostId(), state.uid());
			(state.state()? creates:deletes).add(tripostLike);
			updateTargets.add(state.tripostId());
		});
		tripostLikePort.createAll(creates);
		tripostLikePort.deleteAll(deletes);
		tripostPort.recountAllLikeCount(new ArrayList<>(updateTargets));
	}
}

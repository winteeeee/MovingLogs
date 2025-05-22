package com.ssafy.enjoytrip.features.tripost.application.port.in;

import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface TripostLikeUseCase {
	void like(Command command);
	void unlike(Command command);
	boolean checkLiked(Command command);
	void syncLiked();

	@Getter
	@Builder
	@AllArgsConstructor
	class Command {
		private TripostId tripostId;
		private Uid uid;
	}
}

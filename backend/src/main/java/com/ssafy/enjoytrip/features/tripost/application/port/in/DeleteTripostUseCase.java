package com.ssafy.enjoytrip.features.tripost.application.port.in;

import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.user.domain.Role;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface DeleteTripostUseCase {
	Result deleteTripost(Command command);
	
	@Getter
	@Builder
	@AllArgsConstructor
	class Command {
		private Uid uid;
		private Role role;
		private TripostId tripostId;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	class Result {
		private boolean deleted;
	}
}

package com.ssafy.enjoytrip.features.tripost.application.port.in;

import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface UpdateTripostUseCase {
	Result updateTripost(Command command);

	@Getter
	@Builder
	@AllArgsConstructor
	class Command {
		private TripostId tripostId;
		private Uid uid;
		private String title;
		private String description;
		private String content;
	}
	
	@Getter
	@Builder
	@AllArgsConstructor
	class Result {
		private TripostId tripostId;
	}
}

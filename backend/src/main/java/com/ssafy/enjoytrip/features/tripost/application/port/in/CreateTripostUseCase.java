package com.ssafy.enjoytrip.features.tripost.application.port.in;

import com.ssafy.enjoytrip.features.tripost.domain.component.WaypointSnapshot;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public interface CreateTripostUseCase {
	Result createTripost(Command command);

	@Getter
	@Builder
	@AllArgsConstructor
	class Command {
		private Uid uid;
		private String title;
		private String description;
		private String content;
		private List<WaypointSnapshot> waypointSnapshots;
	}
	
	@Getter
	@Builder
	@AllArgsConstructor
	class Result {
		private TripostId tripostId;
	}
}

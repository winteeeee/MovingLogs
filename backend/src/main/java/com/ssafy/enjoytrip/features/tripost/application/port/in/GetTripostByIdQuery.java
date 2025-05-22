package com.ssafy.enjoytrip.features.tripost.application.port.in;

import com.ssafy.enjoytrip.features.tripost.application.dto.TripostDetailDto;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface GetTripostByIdQuery {
	TripostDetailDto findById(Query query);


	@Getter
	@Builder
	@AllArgsConstructor
	class Query {
		private TripostId tripostId;
		private Uid uid;
	}
}

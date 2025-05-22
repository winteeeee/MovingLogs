package com.ssafy.enjoytrip.features.tripost.application.port.in;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.tripost.application.dto.TripostListItemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface SearchTripostPagedQuery {
	PageDto<TripostListItemDto> searchTriposts(Query query);

	@Getter
	@Builder
	@AllArgsConstructor
	class Query {
		private String query;
		private QueryType type;
		private Integer page;
		private Integer size;
	}

	enum QueryType {
		TITLE,
		CONTENT,
		AUTHOR
	}
}

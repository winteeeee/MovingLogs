package com.ssafy.enjoytrip.features.attraction.adapter.in.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SidoResponse {
    private Long sidoCode;
    private String sidoName;
}

package com.ssafy.enjoytrip.features.tripost.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class HotTripostDto {
    private String id;
    private String title;
    private String author;
    private List<String> waypointNames;
}

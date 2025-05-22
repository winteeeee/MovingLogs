package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateTripostRequest {
    private String uid;
    private String title;
    private String description;
    private String content;
}

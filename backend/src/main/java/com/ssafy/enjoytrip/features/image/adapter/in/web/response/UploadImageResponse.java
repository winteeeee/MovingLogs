package com.ssafy.enjoytrip.features.image.adapter.in.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class UploadImageResponse {
    private String imageId;
    private String mimeType;
    private String imageUrl;
    private String thumbnailUrl;
}

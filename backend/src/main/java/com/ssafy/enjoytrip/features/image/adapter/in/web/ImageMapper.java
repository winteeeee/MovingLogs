package com.ssafy.enjoytrip.features.image.adapter.in.web;

import com.ssafy.enjoytrip.features.image.adapter.in.web.response.UploadImageResponse;
import com.ssafy.enjoytrip.features.image.application.port.in.UploadImageUseCase;
import com.ssafy.enjoytrip.features.image.domain.component.MimeType;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ImageMapper {
    public static UploadImageUseCase.Command toUploadImageCommand(String uid, MultipartFile file) {
        try {
            return UploadImageUseCase.Command.builder()
                    .uid(new Uid(uid))
                    .originalName(file.getOriginalFilename())
                    .mimeType(MimeType.from(file.getContentType()))
                    .content(file.getBytes())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static UploadImageResponse toUploadImageResponse(UploadImageUseCase.Result result) {
        return UploadImageResponse.builder()
                .imageId(result.getImageId().getId())
                .mimeType(result.getMimeType().getValue())
                .imageUrl(result.getImageUrl().getPath())
                .thumbnailUrl(result.getThumbnailUrl().getPath())
                .build();
    }
}

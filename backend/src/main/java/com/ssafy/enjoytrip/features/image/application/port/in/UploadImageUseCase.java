package com.ssafy.enjoytrip.features.image.application.port.in;


import com.ssafy.enjoytrip.features.image.domain.ImageId;
import com.ssafy.enjoytrip.features.image.domain.component.MimeType;
import com.ssafy.enjoytrip.features.image.domain.component.RelativePath;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface UploadImageUseCase {
    Result uploadImage(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private Uid uid;
        private String originalName;
        private MimeType mimeType;
        private byte[] content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    class Result {
        private ImageId imageId;
        private MimeType mimeType;
        private RelativePath thumbnailUrl;
        private RelativePath imageUrl;
    }
}

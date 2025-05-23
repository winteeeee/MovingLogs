package com.ssafy.enjoytrip.features.image.application.port.out;

import com.ssafy.enjoytrip.features.image.domain.component.MimeType;
import com.ssafy.enjoytrip.features.image.domain.component.RelativePath;


public interface ImageStoragePort {
    ImageMeta saveImage(String fileName, byte[] content, MimeType mimeType);

    record ImageMeta(RelativePath imagePath, RelativePath thumbnailPath, MimeType mimeType) {  }
}

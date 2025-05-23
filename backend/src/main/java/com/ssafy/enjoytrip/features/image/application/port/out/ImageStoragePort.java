package com.ssafy.enjoytrip.features.image.application.port.out;

import com.ssafy.enjoytrip.features.image.domain.component.Mimetype;
import com.ssafy.enjoytrip.features.image.domain.component.RelativePath;


public interface ImageStoragePort {
    ImageMeta saveImage(String fileName, byte[] content, Mimetype mimetype);

    record ImageMeta(RelativePath imagePath, RelativePath thumbnailPath, Mimetype mimetype) {  }
}

package com.ssafy.enjoytrip.features.image.application;

import com.ssafy.enjoytrip.common.util.UuidFactory;
import com.ssafy.enjoytrip.features.image.application.port.in.UploadImageUseCase;
import com.ssafy.enjoytrip.features.image.application.port.out.AuthorPort;
import com.ssafy.enjoytrip.features.image.application.port.out.ImagePort;
import com.ssafy.enjoytrip.features.image.application.port.out.ImageStoragePort;
import com.ssafy.enjoytrip.features.image.domain.Image;
import com.ssafy.enjoytrip.features.image.domain.component.Author;
import com.ssafy.enjoytrip.features.user.application.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
class ImageService implements
        UploadImageUseCase {
    private final AuthorPort authorPort;
    private final ImagePort imagePort;
    private final ImageStoragePort imageStoragePort;
    private final Clock clock;

    @Override
    @Transactional
    public Result uploadImage(Command command) {
        Author author = authorPort.getAuthor(command.getUid())
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다: " + command.getUid()));

        String fileName = UuidFactory.newUuid();
        ImageStoragePort.ImageMeta imageMeta = imageStoragePort.saveImage(fileName, command.getContent(), command.getMimetype());

        Image image = Image.of(
                author,
                fileName,
                command.getOriginalName(),
                imageMeta.imagePath(),
                imageMeta.thumbnailPath(),
                imageMeta.mimetype(),
                command.getContent().length,
                clock
        );
        imagePort.save(image);

        return Result.builder()
                .imageId(image.getId())
                .mimeType(image.getMimetype())
                .imageUrl(image.getImageUrl())
                .thumbnailUrl(image.getThumbnailUrl())
                .build();
    }
}

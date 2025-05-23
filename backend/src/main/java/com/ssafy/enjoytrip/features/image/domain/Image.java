package com.ssafy.enjoytrip.features.image.domain;

import com.ssafy.enjoytrip.common.util.UuidFactory;
import com.ssafy.enjoytrip.features.image.domain.component.Author;
import com.ssafy.enjoytrip.features.image.domain.component.Mimetype;
import com.ssafy.enjoytrip.features.image.domain.component.RelativePath;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;
import java.time.LocalDateTime;


@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Image {
    private ImageId id;
    private Author author;
    private String filename;
    private String originalName;
    private RelativePath imageUrl;
    private RelativePath thumbnailUrl;
    private Mimetype mimetype;
    private Integer size;
    private LocalDateTime createdAt;

    public static Image of(
            Author author,
            String filename,
            String originalName,
            RelativePath imageUrl,
            RelativePath thumbnailUrl,
            Mimetype mimetype,
            Integer size,
            Clock clock
    ) {
        return Image.of(
                UuidFactory.newId(ImageId::new),
                author,
                filename,
                originalName,
                imageUrl,
                thumbnailUrl,
                mimetype,
                size,
                LocalDateTime.now(clock)
        );
    }

    public static Image of(
            ImageId imageId,
            Author author,
            String filename,
            String originalName,
            RelativePath imageUrl,
            RelativePath thumbnailUrl,
            Mimetype mimetype,
            Integer size,
            LocalDateTime createdAt
    ) {
        return Image.builder()
                .id(imageId)
                .author(author)
                .filename(filename)
                .originalName(originalName)
                .imageUrl(imageUrl)
                .thumbnailUrl(thumbnailUrl)
                .mimetype(mimetype)
                .size(size)
                .createdAt(createdAt)
                .build();
    }
}

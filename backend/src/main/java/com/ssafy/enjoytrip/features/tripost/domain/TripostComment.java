package com.ssafy.enjoytrip.features.tripost.domain;

import com.ssafy.enjoytrip.common.util.UuidFactory;
import com.ssafy.enjoytrip.features.tripost.domain.component.Author;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Bool;

import java.time.Clock;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TripostComment {
    private TripostCommentId id;
    private TripostId tripostId;
    private Author author;
    private String content;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(
            String content,
            Clock clock) {
        if (this.isDeleted) {
            throw new IllegalStateException("This tripost comment has already been deleted.");
        }

        String newContent = content==null? null : content.strip();
        if (newContent == null || newContent.isEmpty()) {
            throw new IllegalArgumentException("Content must not be empty");
        }
        this.content = newContent;
        this.updatedAt = LocalDateTime.now(clock);
    }

    public void delete(Clock clock) {
        if (this.isDeleted) {
            throw new IllegalStateException("This tripost comment has already been deleted.");
        }
        this.updatedAt = LocalDateTime.now(clock);
        this.isDeleted = true;
    }

    public boolean isOwner(Uid uid) {
        return this.author.getUid().equals(uid);
    }

    public static TripostComment of(
            TripostId tripostId,
            Author author,
            String content,
            Clock clock) {
      return TripostComment.of(
              UuidFactory.newId(TripostCommentId::new),
              tripostId,
              author,
              content,
              false,
              clock
      );
    }

    public static TripostComment of(
            TripostCommentId id,
            TripostId tripostId,
            Author author,
            String content,
            Boolean isDeleted,
            Clock clock) {
        return TripostComment.builder()
                .id(id)
                .tripostId(tripostId)
                .author(author)
                .content(content)
                .isDeleted(isDeleted)
                .createdAt(LocalDateTime.now(clock))
                .updatedAt(LocalDateTime.now(clock))
                .build();
    }
}

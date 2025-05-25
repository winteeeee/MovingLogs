package com.ssafy.enjoytrip.features.tripost.domain;

import java.time.Clock;
import java.util.List;
import java.time.LocalDateTime;

import com.ssafy.enjoytrip.common.util.UuidFactory;
import com.ssafy.enjoytrip.features.tripost.domain.component.Author;
import com.ssafy.enjoytrip.features.tripost.domain.component.WaypointSnapshot;
import com.ssafy.enjoytrip.features.user.domain.Uid;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Tripost {
	private TripostId id;
	private Author author;
	private String title;
	private String description;
	private String content;
	private List<WaypointSnapshot> waypointSnapshots;
	private Integer commentCount;
	private Integer likeCount;
	private Integer viewCount;
	private Boolean isDeleted;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public void update(
			String title,
			String description,
			String content,
			Clock clock) {
		if (this.isDeleted) {
			throw new IllegalStateException("This tripost has already been deleted.");
		}

        String newTitle = title == null ? null : title.strip();
		String newDescription = description == null ? null : description.strip();
		String newContent = content == null ? null : content.strip();
        if (newTitle == null || newTitle.isEmpty()) {
            throw new IllegalArgumentException("Title must not be empty");
        }
		if (newDescription == null || newDescription.isEmpty()) {
			throw new IllegalArgumentException("Description must not be empty");
		}
		if (newContent == null || newContent.isEmpty()) {
			throw new IllegalArgumentException("Content must not be empty");
		}
        this.title = newTitle;
		this.description = newDescription;
        this.content = newContent;
        this.updatedAt = LocalDateTime.now(clock);
	}
	
	public void increaseCommentCount() {
		this.commentCount++;
	}

	public void decreaseCommentCount() {
		if (commentCount > 0) {
			this.commentCount--;
		}
	}

	public void delete(Clock clock) {
		if (this.isDeleted) {
			throw new IllegalStateException("This tripost has already been deleted.");
		}
		this.updatedAt = LocalDateTime.now(clock);
		this.isDeleted = true;
	}

	public boolean isOwner(Uid uid) {
		return this.author.getUid().equals(uid);
	}

	public static Tripost of(
			Author author,
			String title,
			String description,
			String content,
			List<WaypointSnapshot> routes,
			Clock clock
	) {
		return Tripost.of(
				UuidFactory.newId(TripostId::new),
				author,
				title,
				description,
				content,
				routes,
				clock
		);
	}

	public static Tripost of(
			TripostId tripostId,
			Author author,
			String title,
			String description,
			String content,
			List<WaypointSnapshot> waypointSnapshots,
			Clock clock
	) {
		return Tripost.builder()
				.id(tripostId)
				.author(author)
				.title(title)
				.description(description)
				.content(content)
				.waypointSnapshots(waypointSnapshots)
				.commentCount(0)
				.viewCount(0)
				.likeCount(0)
				.isDeleted(false)
				.createdAt(LocalDateTime.now(clock))
				.updatedAt(LocalDateTime.now(clock))
				.build();
	}
}

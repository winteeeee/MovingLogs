package com.ssafy.enjoytrip.features.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class Uid {
	private String id;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Uid)) return false;
		Uid uid = (Uid) o;
		return Objects.equals(id, uid.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}

package com.weatherandoutfit.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LikedBoardVO {
	private int likedBoardId;
	private String email;
	private int boardId;

	public boolean isEmpty() {
		return this.boardId == 0 && (this.email == null || this.email.isEmpty());
	}
}

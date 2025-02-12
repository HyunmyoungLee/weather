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
public class BoardDTO {
	
	private String email;
	private int codeId;
	private String title;
	private int numOfLike;
	private String content;
	private String imageUrl;
}


package com.weatherandoutfit.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BoardVO {
	private int boardId;
	private String email;
	private int codeId;
	private String title;
	private int numOfLike;
	private String content;
	private String imageUrl;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	private String gender;
	private int age;
	private String nickName;
	private String profileImg;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate birthdate;
}

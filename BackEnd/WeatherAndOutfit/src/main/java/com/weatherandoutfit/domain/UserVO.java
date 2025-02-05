package com.weatherandoutfit.domain;

import java.time.LocalDate;

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
public class UserVO {
	private String email;	
	private String password;
	private String name;
	private LocalDate birthdate;
	private String gender;
	private String address;
	private String nickname;
	private String imageUrl;
}

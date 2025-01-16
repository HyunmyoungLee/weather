package com.weatherandoutfit.domain;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDTO {
	@NotEmpty(message="이메일을 입력해주세요")
	@Email
	private String email;
}

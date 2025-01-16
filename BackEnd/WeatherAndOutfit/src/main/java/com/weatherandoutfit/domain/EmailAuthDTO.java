package com.weatherandoutfit.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailAuthDTO {
	@Email
	@NotEmpty
	private String email;
	
	@NotEmpty
	private String authNumber;
}

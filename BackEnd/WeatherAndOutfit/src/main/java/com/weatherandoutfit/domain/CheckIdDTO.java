package com.weatherandoutfit.domain;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CheckIdDTO {
	private String name;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthdate;
}

package com.weatherandoutfit.infra;

import java.util.regex.Pattern;

import com.weatherandoutfit.domain.UserDTO;

public class RegisterValidator {
		
	private final String SUCCESS = "success";
	
	//이메일 형식 
	public String validateEmail(String email) {
		if(email != null) {
			String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
			if(!Pattern.matches(emailRegex, email)) {
				return "이메일 형식이 올바르지 않습니다";
			}
			return SUCCESS;
		} else {
			return "이메일은 필수 항목입니다";
		}
	}
	
	//비밀번호 영문숫자특수기호 조합 8자리 이상 16자 이하
	public String validatePassword(String password) {
		if(password != null) {
			String pwdRegex = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$";
			if(!Pattern.matches(pwdRegex,password)) {
				return "비밀번호 형식이 올바르지 않습니다";
			}
			return SUCCESS;
		} else {
			return "비밀번호는 필수 항목입니다";
		}
	}
	
	//이름(닉네임) 최소 3자 이상 20자 이하 한글 영문 숫가 
	private String validateName(String name) {
		if(name != null) {
			String nameRegex = "^(?=.*[가-힣a-zA-Z])([가-힣a-zA-Z0-9._]{3,20})$";
			if(!Pattern.matches(nameRegex, name)) {
				return "이름 형식이 올바르지 않습니다";
			}
			return SUCCESS;
		} else {
			return "이름은 필수 항목입니다";
		}
	}
	
	private String validateGender(String gender) {
		if(gender == "" || gender == null) {
			return "성별은 필수 항목입니다";
		} else {
			return SUCCESS;
		}
	}
	
	private String validateAddress(String address) {
		if(address == "" || address == null) {
			return "주소는 필수 항목입니다";
		} else { 
			return SUCCESS;
		}
	}
	
	//닉네임 형식
	public String validateNickname(String nickname) {
		if(nickname != null) {
	        String regexp = "^(?!.*[.]{2})[a-z0-9][a-z0-9._]{4,18}[a-z0-9]$";
	        if(!Pattern.matches(regexp, nickname)) {
	        	return "닉네임 형식이 올바르지 않습니다 (영소문자, 숫자, . , _ 한정 사용가능 6-20자 이내)";
	        } 
	        return SUCCESS;
		} else {
			return "닉네임은 필수 항목입니다";
		}
	}
	
	
	public String batchValidate(UserDTO user) {
		String errorMsg = "success";
		errorMsg = validateEmail(user.getEmail());
		if(errorMsg != SUCCESS) return errorMsg;
		errorMsg = validatePassword(user.getPassword());
		if(errorMsg != SUCCESS) return errorMsg;
		errorMsg = validateName(user.getName());
		if(errorMsg != SUCCESS) return errorMsg;
		errorMsg = validateGender(user.getGender());
		if(errorMsg != SUCCESS) return errorMsg;
		errorMsg = validateAddress(user.getAddress());
		if(errorMsg != SUCCESS) return errorMsg;
		
		return errorMsg;
	}
	
}

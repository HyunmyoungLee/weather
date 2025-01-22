package com.weatherandoutfit.controller;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weatherandoutfit.domain.EmailAuthDTO;
import com.weatherandoutfit.domain.EmailDTO;
import com.weatherandoutfit.domain.UserDTO;
import com.weatherandoutfit.infra.RegisterValidator;
import com.weatherandoutfit.infra.SendEmailService;
import com.weatherandoutfit.infra.ValidationException;
import com.weatherandoutfit.service.UserServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins ="http://localhost:8080")
public class RegisterController {
	
	@Autowired
	private UserServiceImpl service;
	@Autowired
	private SendEmailService mailService;
	
	@GetMapping(value ="/checkEmail", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> checkEmail(@RequestParam String email){
		RegisterValidator validator = new RegisterValidator();
		String checkEmailValidation = validator.validateEmail(email);
		if(checkEmailValidation.equals("success")) {
			String checkUserEmail = service.checkEmail(email);			
			return ResponseEntity.ok(checkUserEmail);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(checkEmailValidation);
		}
	}
	
	@PostMapping(value =  "/sendAuthCode", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> sendAuthCode(@ModelAttribute EmailDTO email){
		log.info("{}" , email.getEmail());
		String validEmail = email.getEmail();
		log.info(validEmail);
		String authCode = mailService.setEmail(validEmail);
		log.info(authCode);
		return ResponseEntity.ok(authCode);
	}
	
	@GetMapping(value="/checkAuthCode", produces="application/json;charset=UTF-8")
	public ResponseEntity<Object> checkAuthCode(@ModelAttribute EmailAuthDTO emailAuthDTO){
		log.info("{},{}", emailAuthDTO.getEmail(), emailAuthDTO.getAuthNumber());
		Boolean checkAuthCode = mailService.checkAuthCode(emailAuthDTO.getEmail(), emailAuthDTO.getAuthNumber());
		if(checkAuthCode) {
			return ResponseEntity.status(HttpStatus.OK).body("이메일 인증이 완료되었습니다");
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증번호가 올바르지 않습니다");
		}
	}
	
	@PostMapping(value = "/register",  produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> userRegister(@RequestBody UserDTO user){
		log.info("{}", user);
		int registUser = 0;
		try {
			registUser = service.registUser(user);
		}catch(ValidationException e) {
//			e.printStackTrace();
			log.error("Validation failed : {} " , e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}catch(DuplicateKeyException | MyBatisSystemException e) {
			log.error(" Duplicate Email error : {} " , e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복된 이메일입니다.");
		}catch(Exception e) {
//			e.printStackTrace();
			log.error("Unexpected  error : {} " , e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.ok(registUser > 0 ? "register Success" : "");
	}
	
	
	
}

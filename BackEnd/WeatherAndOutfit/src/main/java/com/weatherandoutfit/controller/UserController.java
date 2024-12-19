package com.weatherandoutfit.controller;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherandoutfit.domain.UserLoginDTO;
import com.weatherandoutfit.domain.UserDTO;
import com.weatherandoutfit.infra.ValidationException;
import com.weatherandoutfit.service.UserServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
	
	private final String UNAUTHORIZED_MESSAGE = "로그인 정보가 없습니다.";
	@Autowired
	private UserServiceImpl service;
	
	@PostMapping(value = "/register",  produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> userRegister(@ModelAttribute UserDTO user){
		log.info("{}", user);
		int registUser = 0;
		try {
			registUser = service.registUser(user);
		}catch(ValidationException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}catch(DuplicateKeyException | MyBatisSystemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복된 이메일입니다.");
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.ok(registUser > 0 ? "register Success" : "");
	}
	
	@PostMapping(value = "/login", produces ="application/json;charset=UTF-8")
	public ResponseEntity<Object> userLogin(@RequestBody UserLoginDTO loginDTO, HttpSession session, HttpServletResponse response){
			Map<String, String>loginResponse = new HashMap<String, String>();
			UserDTO loginInfo = service.login(loginDTO);
			if(loginInfo != null) {
				loginResponse.put("loginSuccess", loginInfo.getName());
				session.setAttribute("user", loginInfo);
				log.info("로그인 성공 세션 생성 : {}", session.getAttribute("user"));
				log.info("로그인 성공 세션 아이디 : {}", session.getId());
				
				Cookie cookie = new Cookie("JSESSIONID", session.getId());
				cookie.setMaxAge(1800); // 30분
				cookie.setPath("/");
				response.addCookie(cookie);
				
				return ResponseEntity.ok(loginResponse);
			} else {
				log.info("로그인 실패");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 아이디 또는 비밀번호입니다.");
			}
			
	}
	
	@PostMapping(value="/logout", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> userLogout(HttpSession session){
		session.invalidate();
		return ResponseEntity.ok("로그아웃 완료");
	}
	
	@GetMapping(value = "/status", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> checkStatus(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UNAUTHORIZED_MESSAGE);
		}
		Object sessionData = null;
		Map<String, String> map = new HashMap<String, String>();
		if(session.getAttribute("user") != null) {
			sessionData = (UserDTO)session.getAttribute("user");
		}
		
		if(sessionData instanceof UserDTO) {
			map.put("name", ((UserDTO)sessionData).getName());
		}
		return ResponseEntity.ok(map);
		
	}
}

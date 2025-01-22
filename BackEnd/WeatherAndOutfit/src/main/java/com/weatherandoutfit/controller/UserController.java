package com.weatherandoutfit.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weatherandoutfit.domain.UserLoginDTO;
import com.weatherandoutfit.domain.CheckIdDTO;
import com.weatherandoutfit.domain.UpdatePasswordDTO;
import com.weatherandoutfit.domain.UserDTO;
import com.weatherandoutfit.service.UserServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {
	
	private final String UNAUTHORIZED_MESSAGE = "로그인 정보가 없습니다.";
	@Autowired
	private UserServiceImpl service;
	
	@PostMapping(value = "/login", produces ="application/json;charset=UTF-8")
	public ResponseEntity<Object> userLogin(@RequestBody UserLoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response){
			Map<String, String>loginResponse = new HashMap<String, String>();
			log.info("{}",loginDTO.toString());
			UserDTO loginInfo = service.login(loginDTO);
			if(loginInfo != null) {
				HttpSession session = request.getSession(true);
				loginResponse.put("loginSuccess", loginInfo.getName());
				session.setAttribute("user", loginInfo);
				log.info("로그인 성공 세션 생성 : {}", session.getAttribute("user"));
				log.info("로그인 성공 세션 아이디 : {}", session.getId());
				
				Cookie cookie = new Cookie("JSESSIONID", session.getId());
				cookie.setMaxAge(1800); // 30분
				cookie.setPath("/");
				cookie.setSecure(false);
				response.addCookie(cookie);
				
				log.info("Cookie Name: {}", cookie.getName());
				log.info("Cookie Value: {}", cookie.getValue());
				log.info("Cookie Path: {}", cookie.getPath());
				
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
	
	@GetMapping(value="/checkId", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> checkId(@ModelAttribute CheckIdDTO idDTO){
		log.info(idDTO.toString());
		List<String> emailList = service.getEmailList(idDTO);
		return ResponseEntity.ok(emailList);
	}
	
	@PutMapping(value="/setNewPassword", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> setPassword(@RequestBody UpdatePasswordDTO updateDTO){
		log.info("{}", updateDTO.toString());
		int result = service.updatePassword(updateDTO);
		if(result == 1) {
				return ResponseEntity.ok("비밀번호가 변경되었습니다 로그인해주세요");
		} else {
			return ResponseEntity.badRequest().body("서버 오류로 인해 비밀번호 변경에 실패했습니다.");
		}
	}
}

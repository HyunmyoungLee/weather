package com.weatherandoutfit.controller;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.weatherandoutfit.domain.UserLoginDTO;
import com.weatherandoutfit.domain.UserProfileDTO;
import com.weatherandoutfit.domain.UserVO;
import com.weatherandoutfit.domain.CheckIdDTO;
import com.weatherandoutfit.domain.UpdatePasswordDTO;
import com.weatherandoutfit.domain.UserDTO;
import com.weatherandoutfit.service.UserServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = "http://ec2-43-201-47-190.ap-northeast-2.compute.amazonaws.com", allowCredentials = "true")
public class UserController {
	
	private final String UNAUTHORIZED_MESSAGE = "로그인 정보가 없습니다.";
	@Autowired
	private UserServiceImpl service;
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@PostMapping(value = "/login", produces ="application/json;charset=UTF-8")
	public ResponseEntity<Object> userLogin(@RequestBody UserLoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response){
			Map<String, String>loginResponse = new HashMap<String, String>();
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
//				cookie.setDomain("ec2-43-201-47-190.ap-northeast-2.compute.amazonaws.com"); 
				response.addCookie(cookie);
	
				log.info("Cookie Name: {}", cookie.getName());
				log.info("Cookie Value: {}", cookie.getValue());
				log.info("Cookie Path: {}", cookie.getPath());
				
				String userEmail = loginInfo.getEmail();
				UserVO userInfo = service.getUserInfo(userEmail);
				loginResponse.put("nickname", userInfo.getNickname());
				loginResponse.put("imageUrl", userInfo.getImageUrl());
				if(userInfo.getNickname() == null || userInfo.getImageUrl() == null) {
					loginResponse.put("redirectUrl", "/initProfile");
				} else {
					loginResponse.put("redirectUrl", "/");
				}
				
				return ResponseEntity.ok(loginResponse);
			} else {
				log.info("로그인 실패");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 아이디 또는 비밀번호입니다.");
			}
			
	}
	
	@PostMapping(value="/logout", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> userLogout(HttpSession session, HttpServletResponse response){
		Cookie cookie = new Cookie("JSESSIONID", null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
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
			map.put("email",((UserDTO)sessionData).getEmail());
		}
		return ResponseEntity.ok(map);
	}
	
	@GetMapping(value="/checkId", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> checkId(@ModelAttribute CheckIdDTO idDTO){
		List<String> emailList = service.getEmailList(idDTO);
		return ResponseEntity.ok(emailList);
	}
	
	@PutMapping(value="/setNewPassword", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> setPassword(@RequestBody UpdatePasswordDTO updateDTO){
		int result = service.updatePassword(updateDTO);
		if(result == 1) {
				return ResponseEntity.ok("비밀번호가 변경되었습니다 로그인해주세요");
		} else {
			return ResponseEntity.badRequest().body("서버 오류로 인해 비밀번호 변경에 실패했습니다.");
		}
	}
	
	@GetMapping(value="/socialLogin", produces = "application/json;charset=UTF-8")
	public void socialLogin(HttpServletResponse response) throws IOException{
		String clientId = "dc076dca00ca3b8af8476dea8f16bd60";
		String redirectUri = "http://ec2-43-201-47-190.ap-northeast-2.compute.amazonaws.com:8081/user/kakaoLogin";
		String KakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="  + clientId + "&redirect_uri=" + redirectUri;
		
		response.sendRedirect(KakaoAuthUrl);
		//"https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=dc076dca00ca3b8af8476dea8f16bd60&redirect_uri=http://localhost:8081/user/socialLogin"

		}
	
	@GetMapping(value="/kakaoLogin", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> kakaoLogin(HttpSession session, @RequestParam String code){
		if(code == null || code.isEmpty()) {
		return ResponseEntity.badRequest().body("코드가 유효하지 않습니다");
	}
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	
	MultiValueMap< String, String> body = new LinkedMultiValueMap<String, String>();
	body.add("grant_type", "authorization_code");
	body.add("client_id", "dc076dca00ca3b8af8476dea8f16bd60");
	body.add("redirect_uri", "http://ec2-43-201-47-190.ap-northeast-2.compute.amazonaws.com:8081/user/kakaoLogin");
	body.add("code", code);
	
	String tokenUri = "https://kauth.kakao.com/oauth/token";
	String getUserInfo = "https://kapi.kakao.com/v2/user/me";
	List<String> getUserInfoLists = Arrays.asList("kakao_account.profile", "kakao_account.name", "kakao_account.email","kakao_account.gender");
	String listAsString = "[" + String.join(",", getUserInfoLists) + "]";
	
	HttpEntity<MultiValueMap<String, String>> requestToken = new HttpEntity<MultiValueMap<String,String>>(body,headers);
	ResponseEntity<Map>	response = restTemplate.exchange(tokenUri, HttpMethod.POST, requestToken, Map.class);
	Map<String, Object> responseBody = response.getBody();
	
	if(responseBody == null || !responseBody.containsKey("access_token")) {
		return ResponseEntity.badRequest().body("토큰 오류 발생");
	}
	log.info((String) responseBody.get("access_token"));
	
	String uri = UriComponentsBuilder.fromHttpUrl(getUserInfo)
			.queryParam("listParam", listAsString)
			.build()
			.toString();

	
	HttpHeaders userInfoHeaders = new HttpHeaders();
	userInfoHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	
	String token = (String) responseBody.get("access_token");
	userInfoHeaders.add("Authorization", "Bearer " + token);
	
	HttpEntity<MultiValueMap<String, String>> requestUserInfo = new HttpEntity<>(userInfoHeaders);
	ResponseEntity<Map> userInfo = restTemplate.exchange(uri, HttpMethod.GET, requestUserInfo, Map.class);
	Map<String, Map<String,Object>> responseUserInfo = userInfo.getBody();
	
	if(responseUserInfo == null || !responseUserInfo.containsKey("kakao_account")) {
		return ResponseEntity.badRequest().body("카카오 정보 조회 실패");
	}
	UserDTO userInfoByKakao = service.getInfoByKakao((String)responseUserInfo.get("kakao_account").get("email"));
	String email = userInfoByKakao.getEmail();
	UserVO userAllInfo = service.getUserInfo(email);
	HttpHeaders redirectHeader = new HttpHeaders();
	if(userInfoByKakao != null) {
		Map<String, String> user = new HashMap<String, String>();
		user.put("loginSuccess", userInfoByKakao.getName());
		session.setAttribute("user", userInfoByKakao);
		if(userAllInfo.getNickname() == null || userAllInfo.getImageUrl() == null) {
			redirectHeader.setLocation(URI.create("http://ec2-43-201-47-190.ap-northeast-2.compute.amazonaws.com/initProfile"));
		} else {			
			redirectHeader.setLocation(URI.create("http://ec2-43-201-47-190.ap-northeast-2.compute.amazonaws.com"));
		}
		return ResponseEntity.status(HttpStatus.FOUND).headers(redirectHeader).build();
	} else {
		redirectHeader.setLocation(URI.create("http://ec2-43-201-47-190.ap-northeast-2.compute.amazonaws.com/register?email=" + (String)responseUserInfo.get("kakao_account").get("email")));
		return ResponseEntity.status(HttpStatus.FOUND).headers(redirectHeader).build();
	}
	}
	
	@PostMapping(value = "/addProfile", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> addProfile(@RequestPart("userProfile") UserProfileDTO profile, @RequestPart(value = "file", required = false) MultipartFile file, HttpSession session){
		UserDTO userSession = (UserDTO) session.getAttribute("user");
		String email = userSession.getEmail();
		if(file == null) {
			file = new MockMultipartFile("file", new byte[0]);
		}
		UserVO userInfo = service.addProfile(profile,file,email);
		
		if(userInfo.getNickname() != null || userInfo.getNickname() != null) {
			return ResponseEntity.ok(userInfo);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유저 프로필 등록 실패");
		}
		
	}
	
	@GetMapping(value="/info", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> getUserInfo(HttpSession session){
		UserDTO userSession = (UserDTO) session.getAttribute("user");
		if(userSession == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UNAUTHORIZED_MESSAGE);
		}
		String email = userSession.getEmail();
		UserVO user = service.getUserInfo(email);
		return ResponseEntity.ok(user);
	}
	
	@PutMapping(value="/modifyProfilePic", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> modifyProfilePic(@RequestParam("file") MultipartFile file, HttpSession session){
		UserDTO userSession = (UserDTO) session.getAttribute("user");
		if(userSession == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UNAUTHORIZED_MESSAGE);
		}
		String email = userSession.getEmail();
		if(file == null) {
			file = new MockMultipartFile("file", new byte[0]);
		}
		int result = service.modfiyProfilePic(email,file);
		return ResponseEntity.ok(result == 1 ? "프로필 사진 변경 완료" : "프로필 사진 변경 실패");
	}
	@PutMapping(value="/modifyNickname", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> modifyNickname(@RequestBody Map<String,String> request, HttpSession session){
		UserDTO userSession = (UserDTO) session.getAttribute("user");
		if(userSession == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UNAUTHORIZED_MESSAGE);
		}
		String email = userSession.getEmail();
		String nickname = request.get("nickname");
		int result = service.modifyNickname(email,nickname);
		return ResponseEntity.ok(result == 1 ? "닉네임 변경 완료" : "닉네임 변경 실패");
	}
}

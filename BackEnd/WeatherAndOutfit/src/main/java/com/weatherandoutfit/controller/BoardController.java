package com.weatherandoutfit.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.weatherandoutfit.domain.BoardDTO;
import com.weatherandoutfit.domain.UserDTO;
import com.weatherandoutfit.service.BoardServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardServiceImpl boardService;
	
	@PostMapping(value ="/addPost" ,produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> addPost(@RequestPart("outfitPost") BoardDTO board, @RequestPart(value = "file", required = true)MultipartFile file, HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 없습니다");
		}	
		if(file == null) {
			file = new MockMultipartFile("file", new byte[0]);
		}
		UserDTO userSession = (UserDTO) session.getAttribute("user");
		String email = userSession.getEmail();
		board.setEmail(email);
		log.info(board.toString());
		int result = boardService.addPost(board, file);
		
		return result == 1 ? ResponseEntity.ok("업로드 완료") : ResponseEntity.badRequest().body("업로드 실패");
		
	}
}

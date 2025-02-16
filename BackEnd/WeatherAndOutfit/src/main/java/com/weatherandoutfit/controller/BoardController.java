package com.weatherandoutfit.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.weatherandoutfit.domain.BoardDTO;
import com.weatherandoutfit.domain.BoardVO;
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
//		HttpSession session = request.getSession(false);
//		if(session == null) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 없습니다");
//		}	
		if(file == null) {
			file = new MockMultipartFile("file", new byte[0]);
		}
//		UserDTO userSession = (UserDTO) session.getAttribute("user");
//		String email = userSession.getEmail();
		String email = "hyunmyoung100@gmail.com";
		board.setEmail(email);
		log.info(board.toString());
		int result = boardService.addPost(board, file);
		
		return result == 1 ? ResponseEntity.ok("업로드 완료") : ResponseEntity.badRequest().body("업로드 실패");
		
	}
	
	@DeleteMapping(value="/deletePost", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> deletePost(@RequestParam("boardId") int boardId, HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 없습니다");
		}
		int result = boardService.deletePost(boardId);
		return result == 1 ? ResponseEntity.ok("삭제 완료") : ResponseEntity.badRequest().body("삭제 실패");
	}
	
	@PutMapping(value ="/modifyPost/{boardId}",consumes = "multipart/form-data", produces="application/json;charset=UTF-8")
	public ResponseEntity<Object> updatePost(@PathVariable("boardId")int boardId, @RequestPart("outfitPost") BoardDTO board, @RequestPart(value="file", required= false) MultipartFile file, HttpServletRequest request){
//		HttpSession session = request.getSession(false);
//		if(session == null) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 없습니다");
//		}	
//		UserDTO user = (UserDTO) session.getAttribute("user");
//		String email =  user.getEmail();
		String email = "hyunmyoung100@gmail.com";
		board.setEmail(email);
		if(file == null) {
			file = new MockMultipartFile("file",new  byte[0]);
		}
		
		int result = boardService.updatePost(boardId, board, file);
		return result == 1 ? ResponseEntity.ok("수정 완료") : ResponseEntity.badRequest().body("수정 실패");
				
	}
	
}

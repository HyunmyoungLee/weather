package com.weatherandoutfit.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.weatherandoutfit.domain.BoardDTO;
import com.weatherandoutfit.domain.BoardVO;
import com.weatherandoutfit.domain.LikedBoardDTO;
import com.weatherandoutfit.domain.UserDTO;
import com.weatherandoutfit.service.BoardServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/board")
@CrossOrigin(origins = "http://localhost:8080")
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
		HttpSession session = request.getSession(false);
		if(session == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 없습니다");
		}	
		UserDTO user = (UserDTO) session.getAttribute("user");
		String email =  user.getEmail();
		board.setEmail(email);
		if(file == null) {
			file = new MockMultipartFile("file",new  byte[0]);
		}
		
		int result = boardService.updatePost(boardId, board, file);
		return result == 1 ? ResponseEntity.ok("수정 완료") : ResponseEntity.badRequest().body("수정 실패");
				
	}
	
	@GetMapping(value="/getBoard", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> getBoard(@RequestParam("location") String location, @RequestParam( value = "genders", required = false, defaultValue =  "")String genders, @RequestParam(value = "ages", required = false, defaultValue ="")String ages,@RequestParam(value ="period", required = false, defaultValue = "") String period ){
		List<String> genderList = genders.isEmpty() ? new ArrayList<String>() : Arrays.asList(genders.split(","));
		List<String> ageList = ages.isEmpty() ? new ArrayList<String>() :  Arrays.asList(ages.split(","));
		List<BoardVO> boardList = boardService.getBoardList(location,genderList,ageList,period);
		if(boardList != null && !boardList.isEmpty()) {
			return ResponseEntity.ok(boardList);
		}

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}
	@GetMapping(value="/getMyBoard", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> getMyBoard(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 없습니다");
		}
		UserDTO user = (UserDTO) session.getAttribute("user");
		String email = user.getEmail();
		List<BoardVO> boardList = boardService.getMyBoardList(email);
		return ResponseEntity.ok(boardList);
	}
	
	@GetMapping(value="/getLikedBoard",  produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object> getLikedBoard(@RequestParam("boardId") int boardId, @RequestParam("email")String email){
		Boolean checkLikedBoard  = boardService.getLikedBoard(boardId, email);
		return ResponseEntity.ok(checkLikedBoard);
	}
	
	@PostMapping(value="/addLikedBoard", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object>addLikedBoard(@RequestBody LikedBoardDTO likedBoard){
		log.info(likedBoard.toString());
		int result = boardService.addLikedBoard(likedBoard);
		return ResponseEntity.ok(result == 1 ? "게시물 좋아요 성공" : "게시물 좋아요 실패");
	}
	
	@DeleteMapping(value="/deleteLikedBoard", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Object>deleteLikedBoard(@RequestBody LikedBoardDTO likedBoard){
		log.info(likedBoard.toString());
		int result = boardService.deleteLikedBoard(likedBoard);
		
		return ResponseEntity.ok(result == 1 ? "게시물 좋아요 삭제 성공" : "게시물 좋아요 삭제 실패");
	}
	
}

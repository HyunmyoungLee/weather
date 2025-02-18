package com.weatherandoutfit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.weatherandoutfit.domain.BoardDTO;
import com.weatherandoutfit.domain.BoardVO;


public interface BoardService {

	int addPost(BoardDTO board, MultipartFile file);
	public int deletePost(int boardId) ;
	BoardVO getBoard(int boardId);
	int updatePost(int boardId, BoardDTO board, MultipartFile file);
	List<BoardVO> getBoardList(String location, ArrayList<String> genders, ArrayList<String> ages, String period);
}

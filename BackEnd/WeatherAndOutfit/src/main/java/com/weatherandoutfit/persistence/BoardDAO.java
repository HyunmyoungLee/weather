package com.weatherandoutfit.persistence;


import java.util.ArrayList;
import java.util.List;

import com.weatherandoutfit.domain.BoardDTO;
import com.weatherandoutfit.domain.BoardVO;


public interface BoardDAO {

	int addPost(BoardDTO board);
	
	BoardVO getBoardInfo(int boardId);
	
	int deletePost(int boardId, String email);
	
	int updatePost(int boardId, BoardDTO board);
	
	public List<BoardVO> getBoardList(String location, ArrayList<String> genders, ArrayList<String> ages,
			String period);

}

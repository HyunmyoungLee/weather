package com.weatherandoutfit.persistence;


import java.util.List;

import com.weatherandoutfit.domain.BoardDTO;
import com.weatherandoutfit.domain.BoardVO;
import com.weatherandoutfit.domain.LikedBoardDTO;
import com.weatherandoutfit.domain.LikedBoardVO;


public interface BoardDAO {

	int addPost(BoardDTO board);
	
	BoardVO getBoardInfo(int boardId);
	
	int deletePost(int boardId, String email);
	
	int updatePost(int boardId, BoardDTO board);
	
	public List<BoardVO> getBoardList(String location, List<String> genders, List<String> ages,
			String period);

	LikedBoardVO getLikedBoard(int boardId, String email);

	int addLikedBoard(LikedBoardDTO likedBoard);

	int deleteLikedBoard(LikedBoardDTO likedBoard);

	int updateBoardLike(LikedBoardDTO likedBoard, Boolean flag);

	List<BoardVO> getMyBoardList(String email);

}

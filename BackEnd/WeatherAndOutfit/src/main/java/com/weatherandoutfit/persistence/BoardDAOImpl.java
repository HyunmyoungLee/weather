package com.weatherandoutfit.persistence;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import com.weatherandoutfit.domain.BoardDTO;
import com.weatherandoutfit.domain.BoardVO;
import com.weatherandoutfit.domain.LikedBoardDTO;
import com.weatherandoutfit.domain.LikedBoardVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class BoardDAOImpl implements BoardDAO {
	private final static String NS = "com.weatherandoutfit.mappers.boardMapper.";

	@Inject
	private SqlSession ses;
	
	@Override
	public int addPost(BoardDTO board) {

		return ses.insert(NS+"addPost", board);
	}

	@Override
	public BoardVO getBoardInfo(int boardId) {
		BoardVO board = ses.selectOne(NS + "getBoard", boardId);
		if(board == null) {
			board = new BoardVO();
		}
		return board;
	}

	@Override
	public int deletePost(int boardId, String email) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("boardId", boardId);
		args.put("email", email);
		return ses.delete(NS+"deleteBoard", args);
	}

	@Override
	public int updatePost(int boardId, BoardDTO board) {
		Map<String,Object> args = new HashMap<String, Object>();
		args.put("boardId", boardId);
		args.put("title", board.getTitle());
		args.put("content", board.getContent());
		args.put("imageUrl", board.getImageUrl());
		args.put("email", board.getEmail());
		return ses.update(NS+"updateBoard", args);
	}

	@Override
	public List<BoardVO> getBoardList(String location, List<String> genders, List<String> ages,
			String period) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("location", location);
		if(genders.size() > 0) {	
			args.put("genders", genders);
		} 
		
		if(ages.size() > 0) {
			args.put("ages", ages);
		}
		if(period != null && !period.isEmpty()) {
			LocalDateTime now = LocalDateTime.now();
			switch (period) {
				case "오늘" :
					args.put("period", now.toLocalDate().atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
					break;
				case  "최근 1주일" :  
					args.put("period", now.minusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
					break;
				case "최근 한 달" : 
					args.put("period", now.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
					break;		
			}
		}
		
		log.info(args.toString());
		List<BoardVO> boardList  = ses.selectList(NS+"getBoardList", args);
		if(boardList == null) {
			boardList  = new ArrayList<BoardVO>();
		}
		log.info(" DAO단 boardList : {}", boardList.toString());
		return boardList;
	}

	@Override
	public LikedBoardVO getLikedBoard(int boardId, String email) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("boardId", boardId);
		args.put("email", email);
		LikedBoardVO likedBoard = ses.selectOne(NS+"getLikedBoard", args);
		if(likedBoard == null) {
			return new LikedBoardVO();
		}
		return likedBoard;
	}

	@Override
	public int addLikedBoard(LikedBoardDTO likedBoard) {
		return ses.insert(NS+"addLikedBoard", likedBoard);
	}

	@Override
	public int deleteLikedBoard(LikedBoardDTO likedBoard) {
		return ses.delete(NS+"deleteLikedBoard", likedBoard);
	}

	@Override
	public int updateBoardLike(LikedBoardDTO likedBoard, Boolean flag) {
		if(flag == true) {
			return ses.update(NS+"increaseLikedBoard", likedBoard);
		} else {
			return ses.update(NS+"decreaseLikedBoard", likedBoard);			
		}
	}

	@Override
	public List<BoardVO> getMyBoardList(String email) {
		List<BoardVO> boardList = ses.selectList(NS+"getMyBoard", email);
		if(boardList == null) {
			return new ArrayList<BoardVO>(); 
		}
		return boardList;
	}
	
}

package com.weatherandoutfit.persistence;


import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.weatherandoutfit.domain.BoardDTO;
import com.weatherandoutfit.domain.BoardVO;

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
	
}

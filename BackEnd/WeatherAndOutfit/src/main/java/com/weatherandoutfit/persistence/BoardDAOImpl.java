package com.weatherandoutfit.persistence;


import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.weatherandoutfit.domain.BoardDTO;

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
	
}

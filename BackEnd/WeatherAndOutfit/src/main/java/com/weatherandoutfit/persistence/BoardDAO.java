package com.weatherandoutfit.persistence;


import com.weatherandoutfit.domain.BoardDTO;


public interface BoardDAO {

	int addPost(BoardDTO board);

}

package com.weatherandoutfit.service;

import org.springframework.web.multipart.MultipartFile;

import com.weatherandoutfit.domain.BoardDTO;


public interface BoardService {

	int addPost(BoardDTO board, MultipartFile file);

}

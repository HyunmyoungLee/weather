package com.weatherandoutfit.service;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.weatherandoutfit.domain.BoardDTO;
import com.weatherandoutfit.infra.S3Service;
import com.weatherandoutfit.persistence.BoardDAOImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardServiceImpl implements BoardService {

	@Autowired
	private S3Service s3Service;
	@Inject
	private BoardDAOImpl boardDAO;
	
	@Override
	public int addPost(BoardDTO board, MultipartFile file) {
		String imageUrl = null;
		try {
		if(!file.getOriginalFilename().isEmpty()) {
				imageUrl = s3Service.uploadFile(file, false);
		} else {
			imageUrl = "https://weatherandoutfit.s3.ap-northeast-2.amazonaws.com/outfit/no_image.jpg";
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		board.setImageUrl(imageUrl);
		int result = boardDAO.addPost(board);
		return result;
	}

}

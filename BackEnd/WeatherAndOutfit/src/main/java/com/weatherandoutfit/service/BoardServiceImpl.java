package com.weatherandoutfit.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.weatherandoutfit.domain.BoardDTO;
import com.weatherandoutfit.domain.BoardVO;
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

	@Transactional
	@Override
	public int deletePost(int boardId) {
		BoardVO board = boardDAO.getBoardInfo(boardId);
		log.info(board.toString());
		String email = board.getEmail();
		
		int result = boardDAO.deletePost(boardId, email);
		log.info("{}", result);
		if(result == 1) {
			String imageFile = board.getImageUrl();
			String fileName = getS3Path(imageFile);
			log.info("{}", fileName);
			if(!fileName.equals("no_image.jpg")) {
				s3Service.deleteFile(fileName);							
			}
		}
		
		return result;
	}

	@Override
	public BoardVO getBoard(int boardId) {
		return boardDAO.getBoardInfo(boardId);
	}

	@Override
	public int updatePost(int boardId, BoardDTO board, MultipartFile file) {
		BoardVO originBoard = boardDAO.getBoardInfo(boardId);
		int result  = 0;
		try {
			if(!file.getOriginalFilename().isEmpty()) {
				String s3Path = getS3Path(originBoard.getImageUrl());
				String newImageUrl = s3Service.uploadFile(file, false);
				if(newImageUrl != null) {
					s3Service.deleteFile(s3Path);
					board.setImageUrl(newImageUrl);
					result = boardDAO.updatePost(boardId, board);
				}
			}
		}catch(IOException e){
			e.printStackTrace();	
		}
		return result;
	}
	
	private String getS3Path(String imageUrl) {
		String[] strArray = imageUrl.split("/");
		String s3Path = strArray[3] +"/" + strArray[4];
		return s3Path;
	}

	@Override
	public List<BoardVO> getBoardList(String location, ArrayList<String> genders, ArrayList<String> ages,
			String period) {
		log.info("boardList 동작");
		List<BoardVO> boardList = boardDAO.getBoardList(location, genders, ages, period);
		return boardList;
	}

}

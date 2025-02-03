package com.weatherandoutfit.service;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.weatherandoutfit.domain.UserLoginDTO;
import com.weatherandoutfit.domain.UserProfileDTO;
import com.weatherandoutfit.domain.CheckIdDTO;
import com.weatherandoutfit.domain.UpdatePasswordDTO;
import com.weatherandoutfit.domain.UserDTO;
import com.weatherandoutfit.infra.RegisterValidator;
import com.weatherandoutfit.infra.S3Service;
import com.weatherandoutfit.infra.ValidationException;
import com.weatherandoutfit.persistence.UserDAOImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	
	@Inject
	UserDAOImpl userDao;

	@Override
	public int registUser(UserDTO user) {
		log.info("Service loading...");
		String msg = new RegisterValidator().batchValidate(user);
		if(!msg.equals("success")) {
			throw new ValidationException(msg);
		}
		return userDao.registUser(user);
	}

	@Override
	public UserDTO login(UserLoginDTO loginDTO) {
		log.info("service LoginInfo  {} ", loginDTO.toString());
		return userDao.login(loginDTO);
	}

	public String checkEmail(String email) {
		
		return userDao.checkEmail(email);
	}

	public List<String> getEmailList(CheckIdDTO idDTO) {
		return userDao.getEmailList(idDTO);
	}

	public int updatePassword(UpdatePasswordDTO updateDTO) {
		return userDao.updatePwd(updateDTO);
	}

	public UserDTO getInfoByKakao(String email) {
		return userDao.getInfoByKakao(email);
	}

	public int addProfile(UserProfileDTO profile, MultipartFile file, String email) {
		String imageUrl = null;
		try {
			if(!file.getOriginalFilename().isEmpty()) {
				S3Service s3Service = new S3Service();
				imageUrl  = s3Service.uploadFile(file);
			} else {
				imageUrl = "https://weatherandoutfit.s3.ap-northeast-2.amazonaws.com/UserProfile/user.png";
			}
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		profile.setProfileImageUrl(imageUrl);
		
		return userDao.addProfile(profile, email);
	}

}

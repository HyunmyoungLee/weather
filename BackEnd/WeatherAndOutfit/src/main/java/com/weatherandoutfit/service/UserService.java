package com.weatherandoutfit.service;

import com.weatherandoutfit.domain.UserLoginDTO;
import com.weatherandoutfit.domain.UserProfileDTO;
import com.weatherandoutfit.domain.UserVO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.weatherandoutfit.domain.CheckIdDTO;
import com.weatherandoutfit.domain.UpdatePasswordDTO;
import com.weatherandoutfit.domain.UserDTO;

public interface UserService {

	public int registUser(UserDTO user);
	public UserDTO login(UserLoginDTO loginDTO);
	public String checkEmail(String email);
	public List<String> getEmailList(CheckIdDTO idDTO);
	int updatePassword(UpdatePasswordDTO updateDTO);
	UserDTO getInfoByKakao(String email);
	UserVO addProfile(UserProfileDTO profile, MultipartFile file, String email);
	UserVO getNickname(String nickname);
	UserVO getUserInfo(String email);
	int modfiyProfilePic(String email, MultipartFile file);
	int modifyNickname(String email, String nickname);
}

package com.weatherandoutfit.persistence;

import com.weatherandoutfit.domain.UserLoginDTO;
import com.weatherandoutfit.domain.UserProfileDTO;
import com.weatherandoutfit.domain.UserVO;

import java.util.List;

import com.weatherandoutfit.domain.CheckIdDTO;
import com.weatherandoutfit.domain.UpdatePasswordDTO;
import com.weatherandoutfit.domain.UserDTO;

public interface UserDAO {
	public int registUser(UserDTO user);
	UserDTO login(UserLoginDTO loginDTO);
	public String checkEmail(String email);
	public List<String> getEmailList(CheckIdDTO idDTO);
	int updatePwd(UpdatePasswordDTO updateDTO);
	UserDTO getInfoByKakao(String email);
	int addProfile(UserProfileDTO profile, String email);
	UserVO getNickname(String nickname);
	UserVO getUserInfo(String email);
	int modifyProfilePic(String email, String imageUrl);
	int modifyNickname(String email, String nickname);
}

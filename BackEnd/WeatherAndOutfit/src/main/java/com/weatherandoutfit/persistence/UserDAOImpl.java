package com.weatherandoutfit.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.weatherandoutfit.domain.UserLoginDTO;
import com.weatherandoutfit.domain.UserProfileDTO;
import com.weatherandoutfit.domain.UserVO;
import com.weatherandoutfit.domain.CheckIdDTO;
import com.weatherandoutfit.domain.UpdatePasswordDTO;
import com.weatherandoutfit.domain.UserDTO;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class UserDAOImpl implements UserDAO{
	private final static String NS = "com.weatherandoutfit.mappers.userMapper.";
	
	@Inject
	private SqlSession ses;

	@Override
	public int registUser(UserDTO user) {
		return ses.insert(NS+"registUser", user);
	}

	@Override
	public UserDTO login(UserLoginDTO loginDTO) {
		log.info("DAO LoginInfo  {} ", loginDTO.toString());
		return ses.selectOne(NS+"login", loginDTO);
	}

	@Override
	public String checkEmail(String email) {
		String userEmail = ses.selectOne(NS+"checkEmail", email);
		if(userEmail == null) {
			return "";
		} else {
			return userEmail;
		}	
	}

	@Override
	public List<String> getEmailList(CheckIdDTO idDTO) {
		List<String> emailList = ses.selectList(NS+"getEmailList", idDTO);
		if(emailList != null) {
			return emailList;
		} else {
			return new ArrayList<String>();
		}
	}

	@Override
	public int updatePwd(UpdatePasswordDTO updateDTO) {
		
		int result = ses.update(NS+"updatePwd", updateDTO);
		return result;
	}

	@Override
	public UserDTO getInfoByKakao(String email) {
		return ses.selectOne(NS+"getInfoByKakao", email);
	}

	@Override
	public int addProfile(UserProfileDTO profile, String email) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("nickName", profile.getNickName());
		args.put("profileImageUrl", profile.getProfileImageUrl());
		args.put("email", email);
		return ses.update(NS+"addProfile", args);
	}

	@Override
	public UserVO getNickname(String nickname) {
		UserVO userInfo = ses.selectOne(NS+"getNickname", nickname);
		if(userInfo != null) {
			return userInfo;
		} else {		
			return new UserVO();
		}
	}

	@Override
	public UserVO getUserInfo(String email) {
		return ses.selectOne(NS+"getUserInfo", email);
	}

	@Override
	public int modifyProfilePic(String email, String imageUrl) {
		Map<String, String> args = new HashMap<String, String>();
		args.put("email", email);
		args.put("imageUrl", imageUrl);
		return ses.update(NS+"updateProfilePic", args);
	}

	@Override
	public int modifyNickname(String email, String nickname) {
		Map<String, String> args = new HashMap<String, String>();
		args.put("email", email);
		args.put("nickname", nickname);
		return ses.update(NS+"updateNickname", args);
	}
}

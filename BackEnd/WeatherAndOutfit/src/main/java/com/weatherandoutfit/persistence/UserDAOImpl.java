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

	public String checkEmail(String email) {
		String userEmail = ses.selectOne(NS+"checkEmail", email);
		if(userEmail == null) {
			return "";
		} else {
			return userEmail;
		}	
	}

	public List<String> getEmailList(CheckIdDTO idDTO) {
		List<String> emailList = ses.selectList(NS+"getEmailList", idDTO);
		if(emailList != null) {
			return emailList;
		} else {
			return new ArrayList<String>();
		}
	}

	public int updatePwd(UpdatePasswordDTO updateDTO) {
		
		int result = ses.update(NS+"updatePwd", updateDTO);
		return result;
	}

	public UserDTO getInfoByKakao(String email) {
		return ses.selectOne(NS+"getInfoByKakao", email);
	}

	public int addProfile(UserProfileDTO profile, String email) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("nickName", profile.getNickName());
		args.put("profileImageUrl", profile.getProfileImageUrl());
		args.put("email", email);
		return ses.update(NS+"addProfile", args);
	}

	public UserVO getNickname(String nickname) {
		UserVO userInfo = ses.selectOne(NS+"getNickname", nickname);
		if(userInfo != null) {
			return userInfo;
		} else {		
			return new UserVO();
		}
	}

	public UserVO getUserInfo(String email) {
		return ses.selectOne(NS+"getUserInfo", email);
	}
}

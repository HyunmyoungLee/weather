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
	
}

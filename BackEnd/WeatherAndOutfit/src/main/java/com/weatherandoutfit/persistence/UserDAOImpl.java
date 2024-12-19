package com.weatherandoutfit.persistence;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.weatherandoutfit.domain.UserLoginDTO;
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
		return ses.selectOne(NS+"login", loginDTO);
	}
	
}

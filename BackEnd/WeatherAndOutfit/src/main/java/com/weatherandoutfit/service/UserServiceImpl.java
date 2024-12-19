package com.weatherandoutfit.service;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weatherandoutfit.domain.UserLoginDTO;
import com.weatherandoutfit.domain.UserDTO;
import com.weatherandoutfit.infra.RegisterValidator;
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
		return userDao.login(loginDTO);
	}

}

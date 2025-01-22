package com.weatherandoutfit.service;

import com.weatherandoutfit.domain.UserLoginDTO;

import java.util.List;

import com.weatherandoutfit.domain.CheckIdDTO;
import com.weatherandoutfit.domain.UserDTO;

public interface UserService {

	public int registUser(UserDTO user);
	public UserDTO login(UserLoginDTO loginDTO);
	public String checkEmail(String email);
	public List<String> getEmailList(CheckIdDTO idDTO);
}

package com.weatherandoutfit.persistence;

import com.weatherandoutfit.domain.UserLoginDTO;

import java.util.List;

import com.weatherandoutfit.domain.CheckIdDTO;
import com.weatherandoutfit.domain.UserDTO;

public interface UserDAO {
	public int registUser(UserDTO user);
	UserDTO login(UserLoginDTO loginDTO);
	public String checkEmail(String email);
	public List<String> getEmailList(CheckIdDTO idDTO);
}

package com.weatherandoutfit.service;

import com.weatherandoutfit.domain.UserLoginDTO;
import com.weatherandoutfit.domain.UserDTO;

public interface UserService {

	public int registUser(UserDTO user);
	public UserDTO login(UserLoginDTO loginDTO);
}

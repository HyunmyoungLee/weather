package com.weatherandoutfit.persistence;

import com.weatherandoutfit.domain.UserLoginDTO;
import com.weatherandoutfit.domain.UserDTO;

public interface UserDAO {
	public int registUser(UserDTO user);
	UserDTO login(UserLoginDTO loginDTO);
	public String checkEmail(String email);
}

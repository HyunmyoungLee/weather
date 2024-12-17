package com.weatherandoutfit.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.Test;

public class ConnectionTest {

	private static final String DRIVER ="com.mysql.cj.jdbc.Driver";
	private static final String ID = "root";
	private static final String PWD = "1234";
	private static final String URL = "jdbc:mysql://localhost:3306/weather?serverTimezone=Asia/Seoul&characterEncoding=UTF-8&useSSL=false";
	
	@Test
	public void testConnection() throws ClassNotFoundException {
		Class.forName(DRIVER);
		try (Connection con = DriverManager.getConnection(URL, ID, PWD)) {
			if (con != null) {
				System.out.println(con.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

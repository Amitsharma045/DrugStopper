package com.drugstopper.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drugstopper.app.dao.LoginUserTypeDAO;
import com.drugstopper.app.entity.LoginUserType;

@Service
public class LoginUserTypeManager {

	@Autowired
	private  LoginUserTypeDAO loginUserTypeDAO;
	
	public LoginUserType getLoginType(String type) throws Exception {
		return loginUserTypeDAO.getLoginType(type);
	}
}

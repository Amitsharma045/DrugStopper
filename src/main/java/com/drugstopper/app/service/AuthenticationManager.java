package com.drugstopper.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drugstopper.app.dao.UserIdentityDAO;
import com.drugstopper.app.entity.User;


@Service
public class AuthenticationManager {
	
	@Autowired
	private  UserIdentityDAO userIdentityDAO;
	
	public User getUserByMobileNumber(String phoneNumber) {
		return userIdentityDAO.getUserByMobileNumber(phoneNumber);
	}

	public Long saveUser(User user) {
		return userIdentityDAO.saveUser(user);
	}
	
	public void saveUpdateUser(User user){
		userIdentityDAO.saveUpdateUser(user);
	}

}

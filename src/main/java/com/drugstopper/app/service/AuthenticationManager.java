package com.drugstopper.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drugstopper.app.dao.UserIdentityDAO;
import com.drugstopper.app.entity.User;

/**
 * @author rpsingh
 *
 */
@Service
public class AuthenticationManager {
	
	@Autowired
	private  UserIdentityDAO userIdentityDAO;
	
	public User getUserById(String phoneNumber) throws Exception {
		return userIdentityDAO.getUserById(phoneNumber);
	}

	public Long saveUser(User user) throws Exception {
		return userIdentityDAO.saveUser(user);
	}
	
	public void saveUpdateUser(User user) throws Exception {
		userIdentityDAO.saveUpdateUser(user);
	}

}

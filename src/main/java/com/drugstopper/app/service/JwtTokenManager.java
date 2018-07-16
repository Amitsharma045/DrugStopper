package com.drugstopper.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drugstopper.app.dao.JwtTokenDAO;
import com.drugstopper.app.entity.JwtToken;
import com.drugstopper.app.entity.User;

/**
 * @author rpsingh
 *
 */
@Service
public class JwtTokenManager {

	@Autowired
	private  JwtTokenDAO jwtTokenDAO;
	
	public Long createAccessToken(User user, String accessToken, String accessKey) throws Exception {
		return jwtTokenDAO.createAccessToken(user, accessToken, accessKey);
	}
	
	public JwtToken getJwtTokenByAccessToken(String accessToken) {
		return jwtTokenDAO.getJwtTokenByAccessToken(accessToken);
	}
}

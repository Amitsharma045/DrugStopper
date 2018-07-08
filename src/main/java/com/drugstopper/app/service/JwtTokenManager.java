package com.drugstopper.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drugstopper.app.dao.JwtTokenDAO;
import com.drugstopper.app.entity.JwtToken;
import com.drugstopper.app.entity.User;

@Service
public class JwtTokenManager {

	@Autowired
	private  JwtTokenDAO jwtTokenDAO;
	
	public Long createAccessAndRefreshToken(User user, String accessToken, String accessKey, 
			String refreshToken, String refreshKey) throws Exception {
		return jwtTokenDAO.createAccessAndRefreshToken(user, accessToken, accessKey, refreshToken, refreshKey);
	}
	
	public JwtToken getJwtTokenByAccessToken(String accessToken) {
		return jwtTokenDAO.getJwtTokenByAccessToken(accessToken);
	}
	
	public int updateAccessToken(String accessKey, String accessToken, String refreshToken) throws Exception {
		return jwtTokenDAO.updateAccessToken(accessKey, accessToken, refreshToken);
	}
}

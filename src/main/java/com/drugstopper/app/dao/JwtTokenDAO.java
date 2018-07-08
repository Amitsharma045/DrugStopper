package com.drugstopper.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drugstopper.app.entity.JwtToken;
import com.drugstopper.app.entity.User;

@Repository
@Transactional
public class JwtTokenDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public Long createAccessAndRefreshToken(User user, String accessToken, String accessKey, 
			String refreshToken, String refreshKey) throws Exception 
	{
		JwtToken jwt = new JwtToken();
		jwt.setAccessKey(accessKey);
		jwt.setAccessToken(accessToken);
		jwt.setRefreshKey(refreshKey);
		jwt.setRefreshToken(refreshToken);
		jwt.setUser(user);

		Long id=(Long) getSession().save(jwt);
		return id;
	}
	
	public int updateAccessToken(String accessKey, String accessToken,
								  String refreshToken) throws Exception 
	{
		Query query = getSession().createQuery("update JwtToken set accessKey = :p1 and accessToken =: p2" +
				" where refreshToken = :p3");
		query.setParameter("p1", accessKey);
		query.setParameter("p2", accessToken);
		query.setParameter("p3", refreshToken);
		int result = query.executeUpdate();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public JwtToken getJwtTokenByAccessToken(String accessToken){
		List<JwtToken> jwtTokenList=getSession()
				.createQuery("from JwtToken jt where jt.accessToken=:p1")
				.setParameter("p1", accessToken).list();

		if(jwtTokenList.size()!=0){
			return jwtTokenList.get(0);
		}
		return null;

	}

}
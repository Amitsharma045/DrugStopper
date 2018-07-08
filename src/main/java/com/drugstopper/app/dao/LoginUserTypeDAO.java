package com.drugstopper.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drugstopper.app.entity.LoginUserType;

@Repository
@Transactional
public class LoginUserTypeDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public LoginUserType getLoginType(String type){
		List<LoginUserType> userTypelist=getSession()
				.createQuery("from LoginUserType ut where ut.loginType =:p1")
				.setParameter("p1", type).list();
		if(userTypelist.size()!=0){
			return userTypelist.get(0);
		}
		return null;
	}
}

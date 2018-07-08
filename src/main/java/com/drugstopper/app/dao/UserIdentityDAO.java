package com.drugstopper.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drugstopper.app.entity.User;


@Repository
@Transactional
public class UserIdentityDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	public User getUserByMobileNumber(String phoneNumber){
		List<User> userlist=getSession()
				.createQuery("from User u where u.userId =:p1")
				.setParameter("p1", phoneNumber).list();
		if(userlist.size()!=0){
			return userlist.get(0);
		}
		return null;
	}
	
	public Long saveUser(User user){
		Long id=(Long) getSession().save(user);
		return id;
	}
	
	public void saveUpdateUser(User user){
		getSession().saveOrUpdate(user);
	}
	
	public User getUserById(Long id){
		return getSession().get(User.class,id);
	}
}

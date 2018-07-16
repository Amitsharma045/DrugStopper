package com.drugstopper.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drugstopper.app.entity.User;
/**
 * @author rpsingh
 *
 */

@Repository
@Transactional
public class UserIdentityDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	public User getUserById(String phoneNumber) throws Exception {
		List<User> userlist = getSession()
							 .createQuery("FROM User u WHERE u.userId =:p1")
							 .setParameter("p1", phoneNumber).list();
		if(userlist.size() != 0) {
			return userlist.get(0);
		}
		return null;
	}
	
	public Long saveUser(User user) throws Exception {
		Long id = (Long) getSession().save(user);
		return id;
	}
	
	public void saveUpdateUser(User user) throws Exception {
		getSession().saveOrUpdate(user);
	}
	
	public User getUserById(Long id) throws Exception {
		return getSession().get(User.class,id);
	}
} 
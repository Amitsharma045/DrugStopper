package com.drugstopper.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drugstopper.app.entity.Category;

/**
 * @author rpsingh
 *
 */
@Repository
@Transactional
public class CategoryDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	public Category[] getCategory() throws Exception {
		List<Category> categoryList = getSession()
									.createQuery("FROM Category")
									.list();
		return categoryList.toArray(new Category[categoryList.size()]);
	}
}
	
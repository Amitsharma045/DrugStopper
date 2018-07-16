package com.drugstopper.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drugstopper.app.dao.CategoryDAO;
import com.drugstopper.app.entity.Category;

/**
 * @author rpsingh
 *
 */
@Service
public class CategoryManager {
	
	@Autowired
	private  CategoryDAO categoryDAO;
	
	public Category[] getCategory() throws Exception {
		return categoryDAO.getCategory();
	}
}

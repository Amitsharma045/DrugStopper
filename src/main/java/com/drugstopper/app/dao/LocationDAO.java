package com.drugstopper.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drugstopper.app.entity.Location;


@Repository
@Transactional
public class LocationDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	
	@SuppressWarnings("unchecked")
	public Location[] searchByName(String name, String categoryId) throws Exception {
		List<Location> locationList = getSession()
				.createQuery("from Location lt where lt.category.id =:p1 and  UPPER(lt.name) LIKE :p2")
				.setParameter("p1", Long.valueOf(categoryId))
				.setParameter("p2", name.toUpperCase()+"%")

				.list();

		return locationList.toArray(new Location[locationList.size()]);
	}
	
	
	@SuppressWarnings("unchecked")
	public Location[] getState() throws Exception {
		List<Location> locationList = getSession()
				.createQuery("from Location lt where lt.category.id = 1")
				.list();

		return locationList.toArray(new Location[locationList.size()]);
	}

	@SuppressWarnings("unchecked")
	public Location[] getLocationById(String id) throws Exception {
		List<Location> locationList = getSession()
				.createQuery("from Location lt where lt.id IN ( Select lm.location.id from  LocationMapping lm"
						+ " where lm.parentLocation.id =:p1) ")
				.setParameter("p1", Long.valueOf(id))
				.setFirstResult(0).setMaxResults(10).list();

		return locationList.toArray(new Location[locationList.size()]);
	}
}

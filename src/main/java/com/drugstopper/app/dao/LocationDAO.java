package com.drugstopper.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drugstopper.app.entity.Location;
/**
 * @author rpsingh
 *
 */

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
									 .createQuery("FROM Location lt WHERE lt.category.id =:p1 AND  UPPER(lt.name) LIKE :p2")
								   	 .setParameter("p1", Long.valueOf(categoryId))
									 .setParameter("p2", name.toUpperCase()+"%")
									 .list();

		return locationList.toArray(new Location[locationList.size()]);
	}
	
	
	@SuppressWarnings("unchecked")
	public Location[] getState() throws Exception {
		List<Location> locationList = getSession()
									.createQuery("FROM Location lt WHERE lt.category.id = 1")
									.list();

		return locationList.toArray(new Location[locationList.size()]);
	}

	@SuppressWarnings("unchecked")
	public Location[] getLocationById(String id) throws Exception {
		List<Location> locationList = getSession()
									.createQuery("FROM Location lt WHERE lt.id IN ( SELECT lm.location.id FROM  LocationMapping lm"
											   + " WHERE lm.parentLocation.id =:p1) ")
									.setParameter("p1", Long.valueOf(id))
									.setFirstResult(0).list();

		return locationList.toArray(new Location[locationList.size()]);
	}
}

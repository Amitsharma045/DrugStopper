package com.drugstopper.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drugstopper.app.dao.LocationDAO;
import com.drugstopper.app.entity.Location;

@Service
public class LocationManager {
	
	@Autowired
	private  LocationDAO locationDAO;
	
	public Location[] getState() throws Exception {
		return locationDAO.getState();
	}
	
	public Location[] getLocationById(String id) throws Exception {
		return locationDAO.getLocationById(id);
	}
	
	public Location[] searchByName(String name, String categoryId) throws Exception {
		return locationDAO.searchByName(name, categoryId);
	}
}

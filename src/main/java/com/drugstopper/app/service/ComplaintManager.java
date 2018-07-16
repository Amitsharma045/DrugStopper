package com.drugstopper.app.service;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.drugstopper.app.dao.ComplaintDAO;
import com.drugstopper.app.entity.ComplaintRegistration;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@Service
public class ComplaintManager {

	
	@Autowired
	private  ComplaintDAO complaintDAO;
	
	public ComplaintRegistration[] getAllComplaints(String complaintId) throws Exception {
		return complaintDAO.getAllComplaints(complaintId);
	}
	
	public ComplaintRegistration getComplaint(String complaintId) throws Exception {
		
		return complaintDAO.getComplaint(complaintId);
	}
	
	public ComplaintRegistration[] getComplaintsByState(String stateId, String id)  throws Exception {
		return complaintDAO.getComplaintsByState(stateId, id);
	}
	public ComplaintRegistration[] getComplaintsByDistrict(String districtId, String id) throws Exception {
		return complaintDAO.getComplaintsByDistrict(districtId, id);
	}
	public ComplaintRegistration[] getComplaintsByCity(String cityId, String id) throws Exception {
		return complaintDAO.getComplaintsByCity(cityId, id);
	}
	public int getTotalComplaintCount() throws Exception {
		return complaintDAO.getTotalComplaintCount();
	}
	public int getTotalComplaintCountByState(String cityId) throws Exception {
		return complaintDAO.getTotalComplaintCountByState(cityId);
	}
	public int getTotalComplaintCountByDistrict(String districtId) throws Exception {
		return complaintDAO.getTotalComplaintCountByDistrict(districtId);
	}
	public int getTotalComplaintCountByCity(String cityId) throws Exception {
		return complaintDAO.getTotalComplaintCountByCity(cityId);
	}
	public String[] searchComplaintAgainstByLocation(String complaintAgainst, String locationId) throws Exception {
		return complaintDAO.searchComplaintAgainstByLocation(complaintAgainst, locationId);
	}
	
	public ComplaintRegistration saveComplaint(ComplaintRegistration complaintRegistration) {
		try {
			return complaintDAO.saveComplaints(complaintRegistration);
		} catch (ConstraintViolationException | MySQLIntegrityConstraintViolationException ex) {
			try {
				return complaintDAO.saveComplaints(complaintRegistration);
			} catch (Exception e) { 
				System.out.println(e.getMessage());
				return null;
			}
		}
		catch (Exception  ex) {
			System.out.println(ex.getMessage());
			return null; 
		} 
	}
}

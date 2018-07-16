package com.drugstopper.app.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author rpsingh
 *
 */
public class ComplaintBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	
	private String complaintId;
	
	private Date date;
	
	private String state;
	
	private String district;
	
	private String city;
	
	private String complaintAgainst;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(String complaintId) {
		this.complaintId = complaintId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
  
	public String getComplaintAgainst() {
		return complaintAgainst;
	}

	public void setComplaintAgainst(String complaintAgainst) {
		this.complaintAgainst = complaintAgainst;
	}
}

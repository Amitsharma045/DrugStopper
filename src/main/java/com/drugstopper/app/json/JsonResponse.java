package com.drugstopper.app.json;

import com.drugstopper.app.entity.ComplaintRegistration;
import com.drugstopper.app.entity.User;

public class JsonResponse {
	
	private String statusCode;
	private String message;
	private String accessToken;
	private String refreshToken;
	private User user ;
	private ComplaintRegistration complaint;
	private ComplaintRegistration[] complaintList;
	private String actionRequired;
	private String totalCounts;
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public ComplaintRegistration getComplaint() {
		return complaint;
	}
	public void setComplaint(ComplaintRegistration complaint) {
		this.complaint = complaint;
	}
	public ComplaintRegistration[] getComplaintList() {
		return complaintList;
	}
	public void setComplaintList(ComplaintRegistration[] complaintList) {
		this.complaintList = complaintList;
	}
	public String getActionRequired() {
		return actionRequired;
	}
	public void setActionRequired(String actionRequired) {
		this.actionRequired = actionRequired;
	}
	public String getTotalCounts() {
		return totalCounts;
	}
	public void setTotalCounts(String totalCounts) {
		this.totalCounts = totalCounts;
	}


}

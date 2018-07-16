package com.drugstopper.app.json;

import java.util.List;

import com.drugstopper.app.bean.AttachmentBean;
import com.drugstopper.app.bean.ComplaintBean;
import com.drugstopper.app.bean.LocationBean;
import com.drugstopper.app.entity.Category;
import com.drugstopper.app.entity.User;

public class JsonResponse {
	
	private String statusCode;
	private String message;
	private String accessToken;
	private String refreshToken;
	private User user ;
	private ComplaintBean complaint;
	private ComplaintBean[] complaintList;
	private List<AttachmentBean> attachmentBean;
	private LocationBean location;
	private LocationBean[] locationList;
	private Category[] categoryList;
	private String actionRequired;
	private String totalCounts;
	private String[] titleList;

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

	public ComplaintBean getComplaint() {
		return complaint;
	}
	public void setComplaint(ComplaintBean complaint) {
		this.complaint = complaint;
	}
	public ComplaintBean[] getComplaintList() {
		return complaintList;
	}
	public void setComplaintList(ComplaintBean[] complaintList) {
		this.complaintList = complaintList;
	}
	public Category[] getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(Category[] categoryList) {
		this.categoryList = categoryList;
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
	public LocationBean getLocation() {
		return location;
	}
	public void setLocation(LocationBean location) {
		this.location = location;
	}
	public LocationBean[] getLocationList() {
		return locationList;
	}
	public void setLocationList(LocationBean[] locationList) {
		this.locationList = locationList;
	}
	public String[] getTitleList() {
		return titleList;
	}
	public void setTitleList(String[] titleList) {
		this.titleList = titleList;
	}
	public List<AttachmentBean> getAttachmentBean() {
		return attachmentBean;
	}
	public void setAttachmentBean(List<AttachmentBean> attachmentBean) {
		this.attachmentBean = attachmentBean;
	}


}

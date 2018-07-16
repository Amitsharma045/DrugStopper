package com.drugstopper.app.rest;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import com.drugstopper.app.ApplicationInit;
import com.drugstopper.app.bean.Image;
import com.drugstopper.app.json.JsonResponse;
import com.drugstopper.app.property.ConstantProperty;

/**
 * @author rpsingh
 *
 */
public abstract class RestResource {

	@Autowired
	protected HttpServletRequest  request_;
	
	protected LinkedHashMap<String, Object> sendResponse(JsonResponse jsonResponse) throws Exception {
		LinkedHashMap<String, Object> json = new LinkedHashMap<String, Object>();
		json.put(ConstantProperty.STATUS_CODE, jsonResponse.getStatusCode());
		json.put(ConstantProperty.MESSAGE, jsonResponse.getMessage());
		if(jsonResponse.getAccessToken() != null)
			json.put(ConstantProperty.ACCESS_TOKEN, jsonResponse.getAccessToken());
		if(jsonResponse.getUpdateFlag() != null)
			json.put(ConstantProperty.UPDATE_FLAG, jsonResponse.getUpdateFlag());
		if(jsonResponse.getUser() != null)
			json.put(ConstantProperty.USER_DETAIL, jsonResponse.getUser());
		if(jsonResponse.getTitleList() != null)
			json.put(ConstantProperty.COMPLAINT_TITLE, jsonResponse.getTitleList());
		if(jsonResponse.getComplaint() != null)
			json.put(ConstantProperty.COMPLAINTS, jsonResponse.getComplaint());
		if(jsonResponse.getComplaintList() != null)
			json.put(ConstantProperty.COMPLAINT_LIST, jsonResponse.getComplaintList());
		if(jsonResponse.getLocation() != null)
			json.put(ConstantProperty.LOCATIONS, jsonResponse.getLocation());
		if(jsonResponse.getLocationList() != null)
			json.put(ConstantProperty.LOCATION_LIST, jsonResponse.getLocationList());
		if(jsonResponse.getCategoryList() != null)
			json.put(ConstantProperty.CATEGORY_LIST, jsonResponse.getCategoryList());
		if(jsonResponse.getActionRequired() != null)
			json.put(ConstantProperty.ACTION_REQUIRED, jsonResponse.getActionRequired());
		if(jsonResponse.getTotalCounts() != null)
			json.put(ConstantProperty.TOTAL_COUNT, jsonResponse.getTotalCounts());
		if(jsonResponse.getAttachmentBean()!=null)
			json.put(ConstantProperty.ATTACHMENT_LIST, jsonResponse.getAttachmentBean());
		if(jsonResponse.getImageList()!=null)
			json.put(ConstantProperty.IMAGE_LIST, jsonResponse.getImageList());
		
		
		return json;
	}
	
	protected boolean isUserAdmin() {
		String role = request_.getAttribute("role").toString();
		if("Admin".equals(role)) return true;
		return false;
	}
	
	protected void log(Class clazz, String message, String tag) {
		Logger logger = LoggerFactory.getLogger(clazz);
		if(ConstantProperty.LOG_INFO.equals(tag)) {
			logger.info(message);
		} else if(ConstantProperty.LOG_WARNING.equals(tag)) {
			logger.warn(message);
		} else if(ConstantProperty.LOG_ERROR.equals(tag)) {
			logger.error(message);
		} else if(ConstantProperty.LOG_DEBUG.equals(tag)) {
			logger.debug(message);
		} else {
			logger.trace(message);
		}
	}
}

package com.drugstopper.app.rest;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.drugstopper.app.json.JsonResponse;
import com.drugstopper.app.property.ConstantProperty;

public abstract class RestResource {

	@Autowired
	protected HttpServletRequest  request_;
	
	protected LinkedHashMap<String, Object> sendResponse(JsonResponse jsonResponse) throws Exception {
		LinkedHashMap<String, Object> json = new LinkedHashMap<String, Object>();
		json.put(ConstantProperty.STATUS_CODE, jsonResponse.getStatusCode());
		json.put(ConstantProperty.MESSAGE, jsonResponse.getMessage());
		if(jsonResponse.getAccessToken()!=null)
			json.put(ConstantProperty.ACCESS_TOKEN, jsonResponse.getAccessToken());
		if(jsonResponse.getRefreshToken()!=null)
			json.put(ConstantProperty.REFRESH_TOKEN, jsonResponse.getRefreshToken());
		if(jsonResponse.getUser()!=null)
			json.put(ConstantProperty.USER_DETAIL, jsonResponse.getUser());
		if(jsonResponse.getComplaint()!=null)
			json.put(ConstantProperty.COMPLAINTS, jsonResponse.getComplaint());
		if(jsonResponse.getComplaintList()!=null)
			json.put(ConstantProperty.COMPLAINT_LIST, jsonResponse.getComplaintList());
		if(jsonResponse.getActionRequired()!=null)
			json.put(ConstantProperty.ACTION_REQUIRED, jsonResponse.getActionRequired());
		if(jsonResponse.getTotalCounts()!=null)
			json.put(ConstantProperty.TOTAL_COUNT, jsonResponse.getTotalCounts());

		return json;
	}
	
	protected boolean isUserAdmin() {
		System.out.println(request_);
		String role = request_.getAttribute("role").toString();
		System.out.println("User Role is"+role);
		if("Admin".equals(role)) return true;
		return false;
	}
}

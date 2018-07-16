package com.drugstopper.app.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drugstopper.app.bean.LocationBean;
import com.drugstopper.app.entity.Category;
import com.drugstopper.app.entity.Location;
import com.drugstopper.app.json.JsonResponse;
import com.drugstopper.app.property.ConstantProperty;
import com.drugstopper.app.rest.RestResource;
import com.drugstopper.app.service.CategoryManager;
import com.drugstopper.app.service.LocationManager;

@Controller
@RequestMapping(value = "/drugstopper/api/location")
public class LocationResource extends RestResource {

	@Autowired
	private  LocationManager locationManager;
	
	@Autowired
	private  CategoryManager categoryManager;

	private JsonResponse jsonResponse;
	
	

	@RequestMapping(value = "/v1.0/getCategory", produces={"application/json"},
			consumes={"application/x-www-form-urlencoded"},
			method = RequestMethod.GET)
	@ResponseBody
	public HashMap<String,Object> fetchCategory() throws Exception {
		jsonResponse=new JsonResponse();
		Category[] categoryList = categoryManager.getCategory();
		jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
		jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_PROCESSED);
		jsonResponse.setCategoryList(categoryList);
		return sendResponse(jsonResponse);
	}
	
	@RequestMapping(value = "/v1.0/searchBy", produces={"application/json"},
			consumes={"application/x-www-form-urlencoded"},
			method = RequestMethod.GET)
	@ResponseBody
	public HashMap<String,Object> searchByName(@RequestParam("name") String name, 
											   @RequestParam("categoryId") String categoryId) throws Exception {
		jsonResponse=new JsonResponse();
		if(categoryId == null) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			return sendResponse(jsonResponse);
		}
		try {
			Long.valueOf(categoryId);
		} catch (Exception ex) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			return sendResponse(jsonResponse);
		}
		Location[] locationList = locationManager.searchByName(name, categoryId);
		LocationBean[] list = getLocationList(locationList);
		jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
		jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_PROCESSED);
		jsonResponse.setLocationList(list);
		return sendResponse(jsonResponse);
	}
	
	
	
	
	@RequestMapping(value = "/v1.0/getState", produces={"application/json"},
			consumes={"application/x-www-form-urlencoded"},
			method = RequestMethod.GET)
	@ResponseBody
	public HashMap<String,Object> fetchState() throws Exception {
		jsonResponse=new JsonResponse();
		Location[] locationList = locationManager.getState();
		LocationBean[] list = getLocationList(locationList);
		jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
		jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_PROCESSED);
		jsonResponse.setLocationList(list);
		return sendResponse(jsonResponse);
	}
	
	@RequestMapping(value = "/v1.0/getDistrict", produces={"application/json"},
			consumes={"application/x-www-form-urlencoded"},
			method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String,Object> fetchDistrictById(HttpServletRequest request) throws Exception {
		jsonResponse=new JsonResponse();
		String stateId = request.getParameter(ConstantProperty.STATE_ID);
		jsonResponse=new JsonResponse();
		if(stateId == null) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			return sendResponse(jsonResponse);
		}
		try {
			Long.valueOf(stateId);
		} catch (Exception ex) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			return sendResponse(jsonResponse);
		}
		Location[] locationList = locationManager.getLocationById(stateId);
		LocationBean[] list = getLocationList(locationList);
		jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
		jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_PROCESSED);
		jsonResponse.setLocationList(list);
		return sendResponse(jsonResponse);
	}
	
	
	@RequestMapping(value = "/v1.0/getCity", produces={"application/json"},
			consumes={"application/x-www-form-urlencoded"},
			method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String,Object> fetchCityById(HttpServletRequest request) throws Exception {
		jsonResponse=new JsonResponse();
		String districtId = request.getParameter(ConstantProperty.DISTRICT_ID);
		jsonResponse=new JsonResponse();
		if(districtId == null) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			return sendResponse(jsonResponse);
		}
		try {
			Long.valueOf(districtId);
		} catch (Exception ex) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			return sendResponse(jsonResponse);
		}
		Location[] locationList = locationManager.getLocationById(districtId);
		LocationBean[] list = getLocationList(locationList);
		jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
		jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_PROCESSED);
		jsonResponse.setLocationList(list);
		return sendResponse(jsonResponse);
	}
	
	private LocationBean[] getLocationList(Location[] list) {
		List<LocationBean> locationlist = new ArrayList<LocationBean>();
		for (Location location : list) {
			LocationBean locationBean = new LocationBean();
			locationBean.setId(location.getId());
			locationBean.setName(location.getName());
			locationBean.setCategory(location.getCategory().getName());
			
			locationlist.add(locationBean);
		}
		return locationlist.toArray(new LocationBean[locationlist.size()]);
	}
	
}

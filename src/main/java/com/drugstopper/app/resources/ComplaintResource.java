package com.drugstopper.app.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.drugstopper.app.bean.AttachmentBean;
import com.drugstopper.app.bean.ComplaintBean;
import com.drugstopper.app.bean.LocationBean;
import com.drugstopper.app.entity.AttachmentDetail;
import com.drugstopper.app.entity.ComplaintRegistration;
import com.drugstopper.app.entity.Location;
import com.drugstopper.app.entity.User;
import com.drugstopper.app.json.JsonResponse;
import com.drugstopper.app.property.ConstantProperty;
import com.drugstopper.app.rest.RestResource;
import com.drugstopper.app.rest.authentication.AppAuthenticationEndPoint;
import com.drugstopper.app.service.AttachmentDetailManager;
import com.drugstopper.app.service.ComplaintManager;
import com.drugstopper.app.util.CommonUtil;

import io.jsonwebtoken.lang.Collections;
import com.google.gson.Gson;

/**
 * @author rpsingh
 *
 */
@Controller
@RequestMapping(value = "/drugstopper/api/complaint")
public class ComplaintResource  extends RestResource {

	private Class clazz = ComplaintResource.class;

	
	@Autowired
	private  ComplaintManager complaintManager;
	
	@Autowired
	private  AttachmentDetailManager attachmentManager;
	
	private JsonResponse jsonResponse;
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/v1.0/saveComplaint", produces={"application/json"},
			method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String,Object> saveComplaint(@RequestParam("data") String dataJson,@RequestParam("uploadingFiles")  MultipartFile[] uploadedFiles) throws Exception {
		jsonResponse=new JsonResponse();
		try {
			ComplaintRegistration complaintRegistration = new Gson().fromJson(dataJson, ComplaintRegistration.class);
			AttachmentDetail [] attachmentDetails = new AttachmentDetail[uploadedFiles.length];
			if(attachmentDetails.length>0 && !validateFiles(uploadedFiles,attachmentDetails,jsonResponse)) {
				jsonResponse.setStatusCode(ConstantProperty.INVALID_FILE);
				jsonResponse.setMessage(ConstantProperty.INVALID_FILE_ERROR);
				log(clazz, ConstantProperty.INVALID_FILE_ERROR, ConstantProperty.LOG_DEBUG);
				return sendResponse(jsonResponse);
			}
			if(complaintRegistration != null) { 
				System.out.println(complaintRegistration.toString());
				User appUser = new User();
				appUser.setId(getUserId());
				complaintRegistration.setUser(appUser);
				ComplaintRegistration savedComplaint=complaintManager.saveComplaint(complaintRegistration);
				if(savedComplaint!=null && savedComplaint.getId()!=0) {
					if(attachmentDetails.length>0)
						attachmentDetails = attachmentManager.saveAttachments(savedComplaint,attachmentDetails,uploadedFiles);
					jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
					jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_SAVED);
					jsonResponse.setComplaint(getComplaint(savedComplaint)); 
					jsonResponse.setAttachmentBean(getAttachmentBeanFromDetail(Collections.arrayToList(attachmentDetails)));
					log(clazz, ConstantProperty.SUCCESSFUL_SAVED, ConstantProperty.LOG_DEBUG);
					return sendResponse(jsonResponse); 
				}else {
					jsonResponse.setStatusCode(ConstantProperty.METHOD_FAILURE);
					jsonResponse.setMessage(ConstantProperty.FAILURE_NOT_SAVED);
					log(clazz, ConstantProperty.FAILURE_NOT_SAVED, ConstantProperty.LOG_DEBUG);
				}
			}
			else {
				jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR); 
				jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
				log(clazz, ConstantProperty.INTERNAL_SERVER_ERROR, ConstantProperty.LOG_DEBUG);
				return sendResponse(jsonResponse);
			}
		} catch (Exception ex) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			log(clazz, ex.getMessage(), ConstantProperty.LOG_ERROR);
			return sendResponse(jsonResponse);
		}
		return sendResponse(jsonResponse);
	}
	
	@RequestMapping(value = "/v1.0/getAll", produces={"application/json"},
			method = RequestMethod.GET)
	@ResponseBody
	public HashMap<String,Object> fetchComplaint() throws Exception {
		jsonResponse=new JsonResponse();
		try {
			ComplaintRegistration[] complaintList = complaintManager.getAllComplaints(String.valueOf(0));
			ComplaintBean[] list = getComplaintList(complaintList);

			jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
			jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_PROCESSED);
			jsonResponse.setComplaintList(list);
			jsonResponse.setTotalCounts(String.valueOf(complaintManager.getTotalComplaintCount()));
		} catch(Exception ex) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			log(clazz, ex.getMessage(), ConstantProperty.LOG_ERROR);
			return sendResponse(jsonResponse);
		}
		return sendResponse(jsonResponse);
	}
	
	@RequestMapping(value = "/v1.0/getAll/nextList", produces={"application/json"},
			method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String,Object> fetchComplaintNextList(HttpServletRequest request) throws Exception {
		String lastId = request.getParameter(ConstantProperty.LAST_ID);
		jsonResponse = new JsonResponse();
		if(lastId == null) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			log(clazz, ConstantProperty.LAST_ID+" Null Value", ConstantProperty.LOG_DEBUG);
			return sendResponse(jsonResponse);
		}
		try {
			Long.valueOf(lastId);

			ComplaintRegistration[] complaintList = complaintManager.getAllComplaints(String.valueOf(lastId));
			ComplaintBean[] list = getComplaintList(complaintList);
			jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
			jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_PROCESSED);
			jsonResponse.setComplaintList(list);
		} catch (Exception ex) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			log(clazz, ex.getMessage(), ConstantProperty.LOG_ERROR);
			return sendResponse(jsonResponse);
		}
		return sendResponse(jsonResponse);
	}

	@RequestMapping(value = "/v1.0/searchByLocation", produces={"application/json"},
			method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String,Object> fetchComplaintSearchByLocation(HttpServletRequest request) throws Exception {
		String stateId = request.getParameter(ConstantProperty.STATE_ID);
		String districtId = request.getParameter(ConstantProperty.DISTRICT_ID);
		String cityId = request.getParameter(ConstantProperty.CITY_ID);
		jsonResponse = new JsonResponse();
		try {
			if(cityId != null && !("".equals(cityId))) {
				Long.valueOf(cityId);
				ComplaintRegistration[] complaintList = complaintManager.getComplaintsByCity(cityId, String.valueOf(0));
				jsonResponse = getJsonObject(jsonResponse, complaintList, String.valueOf(complaintManager.getTotalComplaintCountByCity(cityId)));
			} else if(districtId != null && !("".equals(districtId))) {
				Long.valueOf(districtId);
				ComplaintRegistration[] complaintList = complaintManager.getComplaintsByDistrict(districtId, String.valueOf(0));
				jsonResponse = getJsonObject(jsonResponse, complaintList, String.valueOf(complaintManager.getTotalComplaintCountByDistrict(districtId)));
			} else if(stateId != null && !("".equals(stateId))) {
				Long.valueOf(stateId);
				ComplaintRegistration[] complaintList = complaintManager.getComplaintsByState(stateId, String.valueOf(0));
				jsonResponse = getJsonObject(jsonResponse, complaintList, String.valueOf(complaintManager.getTotalComplaintCountByState(stateId)));
			} else {
				ComplaintRegistration[] complaintList = complaintManager.getAllComplaints(String.valueOf(0));
				jsonResponse = getJsonObject(jsonResponse, complaintList, String.valueOf(complaintManager.getTotalComplaintCount()));
			}
		} catch (Exception ex) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			log(clazz, ex.getMessage(), ConstantProperty.LOG_ERROR);
			return sendResponse(jsonResponse);
		}
		return sendResponse(jsonResponse);
	}

	@RequestMapping(value = "/v1.0/searchByLocation/nextList", produces={"application/json"},
			method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String,Object> fetchComplaintSearchByLocationNextList(HttpServletRequest request) throws Exception {
		String stateId = request.getParameter(ConstantProperty.STATE_ID);
		String districtId = request.getParameter(ConstantProperty.DISTRICT_ID);
		String cityId = request.getParameter(ConstantProperty.CITY_ID);
		String lastId = request.getParameter(ConstantProperty.LAST_ID);
		
		jsonResponse = new JsonResponse();
		try {
			Long.valueOf(lastId);
			if(cityId != null  && !("".equals(cityId))) {
				Long.valueOf(cityId);
				ComplaintRegistration[] complaintList = complaintManager.getComplaintsByCity(cityId, lastId);
				jsonResponse = getJsonObject(jsonResponse, complaintList, null);
			} else if(districtId != null  && !("".equals(districtId))) {
				Long.valueOf(districtId);
				ComplaintRegistration[] complaintList = complaintManager.getComplaintsByDistrict(districtId, lastId);
				jsonResponse = getJsonObject(jsonResponse, complaintList, null);
			} else if(stateId != null  && !("".equals(stateId))) {
				Long.valueOf(stateId);
				ComplaintRegistration[] complaintList = complaintManager.getComplaintsByState(stateId, lastId);
				jsonResponse = getJsonObject(jsonResponse, complaintList, null);
			} else {
				ComplaintRegistration[] complaintList = complaintManager.getAllComplaints(String.valueOf(lastId));
				jsonResponse = getJsonObject(jsonResponse, complaintList, null);
			}
		} catch (Exception ex) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			log(clazz, ex.getMessage(), ConstantProperty.LOG_ERROR);
			return sendResponse(jsonResponse);
		}
		return sendResponse(jsonResponse);
	}
	
	@RequestMapping(value = "/v1.0/getComplaint/{complaintId}", produces={"application/json"},
			method = RequestMethod.GET)
	@ResponseBody
	public HashMap<String,Object> fetchComplaintDetail(@PathVariable("complaintId") String complaintId) throws Exception {
		jsonResponse=new JsonResponse();
		ComplaintRegistration complaint = complaintManager.getComplaint(complaintId); 
		List<AttachmentDetail> attachmentDetail = (List<AttachmentDetail>) attachmentManager.getAttachments(complaint.getComplaintId());
		jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
		jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_PROCESSED);
		jsonResponse.setComplaint(getComplaint(complaint));
		jsonResponse.setAttachmentBean(getAttachmentBeanFromDetail(attachmentDetail));  
		return sendResponse(jsonResponse);
	}  
	
	@RequestMapping(value = "/v1.0/searchComplaintAgainst", produces={"application/json"},
			method = RequestMethod.GET)
	@ResponseBody
	public HashMap<String,Object> searchComplaintAgainstByLocation(@RequestParam("complaintAgainst") String complaintAgainst, 
			@RequestParam("locationId") String locationId) throws Exception {
		jsonResponse=new JsonResponse();
		if(complaintAgainst == null || locationId == null) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			log(clazz, "complaintAgainst or locationId Null Value", ConstantProperty.LOG_DEBUG);
			return sendResponse(jsonResponse);
		}
		try {
			Long.valueOf(locationId);

			String[] titleList = complaintManager.searchComplaintAgainstByLocation(complaintAgainst, locationId);
			jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
			jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_PROCESSED);
			jsonResponse.setTitleList(titleList);
		} catch (Exception ex) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			log(clazz, ex.getMessage(), ConstantProperty.LOG_ERROR);
			return sendResponse(jsonResponse);
		}

		return sendResponse(jsonResponse);
	}

	private ComplaintBean getComplaint(ComplaintRegistration registration) throws Exception {
		ComplaintBean complaintBean = new ComplaintBean();
		complaintBean.setId(registration.getId());
		complaintBean.setComplaintId(registration.getComplaintId());
		complaintBean.setComplaintAgainst(registration.getComplaintAgainst());
		complaintBean.setDate(registration.getCreatedDate());
		complaintBean.setState(registration.getState().getName());
		complaintBean.setDistrict(registration.getDistrict().getName());
		complaintBean.setCity(registration.getCity().getName());
		return complaintBean;
	}

	private boolean validateFiles(MultipartFile[] uploadedFiles, AttachmentDetail[] attachmentDetails, 
								  JsonResponse jsonResponse) throws Exception
	{
		boolean hasValidate=true;
		long totalFileSize=0;
		if(uploadedFiles.length>0) {
			for (int i=0;i<uploadedFiles.length;i++) {
				if(!validateFileExt(uploadedFiles[i],attachmentDetails[i],jsonResponse)) {
					hasValidate=false;
					break;
				}
				else if(totalFileSize<=ConstantProperty.MAX_FILES_LIMIT){
					totalFileSize+=uploadedFiles[i].getSize();
				}
				else {
					hasValidate=false;
					jsonResponse.setStatusCode(ConstantProperty.FILE_SIZE_LIMIT); 
					jsonResponse.setMessage(ConstantProperty.FILE_SIZE_ERROR);
					break;
				}
			}
		}
		else hasValidate=false;
		return hasValidate;
	}
	
	private boolean validateFileExt(MultipartFile uploadedFile, AttachmentDetail attachmentDetails, 
									JsonResponse jsonResponse) throws Exception
	{
		if(CommonUtil.getMatchingStrings(CommonUtil.listOfAcceptedFiles(),uploadedFile.getContentType())) {
			return true;
		}
		else {
			jsonResponse.setStatusCode(ConstantProperty.FILE_SIZE_LIMIT);
			jsonResponse.setMessage(ConstantProperty.FILE_SIZE_ERROR);
			return false;
		}
	}
	
	private ComplaintBean[] getComplaintList(ComplaintRegistration[] list) throws Exception {
		List<ComplaintBean> complaintlist = new ArrayList<ComplaintBean>();
		for (ComplaintRegistration complaintRegistration : list) {
			ComplaintBean complaintBean = new ComplaintBean();
			complaintBean.setId(complaintRegistration.getId());
			complaintBean.setComplaintId(complaintRegistration.getComplaintId());
			complaintBean.setComplaintAgainst(complaintRegistration.getComplaintAgainst());
			complaintBean.setDate(complaintRegistration.getCreatedDate());
			complaintBean.setState(complaintRegistration.getState().getName());
			complaintBean.setDistrict(complaintRegistration.getDistrict().getName());
			complaintBean.setCity(complaintRegistration.getCity().getName());
			
			complaintlist.add(complaintBean);
		}
		return complaintlist.toArray(new ComplaintBean[complaintlist.size()]);
	}
	
	public List<AttachmentBean> getAttachmentBeanFromDetail(List<AttachmentDetail> attachments) throws Exception {
		List<AttachmentBean> attachmentBeans = new ArrayList<>();
		for (AttachmentDetail attachmentDetail : attachments) {
			AttachmentBean bean = new AttachmentBean();
			bean.setAttachmentType(attachmentDetail.getAttachmentType());
			bean.setComplaintReferenceId(attachmentDetail.getComplaintReferenceId().getComplaintId());
			bean.setName(attachmentDetail.getName());
			bean.setId(attachmentDetail.getId());
			attachmentBeans.add(bean);
		}
		return attachmentBeans;
	}
	
	private JsonResponse getJsonObject(JsonResponse jsonResponse, ComplaintRegistration[] complaintList, 
			String totalCount) throws Exception
	{
		jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
		jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_PROCESSED);
		jsonResponse.setComplaintList(getComplaintList(complaintList));
		if (totalCount != null) jsonResponse.setTotalCounts(totalCount);

		return jsonResponse;
	}
	

}

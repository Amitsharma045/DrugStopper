package com.drugstopper.app.rest.authentication;


import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drugstopper.app.rest.JwtTokenFactory;
import com.drugstopper.app.rest.JwtUtil;
import com.drugstopper.app.entity.OtpTransectionDetail;
import com.drugstopper.app.entity.User;
import com.drugstopper.app.json.JsonResponse;
import com.drugstopper.app.property.ConstantProperty;
import com.drugstopper.app.util.CommonUtil;
import com.drugstopper.app.rest.RestResource;
import com.drugstopper.app.service.AuthenticationManager;
import com.drugstopper.app.service.JwtTokenManager;
import com.drugstopper.app.service.LoginUserTypeManager;
import com.drugstopper.app.service.OtpDetailManager;



@Controller
@RequestMapping(value = "/drugStopper/authentication")
public class AppAuthenticationEndPoint extends RestResource{
	
	@Autowired
	private  AuthenticationManager authenticationManager;
	
	@Autowired
	private  OtpDetailManager otpDetailManager;
	
	@Autowired
	private  JwtTokenManager jwtTokenManager;
	
	@Autowired
	private  LoginUserTypeManager loginUserTypeManager;
	
	private JsonResponse jsonResponse;
	
	@RequestMapping(value = "/v1.0/login", produces={"application/json"},
			consumes={"application/x-www-form-urlencoded"},
			method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String,Object> authenticateUser(HttpServletRequest request) throws Exception {
		String phoneNumber=request.getParameter(ConstantProperty.MOBILE_NUMBER);
		jsonResponse=new JsonResponse();
		if(phoneNumber==null || (phoneNumber.length() != 10)) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			return sendResponse(jsonResponse);
		}
		try {
			Long.parseLong(phoneNumber);
		} catch (Exception ex) {
			System.out.println(ex);
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			return sendResponse(jsonResponse);
		}
		String otp=CommonUtil.getRandomOtp();
		OtpTransectionDetail otpTransectionDetails=getOtpDetail(phoneNumber, otp);
		Long id=otpDetailManager.saveOtpDetails(otpTransectionDetails);
		if(id!=null){
			jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
			jsonResponse.setMessage(ConstantProperty.OTP_SENT);
			String messageBody="complaintBox code: "+otp+". Do not share it or"
					+ " use it elsewhere ";
			//sendSMS.sendSms(phoneNumber, messageBody);
		}
		else{
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
		}
		return sendResponse(jsonResponse);
	}

	@RequestMapping(value = "/v1.0/validate", produces = { "application/json" }, consumes = {
			"application/x-www-form-urlencoded" }, method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> validateOtp(HttpServletRequest request)
			throws Exception {
		User user = null;
		String mobileNumber = request.getParameter(ConstantProperty.MOBILE_NUMBER);
		String otp = request.getParameter(ConstantProperty.OTP);
		jsonResponse = new JsonResponse();
		OtpTransectionDetail otpTransectionDetail = otpDetailManager.getOtpDetails(otp, mobileNumber);
		if (otpTransectionDetail != null) {
			if (ValidateOtpExpiryTime(otpTransectionDetail)) {
				user=authenticationManager.getUserByMobileNumber(mobileNumber);
				if (user == null) {
					user = getUserDetail(otpTransectionDetail);
					Long id = authenticationManager.saveUser(user);
					user.setId(id);
				} else {
					user.setLastLoginDate(CommonUtil.getCurrentDate());
					user.setLoginCount(user.getLoginCount()+1);
					authenticationManager.saveUpdateUser(user);
				}
				otpDetailManager.delete(otpTransectionDetail);

				String accessKey = JwtUtil.getRandomSecretKey();
				String accessToken = JwtTokenFactory.createAccessJwtToken(String.valueOf(user.getId()), user.getUserType().getLoginType(), accessKey);

				String refreshKey = JwtUtil.getRandomSecretKey();
				String refreshToken = JwtTokenFactory.createRefreshJwtToken(String.valueOf(user.getId()), user.getUserType().getLoginType(), refreshKey);

				Long id = jwtTokenManager.createAccessAndRefreshToken(user, accessToken, accessKey, refreshToken,
						refreshKey);
				if (id != null) {
					jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
					jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_AUTHENTICATION);
					jsonResponse.setAccessToken(accessToken);
					jsonResponse.setRefreshToken(refreshToken);
				}
			} else {
				jsonResponse.setStatusCode(ConstantProperty.OTP_EXPIRED);
				jsonResponse.setMessage(ConstantProperty.OTP_EXPIRED_MESSAGE);
			}
		} else {
			jsonResponse.setStatusCode(ConstantProperty.UNAUTHORIZED);
			jsonResponse.setMessage(ConstantProperty.OTP_VALIDATION_FAILED);
		}
		return sendResponse(jsonResponse);

	}

	private boolean ValidateOtpExpiryTime(OtpTransectionDetail otpTransectionDetails){
		Date currentDate=CommonUtil.getCurrentDate();
		Date otpExpiryDate=otpTransectionDetails.getExpirytimeStamp();
		return otpExpiryDate.after(currentDate);
	}
	
	public OtpTransectionDetail getOtpDetail(String mobileNumber, String otp){
		OtpTransectionDetail otpTransectionDetails=new OtpTransectionDetail();
		otpTransectionDetails.setMobileNumber(mobileNumber);
		otpTransectionDetails.setOtp(otp);
		otpTransectionDetails.setExpirytimeStamp(CommonUtil.getExpiryDate());
		
		return otpTransectionDetails;
	}
	
	public User getUserDetail(OtpTransectionDetail otpTransectionDetails){
		User user=new User();
		user.setActive(1);
		user.setLoginCount(1);
		user.setLoginDate(CommonUtil.getCurrentDate());
		user.setUserType(loginUserTypeManager.getLoginType(ConstantProperty.MOBILE_SIGN_UP));
		user.setUserId(otpTransectionDetails.getMobileNumber());
		return user;
	}

}

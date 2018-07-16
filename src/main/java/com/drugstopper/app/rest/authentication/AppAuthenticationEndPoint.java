package com.drugstopper.app.rest.authentication;


import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;


import com.drugstopper.app.rest.JwtTokenFactory;
import com.drugstopper.app.rest.JwtUtil;
import com.drugstopper.app.entity.LoginUserType;
import com.drugstopper.app.entity.OtpTransectionDetail;
import com.drugstopper.app.entity.User;
import com.drugstopper.app.facebook.FBConnection;
import com.drugstopper.app.facebook.FBGraph;
import com.drugstopper.app.json.JsonResponse;
import com.drugstopper.app.property.ConstantProperty;
import com.drugstopper.app.util.CommonUtil;
import com.drugstopper.app.rest.RestResource;
import com.drugstopper.app.service.AuthenticationManager;
import com.drugstopper.app.service.JwtTokenManager;
import com.drugstopper.app.service.LoginUserTypeManager;
import com.drugstopper.app.service.OtpDetailManager;
import com.drugstopper.app.smsapi.sendSMS;

/**
 * @author rpsingh
 *
 */
@Controller
@RequestMapping(value = "/authentication")
public class AppAuthenticationEndPoint extends RestResource {
	
	private Class clazz = AppAuthenticationEndPoint.class;
	
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
			consumes={"multipart/form-data"},
			method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String,Object> authenticateUser(HttpServletRequest request) throws Exception {
		String phoneNumber = request.getParameter(ConstantProperty.MOBILE_NUMBER);
		jsonResponse = new JsonResponse();
		HashMap<String, String> smsStatus = null;
		String otp = CommonUtil.getRandomOtp();
		try {
			Long.valueOf(phoneNumber);
			OtpTransectionDetail otpTransectionDetails = getOtpDetail(phoneNumber, otp);
			Long id = otpDetailManager.saveOtpDetails(otpTransectionDetails);
			if(id != null) {
				jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
				jsonResponse.setMessage(ConstantProperty.OTP_SENT);
				String messageBody = "Silent Reaction OTP Verification Code: "+otp+". Do not share it or"
						+ " use it elsewhere ";
				smsStatus = sendSMS.sendSms(phoneNumber, messageBody);
				if(!smsStatus.get(ConstantProperty.STATUS_CODE).equals(ConstantProperty.OK_STATUS)) {
					jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
					jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
					log(clazz, "OTP NOT SENT", ConstantProperty.LOG_ERROR);

				}
			}
			else {
				jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
				jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
				log(clazz, "NOT ABLE TO GENERATE OTP", ConstantProperty.LOG_TRACE);

			}
		} catch(Exception ex) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			log(clazz, ex.getMessage(), ConstantProperty.LOG_ERROR);
			return sendResponse(jsonResponse);
		}
		return sendResponse(jsonResponse);
	}

	@RequestMapping(value = "/v1.0/validate", produces = { "application/json" }, consumes = {
			"multipart/form-data" }, method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> validateOtp(HttpServletRequest request)
			throws Exception {
		User user = null;
		String mobileNumber = request.getParameter(ConstantProperty.MOBILE_NUMBER);
		String otp = request.getParameter(ConstantProperty.OTP);
		jsonResponse = new JsonResponse();
		try {
			OtpTransectionDetail otpTransectionDetail = otpDetailManager.getOtpDetails(otp, mobileNumber);
			if (otpTransectionDetail != null) {
				if (ValidateOtpExpiryTime(otpTransectionDetail)) {
					user = authenticationManager.getUserById(CommonUtil.getEncryptUserDetail(mobileNumber));
					if (user == null) {
						user = getUserDetail(CommonUtil.getEncryptUserDetail(mobileNumber), loginUserTypeManager.getLoginType(ConstantProperty.MOBILE_SIGN_UP));
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

					Long id = jwtTokenManager.createAccessToken(user, accessToken, accessKey);
					if (id != null) {
						jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
						jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_AUTHENTICATION);
						jsonResponse.setAccessToken(accessToken);
					} else {
						jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
						jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
						log(clazz, "NOT ABLE TO GENERATE ACCESS_TOKEN", ConstantProperty.LOG_DEBUG);
						return sendResponse(jsonResponse);
					}
				} else {
					jsonResponse.setStatusCode(ConstantProperty.UNAUTHORIZED);
					jsonResponse.setMessage(ConstantProperty.OTP_EXPIRED_MESSAGE);
					log(clazz, ConstantProperty.OTP_EXPIRED_MESSAGE, ConstantProperty.LOG_DEBUG);

				}
			} else {
				jsonResponse.setStatusCode(ConstantProperty.UNAUTHORIZED);
				jsonResponse.setMessage(ConstantProperty.OTP_VALIDATION_FAILED);
				log(clazz, ConstantProperty.OTP_VALIDATION_FAILED, ConstantProperty.LOG_DEBUG);

			} 
		} catch(Exception ex) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			log(clazz, ex.getMessage(), ConstantProperty.LOG_ERROR);
			return sendResponse(jsonResponse);
		}
		return sendResponse(jsonResponse);
	}

	@RequestMapping(value = "/v1.0/sso/google", produces={"application/json"},
			consumes={"multipart/form-data"},
			method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String,Object> authenticateUserWithGoogle(HttpServletRequest request) throws Exception {
		jsonResponse = new JsonResponse();
		String token = request.getParameter(ConstantProperty.GMAIL_SIGNIN_TOKEN);
		try {
			final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
			final JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
					.setAudience(Collections.singletonList(ConstantProperty.CLIENT_ID))
					.build();
			final GoogleIdToken googleIdToken = verifier.verify(token);

			if (googleIdToken == null) {
				jsonResponse.setStatusCode(ConstantProperty.UNAUTHORIZED);
				jsonResponse.setMessage(ConstantProperty.AUTHENTICATION_FAILED); 
				log(clazz, ConstantProperty.AUTHENTICATION_FAILED, ConstantProperty.LOG_DEBUG);
				return sendResponse(jsonResponse);
			}
			final Payload payload = googleIdToken.getPayload();
			final Boolean emailVerified = payload.getEmailVerified();
			String emailId = payload.getEmail();
			User user = null;
			if (emailVerified) {
				if (emailId == null) {
					jsonResponse.setStatusCode(ConstantProperty.UNAUTHORIZED);
					jsonResponse.setMessage(ConstantProperty.EMAIL_NOT_PRESENT_IN_ACCESS_CODE); 
					log(clazz, ConstantProperty.EMAIL_NOT_PRESENT_IN_ACCESS_CODE, ConstantProperty.LOG_DEBUG);
					return sendResponse(jsonResponse);
				}
				user = authenticationManager.getUserById(CommonUtil.getEncryptUserDetail(emailId));
				if (user == null) {
					user = getUserDetail(CommonUtil.getEncryptUserDetail(emailId), loginUserTypeManager.getLoginType(ConstantProperty.GOOGLE_SIGN_UP));
					Long id = authenticationManager.saveUser(user);
					user.setId(id);
				} else {
					user.setLastLoginDate(CommonUtil.getCurrentDate());
					user.setLoginCount(user.getLoginCount()+1);
					user.setUserType(loginUserTypeManager.getLoginType(ConstantProperty.GOOGLE_SIGN_UP));
					authenticationManager.saveUpdateUser(user);
				}
				String accessKey = JwtUtil.getRandomSecretKey();
				String accessToken = JwtTokenFactory.createAccessJwtToken(String.valueOf(user.getId()), user.getUserType().getLoginType(), accessKey);

				Long id = jwtTokenManager.createAccessToken(user, accessToken, accessKey);
				if (id != null) {
					jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
					jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_AUTHENTICATION);
					jsonResponse.setAccessToken(accessToken);
				} else {
					jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
					jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
					log(clazz, "NOT ABLE TO GENERATE ACCESS_TOKEN", ConstantProperty.LOG_DEBUG);
				}
			} else {
				jsonResponse.setStatusCode(ConstantProperty.UNAUTHORIZED);
				jsonResponse.setMessage(ConstantProperty.AUTHENTICATION_FAILED);
				log(clazz, ConstantProperty.AUTHENTICATION_FAILED, ConstantProperty.LOG_DEBUG);
			}
		} catch(Exception ex) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			log(clazz, ex.getMessage(), ConstantProperty.LOG_ERROR);
			return sendResponse(jsonResponse);
		}
		return sendResponse(jsonResponse);
	}
	
	@RequestMapping(value = "/v1.0/sso/facebook", produces={"application/json"},
			consumes={"multipart/form-data"},
			method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String,Object> authenticateUserWithFacebook(HttpServletRequest request) throws Exception {
		jsonResponse = new JsonResponse();
		String token = request.getParameter(ConstantProperty.FACEBOOK_SIGNIN_TOKEN);
		if (token == null || token.equals("")) {
			jsonResponse.setStatusCode(ConstantProperty.UNAUTHORIZED);
			jsonResponse.setMessage(ConstantProperty.FACEBOOK_CODE_NOT_PRESENT); 
			log(clazz, ConstantProperty.FACEBOOK_CODE_NOT_PRESENT, ConstantProperty.LOG_DEBUG);
			return sendResponse(jsonResponse);
		}
		try {
			FBConnection fbConnection = new FBConnection();
			String accessToken = fbConnection.getAccessToken(token);

			FBGraph fbGraph = new FBGraph(accessToken);
			String graph = fbGraph.getFBGraph();
			Map<String, String> fbProfileData = fbGraph.getGraphData(graph);
			String emailId = fbProfileData.get("email");
			if (emailId == null) {
				jsonResponse.setStatusCode(ConstantProperty.UNAUTHORIZED);
				jsonResponse.setMessage(ConstantProperty.EMAIL_NOT_PRESENT_IN_ACCESS_CODE);
				log(clazz, ConstantProperty.EMAIL_NOT_PRESENT_IN_ACCESS_CODE, ConstantProperty.LOG_DEBUG);
				return sendResponse(jsonResponse);
			}
			User user = authenticationManager.getUserById(CommonUtil.getEncryptUserDetail(emailId));
        	if (user == null) {
				user = getUserDetail(CommonUtil.getEncryptUserDetail(emailId), loginUserTypeManager.getLoginType(ConstantProperty.FACEBOOK_SIGN_UP));
				Long id = authenticationManager.saveUser(user);
				user.setId(id);
			} else {
				user.setLastLoginDate(CommonUtil.getCurrentDate());
				user.setLoginCount(user.getLoginCount()+1);
				user.setUserType(loginUserTypeManager.getLoginType(ConstantProperty.FACEBOOK_SIGN_UP));
				authenticationManager.saveUpdateUser(user);
			}
        	String accessKey = JwtUtil.getRandomSecretKey();
			String appAccessToken = JwtTokenFactory.createAccessJwtToken(String.valueOf(user.getId()), user.getUserType().getLoginType(), accessKey);

			Long id = jwtTokenManager.createAccessToken(user, appAccessToken, accessKey);
			if (id != null) {
				jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
				jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_AUTHENTICATION);
				jsonResponse.setAccessToken(appAccessToken);
			} else {
				jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
				jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
				log(clazz, "NOT ABLE TO GENERATE ACCESS_TOKEN", ConstantProperty.LOG_DEBUG);
			}
		} catch (Exception ex) {
			jsonResponse.setStatusCode(ConstantProperty.UNAUTHORIZED);
			jsonResponse.setMessage(ex.getMessage()); 
			log(clazz, ex.getMessage(), ConstantProperty.LOG_ERROR);
			return sendResponse(jsonResponse);
		}
		
        return sendResponse(jsonResponse);
	}


	private boolean ValidateOtpExpiryTime(OtpTransectionDetail otpTransectionDetails) throws Exception {
		Date currentDate = CommonUtil.getCurrentDate();
		Date otpExpiryDate = otpTransectionDetails.getExpirytimeStamp();
		return otpExpiryDate.after(currentDate);
	}
	
	public OtpTransectionDetail getOtpDetail(String mobileNumber, String otp) throws Exception {
		OtpTransectionDetail otpTransectionDetails = new OtpTransectionDetail();
		otpTransectionDetails.setMobileNumber(mobileNumber);
		otpTransectionDetails.setOtp(otp);
		otpTransectionDetails.setExpirytimeStamp(CommonUtil.getExpiryDate());
		return otpTransectionDetails;
	}
	
	public User getUserDetail(String userId, LoginUserType type) throws Exception {
		User user = new User();
		user.setActive(1);
		user.setLoginCount(1);
		user.setLoginDate(CommonUtil.getCurrentDate());
		user.setUserType(type);
		user.setUserId(userId);
		return user;
	}
}

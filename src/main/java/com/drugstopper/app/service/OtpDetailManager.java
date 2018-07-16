package com.drugstopper.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drugstopper.app.dao.OtpTransectionDetailDAO;
import com.drugstopper.app.entity.OtpTransectionDetail;

/**
 * @author rpsingh
 *
 */
@Service
public class OtpDetailManager {

	@Autowired
	private  OtpTransectionDetailDAO otpTransectionDetailDAO;

	public Long saveOtpDetails(OtpTransectionDetail otp) throws Exception {
		return otpTransectionDetailDAO.saveOtpDetails(otp);
	}
	
	public OtpTransectionDetail getOtpDetails(String otp,String mobileNumber) throws Exception {
		return otpTransectionDetailDAO.getOtpDetails(otp, mobileNumber);
	}
	
	public  boolean delete(OtpTransectionDetail otpTransectionDetail) throws Exception {
		return otpTransectionDetailDAO.delete(otpTransectionDetail);
	}
}

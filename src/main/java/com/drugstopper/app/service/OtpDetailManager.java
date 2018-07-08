package com.drugstopper.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drugstopper.app.dao.OtpTransectionDetailDAO;
import com.drugstopper.app.entity.OtpTransectionDetail;

@Service
public class OtpDetailManager {

	@Autowired
	private  OtpTransectionDetailDAO otpTransectionDetailDAO;

	public Long saveOtpDetails(OtpTransectionDetail otp) {
		return otpTransectionDetailDAO.saveOtpDetails(otp);
	}
	
	public OtpTransectionDetail getOtpDetails(String otp,String mobileNumber) {
		return otpTransectionDetailDAO.getOtpDetails(otp, mobileNumber);
	}
	
	public  boolean delete(OtpTransectionDetail otpTransectionDetail) {
		return otpTransectionDetailDAO.delete(otpTransectionDetail);
	}
}

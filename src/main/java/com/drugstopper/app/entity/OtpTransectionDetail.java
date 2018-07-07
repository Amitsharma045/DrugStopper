package com.drugstopper.app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author rpsingh
 *
 */

@Entity
@Table(name = "Otp_Detail")
public class OtpTransectionDetail  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "OTD_UserName")
	private String userName;
	@Column(name = "OTD_MobileNumber")
	private String mobileNumber;
	@Column(name = "OTD_otp")
	private String otp;
	@Column(name = "OTD_ExpirytimeStamp")
	private Date expirytimeStamp;

	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public Date getExpirytimeStamp() {
		return expirytimeStamp;
	}
	public void setExpirytimeStamp(Date expirytimeStamp) {
		this.expirytimeStamp = expirytimeStamp;
	}


}

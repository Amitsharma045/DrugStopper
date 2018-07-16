package com.drugstopper.app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author rpsingh
 *
 */

@Entity
@Table(name = "User_Table")
public class User extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "UT_UserType", referencedColumnName = "id")
	private LoginUserType userType;
	
	@Column(name = "UT_UserId")
	private String userId;
	
	@Column(name = "UT_Active")
	private int active;
	
	@Column(name = "UT_LoginDate")
	private Date loginDate;

	@Column(name = "UT_LastLoginDate")
	private Date lastLoginDate;
	
	@Column(name = "UT_LoginCount")
	private int loginCount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LoginUserType getUserType() {
		return userType;
	}

	public void setUserType(LoginUserType userType) {
		this.userType = userType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int isActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}


}


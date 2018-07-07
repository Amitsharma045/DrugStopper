package com.drugstopper.app.entity;

import java.io.Serializable;

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
@Table(name = "Jwt_Token")
public class JwtToken  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "JWT_AccessKey")
	private String accessKey;
	@Column(name = "JWT_AccessToken")
	private String accessToken;
	@Column(name = "JWT_RefreshKey")
	private String refreshKey;
	@Column(name = "JWT_RefreshToken")
	private String refreshToken;
	@ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "JWT_UserId", referencedColumnName = "id")
	private UserTable user;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public UserTable getUser() {
		return user;
	}
	public void setUser(UserTable user) {
		this.user = user;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getRefreshKey() {
		return refreshKey;
	}
	public void setRefreshKey(String refreshKey) {
		this.refreshKey = refreshKey;
	}

}

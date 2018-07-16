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
@Table(name = "Complaint_Registration")
public class ComplaintRegistration extends BaseEntity   implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "CR_ComplaintId")
	private String complaintId;
	
	@Column(name = "CR_Date")
	private Date date;
	
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "CR_State", referencedColumnName = "id")
	private Location state;
	
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "CR_District", referencedColumnName = "id")
	private Location district;
	
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "CR_City", referencedColumnName = "id")
	private Location city;
	
	@Column(name = "CR_Address")
	private String address;
	
	@Column(name = "CR_ComplaintTitle")
	private String complaintTitle;
	
	@Column(name = "CR_ComplaintAgainst")
	private String complaintAgainst;
	
	@Column(name = "CR_ComplaintDescription")
	private String complaintDescription;
	
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "CR_User", referencedColumnName = "id")
	private User user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(String complaintId) {
		this.complaintId = complaintId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Location getState() {
		return state;
	}

	public void setState(Location state) {
		this.state = state;
	}

	public Location getDistrict() {
		return district;
	}

	public void setDistrict(Location district) {
		this.district = district;
	}

	public Location getCity() {
		return city;
	}

	public void setCity(Location city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getComplaintTitle() {
		return complaintTitle;
	}

	public void setComplaintTitle(String complaintTitle) {
		this.complaintTitle = complaintTitle;
	}

	public String getComplaintAgainst() {
		return complaintAgainst;
	}

	public void setComplaintAgainst(String complaintAgainst) {
		this.complaintAgainst = complaintAgainst;
	}

	public String getComplaintDescription() {
		return complaintDescription;
	}

	public void setComplaintDescription(String complaintDescription) {
		this.complaintDescription = complaintDescription;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		return "ComplaintRegistration [id=" + id + ", complaintId=" + complaintId + ", date=" + date + ", state="
				+ state + ", district=" + district + ", city=" + city + ", address=" + address + ", complaintTitle="
				+ complaintTitle + ", complaintAgainst=" + complaintAgainst + ", complaintDescription="
				+ complaintDescription + ", user=" + user + "]";
	}
}

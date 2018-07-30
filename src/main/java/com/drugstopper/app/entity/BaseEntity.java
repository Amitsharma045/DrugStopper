package com.drugstopper.app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * @author rpsingh
 *
 */
@MappedSuperclass
public class BaseEntity  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "CreatedDate" , updatable = false)
	@CreationTimestamp
	private Date createdDate;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	@Column(name = "ModifiedDate")
	@UpdateTimestamp
	private Date modifiedDate;
	
	@Column(name = "ModifiedBy")
	@LastModifiedBy
	private String modifiedBy;
	
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	
	@PreUpdate
	public void setModifiedDateUpdate() {  this.modifiedDate = new Date(); }

}

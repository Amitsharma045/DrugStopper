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
@Table(name = "Attachment_Detail")
public class AttachmentDetail extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "AD_ComplaintReferenceId", referencedColumnName = "id")
	private ComplaintRegistration complaintReferenceId;
	
	@Column(name = "Attachment_Type")
	private String attachmentType;

	@Column(name = "AD_Name")
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ComplaintRegistration getComplaintReferenceId() {
		return complaintReferenceId;
	}

	public void setComplaintReferenceId(ComplaintRegistration complaintReferenceId) {
		this.complaintReferenceId = complaintReferenceId;
	}

	public String getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}

package com.drugstopper.app.bean;

public class AttachmentBean {
	private long id;
	private String complaintReferenceId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getComplaintReferenceId() {
		return complaintReferenceId;
	}
	public void setComplaintReferenceId(String complaintReferenceId) {
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
	private String attachmentType;
	private String name;
}

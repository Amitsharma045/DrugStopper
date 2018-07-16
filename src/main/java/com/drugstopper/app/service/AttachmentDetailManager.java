package com.drugstopper.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.drugstopper.app.dao.AttachmentDetailDao;
import com.drugstopper.app.entity.AttachmentDetail;
import com.drugstopper.app.entity.ComplaintRegistration;

@Service
public class AttachmentDetailManager {
	@Autowired
	private AttachmentDetailDao attachmentDao;

	public AttachmentDetail[] saveAttachments(ComplaintRegistration savedComplaint, AttachmentDetail[] attachmentDetails, MultipartFile[] uploadedFiles) {
		// TODO Auto-generated method stub
		return attachmentDao.saveAttachments(savedComplaint,attachmentDetails,uploadedFiles);
	}
	
	public List<AttachmentDetail> getAttachments(String complaintId) {
		return attachmentDao.getAttachments(complaintId);
	}
		
}

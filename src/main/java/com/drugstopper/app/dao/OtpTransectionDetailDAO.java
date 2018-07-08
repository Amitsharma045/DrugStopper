package com.drugstopper.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drugstopper.app.entity.OtpTransectionDetail;

@Repository
@Transactional
public class OtpTransectionDetailDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public Long saveOtpDetails(OtpTransectionDetail otp){
		Long id=(Long) getSession().save(otp);
		return id;
	}
	
	@SuppressWarnings("unchecked")
	public OtpTransectionDetail getOtpDetails(String otp,String mobileNumber){
		List<OtpTransectionDetail> otpTransectionDetailslist=getSession()
				.createQuery("from OtpTransectionDetail od where od.mobileNumber=:p1 and od.otp=:p2")
				.setParameter("p1", mobileNumber)
				.setParameter("p2", otp).list();

		if(otpTransectionDetailslist.size()!=0){
			return otpTransectionDetailslist.get(0);
		}
		return null;
	}
	
	public  boolean delete(OtpTransectionDetail otpTransectionDetail) {
		getSession().delete(otpTransectionDetail);
		return true;
	}

}

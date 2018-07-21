package com.drugstopper.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.drugstopper.app.entity.ComplaintRegistration;
import com.drugstopper.app.entity.Location;
import com.drugstopper.app.util.CommonUtil;
/**
 * @author rpsingh
 *
 */

@Repository
@Transactional
public class ComplaintDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	public ComplaintRegistration[] getAllComplaints(String complaintId) throws Exception { 
		String query = "FROM ComplaintRegistration cr WHERE cr.id <:p1 ORDER BY cr.id DESC";
		if(complaintId.equals("0")) {
			query = "FROM ComplaintRegistration cr ORDER BY cr.id DESC";
		}
		
		Query querySql = getSession().createQuery(query);
		if(!complaintId.equals("0")) querySql.setParameter("p1", Long.valueOf(complaintId));
		
		List<ComplaintRegistration> complaintList =querySql.setFirstResult(0).setMaxResults(10).list();
		return complaintList.toArray(new ComplaintRegistration[complaintList.size()]);
	}
	
	@SuppressWarnings("unchecked")
	public ComplaintRegistration[] getComplaintsByState(String stateId, String complaintId) throws Exception {
		String query = "FROM ComplaintRegistration cr WHERE cr.id <:p1 AND cr.state.id =:p2 ORDER BY cr.id DESC";
		if(complaintId.equals("0")) {
			query = "FROM ComplaintRegistration cr WHERE cr.state.id =:p2 ORDER BY cr.id DESC";
		}
		Query querySql = getSession().createQuery(query).setParameter("p2", Long.valueOf(stateId));
		if(!complaintId.equals("0")) querySql.setParameter("p1", Long.parseLong(complaintId));
		
		List<ComplaintRegistration> complaintList =querySql.setFirstResult(0).setMaxResults(10).list();
		return complaintList.toArray(new ComplaintRegistration[complaintList.size()]);
	}
	
	@SuppressWarnings("unchecked")
	public ComplaintRegistration[] getComplaintsByDistrict(String districtId, String complaintId) throws Exception {
		String query = "FROM ComplaintRegistration cr WHERE cr.district.id =:p1  AND cr.id <:p2 ORDER BY cr.id DESC ";
		if(complaintId.equals("0")) {
			query = "FROM ComplaintRegistration cr WHERE cr.district.id =:p1 ORDER BY cr.id DESC";
		}
		Query querySql = getSession().createQuery(query).setParameter("p1", Long.valueOf(districtId));
		if(!complaintId.equals("0")) querySql.setParameter("p2", Long.parseLong(complaintId));
		
		List<ComplaintRegistration> complaintList =querySql.setFirstResult(0).setMaxResults(10).list();
		return complaintList.toArray(new ComplaintRegistration[complaintList.size()]);
	}

	@SuppressWarnings("unchecked")
	public ComplaintRegistration[] getComplaintsByCity(String cityId, String complaintId) throws Exception {
		String query = "FROM ComplaintRegistration cr WHERE cr.city.id =:p1 AND cr.id <:p2 ORDER BY cr.id DESC";
		if(complaintId.equals("0")) {
			query = "FROM ComplaintRegistration cr WHERE cr.city.id =:p1 ORDER BY cr.id DESC";
		}
		Query querySql = getSession().createQuery(query).setParameter("p1", Long.valueOf(cityId));
		if(!complaintId.equals("0")) querySql.setParameter("p2", Long.parseLong(complaintId));
		
		List<ComplaintRegistration> complaintList =querySql.setFirstResult(0).setMaxResults(10).list();
		return complaintList.toArray(new ComplaintRegistration[complaintList.size()]);
	}
	
	@SuppressWarnings("unchecked")
	public String[] searchComplaintAgainstByLocation(String complaintAgainst, String locationId) throws Exception {
		List<String> titleList = getSession()
								.createQuery("SELECT cr.complaintAgainst FROM ComplaintRegistration cr WHERE cr.city.id =:p1"
							   			   + " AND  UPPER(cr.complaintAgainst) LIKE :p2")
								.setParameter("p1", Long.valueOf(locationId))
								.setParameter("p2", complaintAgainst.toUpperCase()+"%")
								.list();

		return titleList.toArray(new String[titleList.size()]);
	}
	public int getTotalComplaintCount() throws Exception {
		int count =  ( (Long) getSession().createQuery("SELECT COUNT(1) FROM ComplaintRegistration").iterate().next() ).intValue();
		return count;
	}
	
	public int getTotalComplaintCountByState(String cityId) throws Exception {
		int count =  ( (Long) getSession().createQuery("SELECT COUNT(1) FROM ComplaintRegistration cr WHERE cr.state.id =:p1")
										  .setParameter("p1", Long.valueOf(cityId)).iterate().next() ).intValue();
		return count;
	}
	
	public int getTotalComplaintCountByDistrict(String districtId) throws Exception {
		int count =  ( (Long) getSession().createQuery("SELECT COUNT(1) FROM ComplaintRegistration cr WHERE cr.district.id =:p1")
										  .setParameter("p1", Long.valueOf(districtId)).iterate().next() ).intValue();
		return count;
	}
	
	public int getTotalComplaintCountByCity(String cityId) throws Exception {
		int count =  ( (Long) getSession().createQuery("SELECT COUNT(1) FROM ComplaintRegistration cr WHERE cr.city.id =:p1")
										  .setParameter("p1", Long.valueOf(cityId)).iterate().next() ).intValue();
		return count;
	}
	
	public ComplaintRegistration saveComplaints(ComplaintRegistration complaintRegistration) throws Exception {
		complaintRegistration.setState((Location)getSession().get(Location.class, complaintRegistration.getState().getId()));
		complaintRegistration.setDistrict((Location)getSession().get(Location.class, complaintRegistration.getDistrict().getId()));
		complaintRegistration.setCity((Location)getSession().get(Location.class, complaintRegistration.getCity().getId()));
		complaintRegistration.setComplaintId(CommonUtil.createComplaintId(complaintRegistration.getState(),complaintRegistration.getDistrict(),complaintRegistration.getCity()));
		complaintRegistration.setId((long) (getSession().save(complaintRegistration)));
		return complaintRegistration;
	}

	public ComplaintRegistration getComplaint(String complaintId) throws Exception {
		ComplaintRegistration complaintRegistration=(ComplaintRegistration) getSession().createQuery("FROM ComplaintRegistration cr WHERE cr.complaintId =:p1")
																						.setParameter("p1", complaintId).list().get(0);
		 return complaintRegistration;
	}
}

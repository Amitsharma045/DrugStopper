package com.drugstopper.app.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import com.drugstopper.app.entity.Location;
import com.drugstopper.app.property.ConstantProperty;
import com.google.common.net.MediaType;


public class CommonUtil {

	private static final String DELIM = "-";

	public static String getRandomOtp(){
		Random rand = new Random();
		return String.format("%04d", rand.nextInt(10000));
	} 
	
	public static Date getExpiryDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}
	
	public static Date getCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}
	
	public static boolean isEmpty(String s, boolean trim) {
		if (s != null) {
			if (trim) return (s.trim().length() == 0);
			return (s.length() == 0);
		} else {
			return true;
		}
	}
	
	public static  boolean ValidateExpiryTime(Date currentDate,Date expiryDate){
		return expiryDate.after(currentDate);
	}
	
	public static String getYearMonth() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.get(Calendar.MONTH);
		return String.valueOf(calendar.get(Calendar.YEAR))+calendar.get(Calendar.MONTH);
	}
	
	public static String getRandomNumber(){
		Random rand = new Random();
		return String.format("%04d", rand.nextInt(10000));
	}
	
	public static String createComplaintId(Location state, Location district, Location city) {
		String complaintId=state.getShortName()+DELIM+district.getShortName()+DELIM+city.getShortName()+DELIM;
		complaintId+=getRandomNo();
		return complaintId;
	}

	private static Integer getRandomNo() {
		return new Random().nextInt(ConstantProperty.MAX_RANDOM_NUM) + ConstantProperty.MIN_RANDOM_NUM;
	}
	
	public static List<MediaType> listOfAcceptedFiles(){
		List<MediaType> acceptedLists= new ArrayList<>();
		acceptedLists.add(MediaType.MICROSOFT_WORD);
		acceptedLists.add(MediaType.ANY_AUDIO_TYPE);
		acceptedLists.add(MediaType.ANY_VIDEO_TYPE);
		acceptedLists.add(MediaType.MICROSOFT_EXCEL);
		acceptedLists.add(MediaType.JPEG);
		acceptedLists.add(MediaType.BMP);
		acceptedLists.add(MediaType.PNG);
		acceptedLists.add(MediaType.PDF);
		return acceptedLists;
	}
	
	public static boolean getMatchingStrings(List<MediaType> list, String regex) {
		  for (MediaType s:list) {
		    if(MediaType.parse(regex).is(s))
		    	return true;
		  }

		  return false;
		}
}

package com.drugstopper.app.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class CommonUtil {

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
}

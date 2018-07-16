package com.drugstopper.app.smsapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.drugstopper.app.property.ConstantProperty;
 
/**
 * @author rpsingh
 *
 */
public class sendSMS {
	public static HashMap<String,String> sendSms(String mobileNumber,String messageBody) {
		HashMap<String,String> smsStatus = new HashMap<String,String>();
		try {
			// Construct data
			String apiKey = "apikey=" + "vTHO0++oVmc-7UI5RTCDMHzoTU04iLUzuyg7CjD1Oh";
			String message = "&message=" + messageBody;
			String sender = "&sender=" + "SILENT";
			String numbers = "&numbers=" + mobileNumber;
			
			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
			String data = apiKey + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
			System.out.println("SMS Response : "+stringBuffer.toString());
			smsStatus.put(ConstantProperty.STATUS_CODE, ConstantProperty.OK_STATUS);
			smsStatus.put(ConstantProperty.MESSAGE, stringBuffer.toString());
			return smsStatus;
		} catch (Exception e) {
			System.out.println("exception came");
			System.out.println("SMS Response : "+e.getMessage());

			smsStatus.put(ConstantProperty.STATUS_CODE, ConstantProperty.SERVER_ERROR);
			smsStatus.put(ConstantProperty.MESSAGE, e.getMessage());
			return smsStatus;
		}
	}
	/* public static void main( String[] args ){ 
		 System.out.println(sendSms());
	 }*/
}
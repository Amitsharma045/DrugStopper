package com.drugstopper.app.rest;

import java.util.Date;
import java.util.UUID;


import com.drugstopper.app.util.CommonUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @author rpsingh
 *
 */
public class JwtUtil {

	
	
	  public static void deleteJwtSession(String accessToken) throws Exception {
		  
	  }
	  
	  public static String getRandomSecretKey() throws Exception {
		  UUID uid = UUID.randomUUID();
		  return uid.toString();
	  }
	  
	  public static boolean validateAccessToken(Date expirydate) throws Exception {
		  if(CommonUtil.ValidateExpiryTime(CommonUtil.getCurrentDate(),
				  							expirydate))
		  {
			  return true;
		  }
		  return false;
	  }
	  
	  public static Claims parseJwtToken(String token, String signingKey) throws Exception {
		  return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
	  }

	  public static final String AUTHENTICATION_SCHEME  = "Bearer";
	  public static final String REFRESH_TOKEN  = "refreshToken";

}

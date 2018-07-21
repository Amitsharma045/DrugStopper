package com.drugstopper.app.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import com.drugstopper.app.entity.JwtToken;
import com.drugstopper.app.property.ConstantProperty;
import com.drugstopper.app.resources.ComplaintResource;
import com.drugstopper.app.rest.JwtTokenFactory;
import com.drugstopper.app.rest.JwtUtil;
import com.drugstopper.app.service.JwtTokenManager;
import com.drugstopper.app.util.CommonUtil;

import io.jsonwebtoken.Claims;
/**
 * @author rpsingh
 *
 */
@WebFilter(urlPatterns = "/drugstopper/api/*")
public class AppAuthenticationFilter implements Filter {
 
	private Class clazz = AppAuthenticationFilter.class;

	@Autowired
	private  JwtTokenManager jwtTokenManager;
	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpservletRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpservletResponse = (HttpServletResponse) servletResponse;
		try {
			// Get the Authorization header from the request
			String authorizationHeader = httpservletRequest.getHeader(HttpHeaders.AUTHORIZATION);
			System.out.println("filter hit");
			if (!isTokenBasedAuthentication(authorizationHeader)) {
				abortWithErrorStatus(httpservletResponse, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token type");
				return;
			}
			String token = authorizationHeader.substring(JwtUtil.AUTHENTICATION_SCHEME.length()+1).trim();
			System.out.println("access Token :: " + token);
			String appUserId = null, role=null;
			try {
				JwtToken jwt = jwtTokenManager.getJwtTokenByAccessToken(token);
				if(jwt != null){
					Claims claims = JwtUtil.parseJwtToken(token, jwt.getAccessKey());
					appUserId = claims.getSubject();
					role = claims.get("role").toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
				abortWithErrorStatus(httpservletResponse, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
			}

			if (!CommonUtil.isEmpty(appUserId, true)) {
				httpservletRequest.setAttribute("appUserId", appUserId);
				httpservletRequest.setAttribute("role", role);
			}
			else{
				abortWithErrorStatus(httpservletResponse, HttpServletResponse.SC_UNAUTHORIZED, "InValid Token");
				return;
			}
			filterChain.doFilter(servletRequest, servletResponse);
		} catch(Exception ex) {
			abortWithErrorStatus(httpservletResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
			log(clazz, ex.getMessage(), ConstantProperty.LOG_ERROR);
			return;

		}
	}
 
    
    public void destroy() {
 
    }

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isTokenBasedAuthentication(String authorizationHeader) {
		// Check if the Authorization header is valid
		// It must not be null and must be prefixed with "Bearer" plus a whitespace
		// The authentication scheme comparison must be case-insensitive
		return authorizationHeader != null && authorizationHeader.toLowerCase().
				startsWith(JwtUtil.AUTHENTICATION_SCHEME.toLowerCase());
	}

	private void abortWithErrorStatus(HttpServletResponse httpservletResponse, int status, 
			String errorMsg) throws IOException 
	{
		// Abort the filter chain with a 401 status code response
		// The WWW-Authenticate header is sent along with the response
		httpservletResponse.sendError(status,errorMsg);
	}
	
	private void log(Class clazz, String message, String tag) {
		Logger logger = LoggerFactory.getLogger(clazz);
		if(ConstantProperty.LOG_INFO.equals(tag)) {
			logger.info(message);
		} else if(ConstantProperty.LOG_WARNING.equals(tag)) {
			logger.warn(message);
		} else if(ConstantProperty.LOG_ERROR.equals(tag)) {
			logger.error(message);
		} else if(ConstantProperty.LOG_DEBUG.equals(tag)) {
			logger.debug(message);
		} else {
			logger.trace(message);
		}
	}
	
}

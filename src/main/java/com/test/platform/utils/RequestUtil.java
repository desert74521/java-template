package com.test.platform.utils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.platform.accesstoken.AccessUser;

@Component
public class RequestUtil {
	
	@Autowired(required = false)
	private HttpServletRequest request;
	
	private static RequestUtil requestUtil;
	 
    @PostConstruct
    public void init() {
    	requestUtil = this;
    }
	
	private static ThreadLocal<AccessUser> loginUser = new ThreadLocal<>();
	
	public static void setUserInfo(AccessUser curLoginUser) {
		loginUser.set(curLoginUser);
	}
	
	public static AccessUser getCurLoginUser() {
		return loginUser.get();
	}
	
	public static String[] geiBasicInfo() {
		if(requestUtil.request != null) {
			String auth = requestUtil.request.getHeader("Authorization");
			if ((auth != null) && (auth.length() > 6))
			{
				String HeadStr = auth.substring(0, 5).toLowerCase();
				if (HeadStr.compareTo("basic") == 0)
				{
					auth = auth.substring(6, auth.length());  
		            String decodedAuth = getFromBASE64(auth);
		            if (decodedAuth != null)
		            {
		            	String[] userArray = decodedAuth.split(":");
		            	return userArray;
		            }
				}
			}
		}
		
		return null;
	}
	
	public static String getHeader(String key) {
		if(requestUtil.request != null) {
			String value = requestUtil.request.getHeader(key);
			return value;
		}
		return null;
	}
	
	private static String getFromBASE64(String s) {
	    if (s == null)  
	        return null;
	    try {  
	        byte[] b = Base64.decode(s);  
	        return new String(b);  
	    } catch (Exception e) {  
	        return null;  
	    }
    }
	
}

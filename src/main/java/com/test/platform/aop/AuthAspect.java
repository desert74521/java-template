package com.test.platform.aop;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.platform.accesstoken.AccessUser;
import com.test.platform.utils.RequestUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 	鉴权切面
 */
@Slf4j
@Aspect
@Component
public class AuthAspect {

    @Autowired
    private HttpServletRequest request;
    
    @Pointcut(value = "@annotation(com.test.platform.aop.annotation.Auth)")
    private void cut() {

    }

    @Before("cut()")
    public void around(JoinPoint joinPoint) throws Throwable {
        String url = request.getRequestURI();
        String ip = getIpAdrress(request);
        String deviceType = getDeviceType(request);
        AccessUser accessUser = new AccessUser();
        log.info("【user:" + accessUser.getUserName() + "]】【设备："+deviceType+"】【ip:" + ip + "】【 访问api:" + url + "】");
        RequestUtil.setUserInfo(accessUser);
    }
    
	private String getIpAdrress(HttpServletRequest request) {
		String Xip = request.getHeader("X-Real-IP");
		String XFor = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = XFor.indexOf(",");
			if(index != -1){
				return XFor.substring(0,index);
			}else{
				return XFor;
			}
		}
		XFor = Xip;
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			return XFor;
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getRemoteAddr();
		}
		return XFor;
	}
	
	private String getDeviceType(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		String result = "PC";
		if(userAgent.indexOf("Android") != -1) {
			result = "Android";
		}
		if(userAgent.indexOf("iPhone") != -1) {
			result = "iPhone";
		}
		if(userAgent.indexOf("iPad") != -1) {
			result = "iPad";
		}
		return result;
	}
    
}

package com.kiwiframework.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;

/**
 * webUtil工具类
 *
 * @author xiongzhao
 */
public class WebUtil {
	
	private static Logger logger = LoggerFactory.getLogger(WebUtil.class);
	
	private static String appName;

	private static final String SESSION_MEMBER_KEY = "SSO_USER";

	/**
	 * 获取管理后台的用户登录信息的对象
	 * @return 登录用户对象
     */
	public static Object getSsoUser(){
		return getRequest().getSession().getAttribute(SESSION_MEMBER_KEY);
	}

	/**
	 * 保存用户登录信息
	 * @param member 当前登录用户对象
	 * @param <T> 当前登录用户泛型
     */
	public static <T> void saveSsoUser(T member){
		getRequest().getSession().setAttribute(SESSION_MEMBER_KEY,member);
	}

	/**
	 * 获取request
	 * @return {@link HttpServletRequest}
     */
	public static HttpServletRequest getRequest(){
		return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 获取response
	 * @return {@link HttpServletResponse}
	 */
	public static HttpServletResponse getResponse(){
		return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}


	/**
	 * 得到服务器路径
	 * @param request {@link ServletRequest}
	 * @return 服务器路径
     */
	public static String getServerPath(ServletRequest request){
        String basePath="";
        if (!"80".equals(request.getServerPort())){
			basePath=request.getServerName()+":"+request.getServerPort();
        }else {
			basePath=request.getServerName();
        }

		return basePath;
	}

	/**
	 * 在有nginx等反向代理的情况下获取真实的ip地址。
	 * @param request {@link HttpServletRequest}
	 * @return 当前机器ip
     */
	public static String getIpAddr(HttpServletRequest request) {

		String ip = request.getHeader("x-forwarded-for");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

			ip = request.getHeader("Proxy-Client-IP");

		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

			ip = request.getHeader("WL-Proxy-Client-IP");

		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 得到项目名称
	 * @return 当前项目名称
     */
	public static String getAppName() {
		return appName;
	}

	/**
	 * 设置当前项目名称
	 * @param appName 当前项目名称
     */
	public static void setAppName(String appName) {
		WebUtil.appName = appName;
	}

	/**
	 * 获取机器ip地址
	 * @return 当前机器ip地址
     */
	public static InetAddress getInetAddress() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
            logger.error(e.getMessage(),e);
		}
		return null;
	}
}

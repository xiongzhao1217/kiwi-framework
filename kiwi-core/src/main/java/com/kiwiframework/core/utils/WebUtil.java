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
	 * @return
     */
	public static Object getSsoUser(){
		return getRequest().getSession().getAttribute(SESSION_MEMBER_KEY);
	}

	/**
	 * 保存用户登录信息
	 * @return
	 */
	public static <T> void saveSsoUser(T member){
		getRequest().getSession().setAttribute(SESSION_MEMBER_KEY,member);
	}

	/**
	 * 获取request
	 * @return
	 */
	public static HttpServletRequest getRequest(){
		return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 获取response
	 * @return
	 */
	public static HttpServletResponse getResponse(){
		return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}


	/**
	 * 得到服务器路径
	 * @param request
	 * @return
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
	 * 
	 * @author lipengfei
	 * @date 2015-4-20
	 * @param request
	 * @return
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
	 * 
	 * @author lipengfei
	 * @date 2015-4-23
	 * @return
	 */
	public static String getAppName() {
		return appName;
	}

	public static void setAppName(String appName) {
		WebUtil.appName = appName;
	}

	public static InetAddress getInetAddress() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
            logger.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 获取当前系统服务器所在节点的ip地址。
	 * 
	 * @author lipengfei
	 * @date May 3, 2015
	 * @param boardcomName
	 *            网卡名称
	 * @return
	 */
	public static String getAppIP(String boardcomName) {
		String ip = "";
		Enumeration<NetworkInterface> e;
		try {
			e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) e.nextElement();
				if (!ni.getName().equals(boardcomName)) {
					continue;
				} else {
					Enumeration<?> e2 = ni.getInetAddresses();
					while (e2.hasMoreElements()) {
						InetAddress ia = (InetAddress) e2.nextElement();
						if (ia instanceof Inet6Address)
							continue;
						ip = ia.getHostAddress();
					}
					break;
				}
			}
		} catch (SocketException e1) {
            logger.error(e1.getMessage(),e1);
		}
		return ip;
	}

	/**
	 * 获取当前系统服务器所在节点的ip地址。
	 * 
	 * @author lipengfei
	 * @date May 3, 2015
	 *            网卡名称
	 * @return
	 */
	public static String printAllboardcomName() {
		String ip = "";
		Enumeration<NetworkInterface> e;
		try {
			e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				System.out.println(e.nextElement());
			}
		} catch (SocketException e1) {
            logger.error(e1.getMessage(),e1);
		}
		return ip;
	}
	
	/**
	 * 从request中获取所有参数
	 * @author lipengfei
	 * @date May 9, 2015
	 * @param request
	 * @return
	 */
	public static Map<String, String> getParamsFromRequest(HttpServletRequest request){
		//获取支付宝GET过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			try {
				
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			
			} catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(),e);
			}
			params.put(name, valueStr);
		}
		return params;
	}
	
	/**
	 *
     * 从List中只取出一个数据,如果多于一个数据，则会抛出{@code TraininghubException}异常。
	 * @author lipengfei
	 * @date May 9, 2015
     *
	 */
	public static <T> T getOneFromList(List<T> list){
		T value =null;
		if(list!=null && list.size()>0){
			
			if(list.size()==1){
				value = list.get(0);
			}else {
				throw new RuntimeException("Can only get one data，but more...");
			}
		}
		return value;
	}
}

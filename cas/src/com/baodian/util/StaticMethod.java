package com.baodian.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;

public class StaticMethod {
	
	 //tomcat的get方法的编码格式，如果不是utf8，那么需要转码 @update
    private static String tomcatEncode;
    //账号和密码之间的分割符
    private static String passSprt;
	
	/**
	 * 外网代理存在时，也能正确获取ip
	 * @return ip
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		//System.out.println(ip);
		return ip;
	}
	/**
	 * 检查字符是否不为空，且长度在min到max之间
	 */
	public static boolean checkStr(String param, int min, int max) {
		if(param == null) return false;
		if(param.length() < min) return false;
		if(param.length() > max) return false;
		return true;
	}
	/**
	 * 返回tomcat编码，防止乱码，自动判断get、post
	 */
	public static String tomcatDecode(HttpServletRequest request, String str) {
		if(request.getMethod().equals("POST")) {
			return str;
		}
		return tomcatDecode(str);
	}
	/**
	 * 返回tomcat编码，防止乱码
	 */
	public static String tomcatDecode(String str) {
		if(str == null) {
			return null;
		}
		try {
			return new String(str.getBytes(tomcatEncode), "UTF8");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}
	/**
	 * 字符串转16进制，可以用于解决中文编码问题
	 */
	public static String Str2Hex(String str) {
		try {
			return new BigInteger(1, str.getBytes("UTF-8")).toString(16);
		} catch (Exception e) {
			return str;
		}
	}
	/**
	 * 16进制转字符串
	 */
	public static String hex2Str(String str) {
		try {
			byte[] bt = new BigInteger(str, 16).toByteArray();
			if(bt[0] == 0) {
				return new String(bt, 1, bt.length-1, "UTF-8");
			} else {
				return new String(bt, "UTF-8");
			}
		} catch (Exception e) {
			return str;
		}
	}
	/**
	 * 生成密码
	 */
	public static String makePass(String account, String password) {
		return DigestUtils.shaHex(account + passSprt + password);
	}
//set get
	public String getTomcatEncode() {
		return tomcatEncode;
	}
	public void setTomcatEncode(String tomcatEncode) {
		StaticMethod.tomcatEncode = tomcatEncode;
	}
	public static String getPassSprt() {
		return passSprt;
	}
	public static void setPassSprt(String passSprt) {
		StaticMethod.passSprt = passSprt;
	}
}

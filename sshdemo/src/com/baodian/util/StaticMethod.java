package com.baodian.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.baodian.service.util.InitData;
import com.baodian.util.rsa.RSACoder;

public class StaticMethod {
	public static final String loginError = "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
	public static final String inputError = "{\"status\":1,\"mess\":\"输入有误！\"}";
	public static final String addSucc = "{\"status\":0,\"mess\":\"添加成功！\"}";
	public static final String changeSucc = "{\"status\":0,\"mess\":\"更新成功！\"}";
	public static final String removeSucc = "{\"status\":0,\"mess\":\"删除成功！\"}";
	public static final String authError = "{\"status\":1,\"mess\":\"没有权限！\"}";
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
	private static Format ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 将时间转换为2013-02-24 22:31:13格式
	 */
	public static String DateToString(Date date) {
		return ft.format(date);
	}
	/**
	 * 将时间转换为2013-02-24 22:31:13格式
	 */
	public static String LongToDate(long l) {
		return ft.format(l);
	}
	/**
	 * json字符串返回
	 */
	public static String jsonMess(int state, String mess) {
		return "{\"status\":" + state + ",\"mess\":\"" + mess + "\"}";
	}
	/**
	 * 失败信息
	 */
	public static String errorMess(String mess) {
		return jsonMess(1, mess);
	}
	/**
	 * 成功信息
	 */
	public static String succMess(String mess) {
		return jsonMess(0, mess);
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
	 * 字符串转换成数字
	 * * @return error: -1
	 */
	public static int Str2Int(String i) {
		try {
			return Integer.parseInt(i);
		} catch(Exception e) {
			return -1;
		}
	}
	/**
	 * sha1加密
	 */
	public static String encodeSha1(String code) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(code.getBytes("UTF-8"));
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 返回字符串长度
	 * @return 空:-1
	 */
	public static int StrSize(String str) {
		if(str == null) {
			return -1;
		}
		return str.length();
	}
	/**
	 * 字符串转16进制，传输时解决中文问题
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
	 * 返回tomcat编码，防止乱码
	 */
	public static String tomcatDecode(String str) {
		if(str == null) {
			return null;
		}
		try {
			return new String(str.getBytes(InitData.tomcatEncode), "UTF8");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}
	/**
	 * 类似javascript array的join，将数组转换成字符串，中间用字符隔开
	 * @param array 数组
	 * @param separator 分隔符
	 * @return
	 */
	public static String arrayToStr(Set<Integer> array, String separator) {
		if(array.size() == 0) {
			return "";
		}
		StringBuilder str = new StringBuilder();
		for(int i : array) {
			str.append(i + separator);
		}
		return str.substring(0, str.length()-1);
	}
	/**
	 * 使用公钥加密
	 */
	public static String rsaPUBEncode(String code) throws Exception {
		return RSACoder.encryptByPublicKey(code, 
				InitData.casData.get("clientPubKey"), InitData.casData.get("clientModKey"));
	}
}

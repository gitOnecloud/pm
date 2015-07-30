package com.baodian.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.baodian.service.util.InitData;

/**
 * 保存从配置文件读取的信息
 * @author LF_eng
 */
@Component("initData")
public class InitData {
	/**
	 * 所有允许上传的扩展名
	 */
	public Set<String> extSet = new HashSet<String>();
	public static Set<String> imageSet = new HashSet<String>();
	/**
	 * 0-image 1-flash 2-media 3-file 4-uploadDir
	 * 5-dirSize 6-fileSize
	 */
	public static String[] upload = new String[7];
	/**
	 * 0-dirSize 1-fileSize
	 */
	public static Long[] longPms = new Long[2];
	//四班三倒
	//上班顺序
	public static String[] dutyName = new String[4];
	//白班顺序
	public static int[] dutyOrder = new int[4];
	/**
	 * 0=>cas.server.encode 1=>cas.client
	 * 2=>cas.client.pubKey 3=>cas.client.modKey
	 * 4=>cas.server.userUrl 5=>cas.server.loginUrl
	 * 6=>cas.client.checkUrl 7=>cas.noUserFailureUrl
	 * 8=>cas.server.userStatusUrl 9=>cas.client.loginUrl
	 * 10=>cas.client.indexUrl 11=>cas.client.logoutUrl
	 */
	//public static String[] casData = new String[11];
	public static Map<String, String> casData = new HashMap<String, String>();
	//tomcat编码
	public static String tomcatEncode;
	/**
	 * 是否使用cas，在org.jasig.cas.client.validation.Cas20ServiceTicketValidator设置
	 * 如果加载security-cas.xml，这个值将自动变为true
	 */
	public static boolean useCas = false;
	/**
	 * 登录相关的数据
	 * login.lockMinute
	 * login.oneMinuteNum
	 * login.oneDayNum
	 */
	public static Map<String, Integer> loginData = new HashMap<String, Integer>();
	//日志类型
	public static String[] logType;
	
//初始化
	@PostConstruct
	public void init() {
		//System.out.println("file.encoding:" + new Properties(System.getProperties()).getProperty("file.encoding"));
		InputStream in = this.getClass().getResourceAsStream("/config.properties");
		Properties properties = new Properties();
		try {
			properties.load(in);
			upload[0] = properties.getProperty("image");
			upload[1] = properties.getProperty("flash");
			upload[2] = properties.getProperty("media");
			upload[3] = properties.getProperty("file");
			for(String s : upload[0].split(",")) {
				extSet.add(s);
				imageSet.add(s);
			}
			for(String s : upload[1].split(",")) {
				extSet.add(s);
			}
			for(String s : upload[2].split(",")) {
				extSet.add(s);
			}
			for(String s : upload[3].split(",")) {
				extSet.add(s);
			}
			upload[4] = properties.getProperty("uploadDir");
			longPms[0] = Long.parseLong(properties.getProperty("dirSize"));
			longPms[1] = Long.parseLong(properties.getProperty("fileSize"));
			upload[5] = fileSize(longPms[0]);
			upload[6] = fileSize(longPms[1]);
			//四班三倒
			String[] str = properties.getProperty("dutyName").split(",");
			for(int i=0; i<str.length; i++) {
				dutyName[i] = str[i];
			}
			str = properties.getProperty("dutyOrder").split(",");
			for(int i=0; i<str.length; i++) {
				dutyOrder[i] = Integer.parseInt(str[i]);
			}
			tomcatEncode = properties.getProperty("tomcat.encode");
			//登录数据
			loginData.put("lockMinute", Integer.parseInt(properties.getProperty("login.lockMinute")));
			loginData.put("oneMinuteNum", Integer.parseInt(properties.getProperty("login.oneMinuteNum")));
			loginData.put("oneDayNum", Integer.parseInt(properties.getProperty("login.oneDayNum")));
			//日志类型
			logType = properties.getProperty("log.type").split(",");
			
			System.out.println("***初始化InitData(config)成功***");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//读取spring.properties
		in = this.getClass().getResourceAsStream("/cas.properties");
		try {
			properties.load(in);
			casData.put("serverEncode", properties.getProperty("cas.server.encode"));
			casData.put("client", properties.getProperty("cas.client"));
			casData.put("clientPubKey", properties.getProperty("cas.client.pubKey"));
			casData.put("clientModKey", properties.getProperty("cas.client.modKey"));
			String casServer = properties.getProperty("cas.server");
			casData.put("serverUserUrl", properties.getProperty("cas.server.userUrl")
					.replace("${cas.server}", casServer));
			casData.put("serverLoginUrl", properties.getProperty("cas.server.loginUrl")
					.replace("${cas.server}", casServer));
			String client = properties.getProperty("cas.client");
			casData.put("clientCheckUrl", properties.getProperty("cas.client.checkUrl")
					.replace("${cas.client}", client));
			casData.put("noUserFailureUrl", properties.getProperty("cas.noUserFailureUrl"));
			casData.put("serverUserStatusUrl", properties.getProperty("cas.server.userStatusUrl") 
					.replace("${cas.server}", casServer));
			casData.put("clientLoginUrl", properties.getProperty("cas.client.loginUrl"));
			casData.put("clientIndexUrl", properties.getProperty("cas.client.indexUrl"));
			casData.put("clientLogoutUrl", properties.getProperty("cas.client.logoutUrl"));
			
			System.out.println("***初始化InitData(cas)成功***");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 将格式化输出文件大小
	 */
	private String fileSize(long fsize) {
		if(fsize < 1024) {
			return fsize + "B";
		}
		if(fsize >= 1073741824) {
			return fsize/1073741824 + "GB";
		}
		if(fsize >= 1048576) {
			return fsize/1048576 + "MB";
		}
		return fsize/1024 + "KB";
	}
//r
	/**
	 * 重新读取数据
	 */
	public void reload() {
		extSet.clear();
		imageSet.clear();
		this.init();
	}
//o
	/**
	 * 输出数据
	 */
	public void output() {
		System.out.println("---扩展名0-image 1-flash 2-media 3-file 4-uploadDir");
		for(String s: upload) {
			System.out.println(s);
		}
		printSet("---图片扩展imageSet", imageSet);
		printSet("---全部扩展extSet", extSet);
		printString("---值班名称dutyName", dutyName);
		printInt("---值班顺序dutyOrder", dutyOrder);
	}
	private void printSet(String name, Set<String> o) {
		System.out.print(name + "(" + o.size() + ") => ");
		for(String s : o) {
			System.out.print(s + " ");
		}
		System.out.println();
	}
	private void printString(String name, String[] o) {
		System.out.print(name + "(" + o.length + ") => ");
		for(String s : o) {
			System.out.print(s + " ");
		}
		System.out.println();
	}
	private void printInt(String name, int[] o) {
		System.out.print(name + "(" + o.length + ") => ");
		for(int s : o) {
			System.out.print(s + " ");
		}
		System.out.println();
	}
}

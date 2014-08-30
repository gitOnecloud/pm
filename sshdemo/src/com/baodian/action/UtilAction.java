package com.baodian.action;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.service.role.SecuManager;
import com.baodian.service.user.UserManager;
import com.baodian.service.util.InitData;
import com.baodian.service.util.StaticData;
import com.baodian.util.rsa.RSACoder;

@SuppressWarnings("serial")
@Component("util")
@Scope("prototype")//必须注解为多态
public class UtilAction extends CommonAction implements SessionAware {
//依赖注入
	@Resource(name="secuManager")
	private SecuManager secuManager;
	@Resource(name="staticData")
	private StaticData sdata;
	@Resource(name="initData")
	private InitData idata;
	@Resource(name="userManager")
	private UserManager userManager;
//页面属性
	private Map<String, Object> session;
//访问方法
	public String index() {
		JSONObject result = new JSONObject();
		result.put("fileSizeLimit", InitData.upload[6]);
		json = result.toString();
		return "index";
	}
	public String admin() {
		json = secuManager.findMenu().toString();
		return "admin";
	}
	public String lguser() {
		json = userManager.findlgUser();
		return SUCCESS;
	}
	/**
	 * 获取rsa密钥，私钥保存在session中
	 */
	public String rsaKey() {
		 //保存rsa密钥到flow, rsa_publicKey保存的是模Modulus @update
        KeyPair keyPair = RSACoder.initKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        session.put("rsa_priKey", privateKey);
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        JSONObject js = new JSONObject();
        js.put("status", 0);
        js.put("rsa_pubKey", publicKey.getPublicExponent().toString(16));
        js.put("rsa_modKey", publicKey.getModulus().toString(16));
        json = js.toString();
		return JSON;
	}
	/**
	 * 自助注册
	 */
	public String selfReg_no_rd() {
		json = sdata.findDept();
		return SUCCESS;
	}
	/**
	 * 登录
	 */
	public String login_no_rd() {
		return SUCCESS;
	}
//刷新内存
	//
	public String rfall_rf() {
		this.secuManager.refreshAll_RA_();
		this.secuManager.refreshMenu();
		this.sdata.reload();
		this.idata.reload();
		json = "{\"status\":0}";
		return JSON;
	}
	//权限角色
	public String refresh_rf() {
		this.secuManager.refreshAll_RA_();
		json = "{\"status\":0}";
		return JSON;
	}
	//权限菜单
	public String rfmenu_rf() {
		this.secuManager.refreshMenu();
		json = "{\"status\":0}";
		return JSON;
	}
	//值班部门
	public String rfduty_rf() {
		this.sdata.reload();
		//sdata.output();
		json = "{\"status\":0}";
		return JSON;
	}
	//配置文件
	public String rfdata_rf() {
		this.idata.reload();
		//idata.output();
		json = "{\"status\":0}";
		return JSON;
	}
	
//set get
	public org.springframework.security.core.userdetails.User getUser() {
		return SecuManager.currentUser();
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public Map<String, String> getCasData() {
		return InitData.casData;
	}
}

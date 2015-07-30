package com.baodian.action;

import java.security.interfaces.RSAPrivateKey;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.user.User;
import com.baodian.service.user.UserManager;
import com.baodian.util.StaticMethod;
import com.baodian.util.page.UserPage;

@SuppressWarnings("serial")
@Component("user")
@Scope("prototype")//必须注解为多态
public class UserAction extends CommonAction implements SessionAware {
//依赖注入
	@Resource(name="userManager")
	private UserManager userManager;
//页面属性
	private User user;
	private UserPage page;
	private int depmId;
	private String date;
	private Map<String, Object> session;
//访问方法
//c
	public String save_js() {
		if(user == null) {
			json = StaticMethod.errorMess("输入有误");
		} else if(! StaticMethod.checkStr(user.getAccount(), 2, 20)) {
			json = StaticMethod.errorMess("账号有误！");
		} else if(! StaticMethod.checkStr(user.getName(), 2, 20)) {
			json = StaticMethod.errorMess("名字有误！");
		} else if(user.getDpm() == null) {
			json = StaticMethod.errorMess("部门为空！");
		} else {
			json = userManager.save(user, json, (RSAPrivateKey) session.get("rsa_priKey"));
			session.remove("rsa_priKey");
		}
		return JSON;
	}
//r
	public String list() {
		json = userManager.findDRs();
		return SUCCESS;
	}
	public String list_js() {
		if(page == null)
			page = new UserPage();
		json = userManager.findU_inadOnPage(page);
		return JSON;
	}
	/**
	 * 用户及所在部门
	 */
	public String dpm_js() {
		json = userManager.findU_Ds(json);
		return JSON;
	}
	/**
	 * 部门中的子部门和用户
	 */
	public String indpm_js() {
		json = userManager.findU_Ds(depmId);
		return JSON;
	}
	public String login() {
		return SUCCESS;
	}
	/**
	 * 登录用户
	 */
	public String login_js() {
		if(page == null) {
			page = new UserPage();
		}
		json = userManager.findlgUser(page);
		return JSON;
	}
//u
	public String change_js() {
		if(user.getId() == 0) {
			json = "{\"status\":1,\"mess\":\"未选择用户！\"}";
		} else {
			json = userManager.change(user, json, (RSAPrivateKey) session.get("rsa_priKey"));
			session.remove("rsa_priKey");
		}
		return JSON;
	}
	public String changePW_js() {
		json = userManager.changePW(user, (RSAPrivateKey) session.get("rsa_priKey"));
		session.remove("rsa_priKey");
		return JSON;
	}
	public String changeDpm_js() {
		json = userManager.changeDpm(json);
		return JSON;
	}
	public String changeRole_js() {
		json = userManager.changeRole(json);
		return JSON;
	}
	public String changeStatus_js() {
		json = userManager.changeStatus(json);
		return JSON;
	}
//d
	/*public String remove_js() {
		if(user.getId() == 0) {
			json = "{\"status\":1,\"mess\":\"未选择用户！\"}";
		} else {
			json = userManager.remove(user);
		}
		return "json";
	}*/
	public String logout_js() {
		json = userManager.logout(json);
		return JSON;
	}
//o
	public String makePassword() {
		if(user != null) {
			json = this.userManager.makePassword(user.getAccount(), user.getPassword());
		}
		return "jhtml";
	}
	
	public String getAllUserName(){
		json = userManager.getAllUserName();
		//System.out.println("json="+json);
		return JSON;
	}
	
//set get
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public UserPage getPage() {
		return page;
	}
	public void setPage(UserPage page) {
		this.page = page;
	}
	public int getDepmId() {
		return depmId;
	}
	public void setDepmId(int depmId) {
		this.depmId = depmId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}

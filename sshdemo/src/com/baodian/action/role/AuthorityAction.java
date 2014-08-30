package com.baodian.action.role;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.role.Authority;
import com.baodian.service.role.AuthorityManager;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("authority")
@Scope("prototype")//必须注解为多态
public class AuthorityAction extends ActionSupport {
//依赖注入	
	@Resource(name="authorityManager")
	private AuthorityManager authorityManager;
//页面属性
	private String json;
	private String[] auths;
	private Authority authority;
	private List<Authority> authorities;
//访问方法
//c
	public String add_js() {
		int length = authority.getName().length();
		if(length>0 && length<20) {
			authorityManager.save(authority);
			json = "{\"status\":0,\"id\":\"" + authority.getId() + "\"}";
		} else {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		return "json";
	}
//r
	public String list() {
		json = authorityManager.findAsOnsort();
		return "success";
	}
	public String role() {
		json = authorityManager.findRoAus();
		return "success";
	}
	/*public String menu_js() {
		json = authorityManager.findAsOnsort();
		return "json";
	}
	public String list_js() {
		json = authorityManager.findAsOnsort(null);
		return "json";
	}*/
//u
	public String changeMenu_js() {
		if(auths.length > 0) {
			authorityManager.changeAuths(auths);
		}
		json = "{\"status\":0, \"mess\":\"更新成功！\"}";
		return "json";
	}
	public String change_js() {
		if(authority.getName() == null)
			authority.setName("");
		int length = authority.getName().length();
		if(length < 20) {
			if(authority.getUrl() == null)
				authority.setUrl("");
			length = authority.getUrl().length();
			if(length < 50) {
				authorityManager.changeA_nu(authority);
				json = "{\"status\":0}";
			} else {
				json = "{\"status\":1,\"mess\":\"权限url长度超出50个！\"}";
			}
		} else {
			json = "{\"status\":1,\"mess\":\"权限名字长度超出20个！\"}";
		}
		return "json";
	}
	public String changeRole_js() {
		json = authorityManager.changeRole(json);
		return "json";
	}
//d
	public String remove_js() {
		json = authorityManager.remove(authority);
		return "json";
	}
//set get
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String[] getAuths() {
		return auths;
	}
	public void setAuths(String[] auths) {
		this.auths = auths;
	}
	public List<Authority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
	public Authority getAuthority() {
		return authority;
	}
	public void setAuthority(Authority authority) {
		this.authority = authority;
	}
	
}

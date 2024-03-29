﻿package com.baodian.action.role;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.action.UtilAction;
import com.baodian.model.role.Role;
import com.baodian.service.role.AuthorityManager;
import com.baodian.service.role.RoleManager;
import com.baodian.util.JSONValue;
import com.baodian.util.StaticMethod;

@SuppressWarnings("serial")
@Component("role")
@Scope("prototype")//必须注解为多态
public class RoleAction extends UtilAction {
//依赖注入	
	@Resource(name="roleManager")
	private RoleManager roleManager;
	@Resource(name="authorityManager")
	private AuthorityManager authorityManager;
//页面属性
	private Role role;
	private String[] roles;
	private int[] ids;
//访问方法
//c
	public String add_js() {
		if(role==null || ! StaticMethod.checkStr(role.getName(), 1, 20)) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			roleManager.save(role, ids);
			json = "{\"status\": 0,\"id\": " + role.getId() + ",\"mess\":\"添加成功！\"}";
		}
		return JSON;
	}
	public String addInput() {
		json = JSONValue.escape(StaticMethod.tomcatDecode(json));
		json = "{\"auths\":" + authorityManager.findAsOnsort(null) +
				",\"parent\":\"" + json + "\"}";
		return SUCCESS;
	}
//r
	public String list() {
		json = roleManager.findRoles().toString();
		return SUCCESS;
	}
	public String show() {
		if(role==null || role.getId()==0)
			return "list";
		role = roleManager.findRA_ByR_id(role.getId());
		if(role == null)
			return "list";
		json = authorityManager.findAsOnsort(role);
		return SUCCESS;
	}
//u
	/**
	 * 更改角色权限
	 * @return
	 */
	public String changeRA_js() {
		if(role.getId()==0)
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		else {
			roleManager.changeRA(role.getId(), ids);
			json = "{\"status\":0}";
		}
		return JSON;
	}
	/**
	 * 更改角色名称
	 * @return
	 */
	public String changeN_js() {
		if(role==null || ! StaticMethod.checkStr(role.getName(), 1, 20)) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			roleManager.changeName(role);
			json = "{\"status\":0}";
		}
		return JSON;
	}
	public String changeSort_js() {
		if(roles==null || roles.length==0) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			roleManager.changeSort(roles);
			json = "{\"status\":0,\"mess\":\"更新成功！\"}";
		}
		return JSON;
	}
//d
	public String remove_js() {
		if(role.getId() == 0) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			roleManager.remove(role);
			json = "{\"status\":0}";
		}
		return JSON;
	}
//set get
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String[] getRoles() {
		return roles;
	}
	public void setRoles(String[] roles) {
		this.roles = roles;
	}
	public int[] getIds() {
		return ids;
	}
	public void setIds(int[] ids) {
		this.ids = ids;
	}
}

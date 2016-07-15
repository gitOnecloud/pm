package com.baodian.action.user;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.action.UtilAction;
import com.baodian.model.user.Department;
import com.baodian.service.user.DepartmentManager;

@SuppressWarnings("serial")
@Component("department")
@Scope("prototype")//必须注解为多态
public class DepartmentAction extends UtilAction {
//依赖注入
	@Resource(name="departmentManager")
	private DepartmentManager dm;
//页面属性
	private Department department;
	private String[] dpms;
//访问方法
//c
	public String add_js() {
		if(department!=null && department.ckName()>0) {
			dm.save(department);
			json = "{\"status\":0,\"id\":\"" + department.getId() + "\"}";
		} else {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		return JSON;
	}
//r
	public String list() {
		json = dm.findAll();
		return SUCCESS;
	}
	public String list_js() {
		json = dm.findAll();
		return JSON;
	}
//u
	public String change_js() {
		if(department!=null && department.ckName()>0) {
			dm.changeName(department);
			json = "{\"status\":0}";
		} else {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		return JSON;
	}
	public String changeSort_js() {
		if(dpms==null || dpms.length==0) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			dm.changeSort(dpms);
			json = "{\"status\":0,\"mess\":\"更新成功！\"}";
		}
		return JSON;
	}
//d
	public String remove_js() {
		json = dm.remove(department);
		return JSON;
	}
//set get
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public String[] getDpms() {
		return dpms;
	}
	public void setDpms(String[] dpms) {
		this.dpms = dpms;
	}
}

package com.baodian.service.user;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.user.DepartmentDao;
import com.baodian.dao.user.UserDao;
import com.baodian.model.user.Department;
import com.baodian.service.util.StaticData;

@Service("departmentManager")
public class DepartmentManager {

	private DepartmentDao ddao;
	@Resource(name="departmentDao")
	public void setDdao(DepartmentDao ddao) {
		this.ddao = ddao;
	}
	private UserDao userDao;
	@Resource(name="userDao")
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	private StaticData sdata;
	@Resource(name="staticData")
	public void setSdata(StaticData sdata) {
		this.sdata = sdata;
	}
//c
	/**
	 * 保存部门
	 * @param department (name, parent.id)
	 */
	public void save(Department d) {
		ddao.save(d);
		sdata.reload();
	}
//r
	/**
	 * 读取所有的部门信息
	 * @return Json [{"id":"", "name":"", "pId":""}]
	 */
	public String findAll() {
		return sdata.findDept();
	}
//u
	/**
	 * 更改部门名称
	 * @param department
	 */
	public void changeName(Department d) {
		ddao.updateName(d);
		StaticData.depts.get(d.getId()).setName(d.getName());
	}
	public void changeSort(String[] dpms) {
		for(String goods : dpms) {
			String[] ids = goods.split("_");
			ddao.updateSort(Integer.parseInt(ids[0]),
					Integer.parseInt(ids[1]), Integer.parseInt(ids[2]));
		}
		sdata.reload();
	}
//d
	public String remove(Department department) {
		int did = department.getId();
		int num = ddao.getChildrenNum(did);
		if(num > 0) {
			return "{\"status\":1,\"mess\": \"存在" + num + "个子部门！\"}";
		}
		num = userDao.getU_NumbyD_id(did);
		if(num > 0) {
			return "{\"status\":1,\"mess\": \"此部门存在" + num + "个用户！\"}";
		}
		ddao.delete(did, "Department");
		sdata.reload();
		return "{\"status\":0}";
	}
}

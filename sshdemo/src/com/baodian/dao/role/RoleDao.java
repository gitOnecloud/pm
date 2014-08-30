package com.baodian.dao.role;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.baodian.dao.role.RoleDao;
import com.baodian.dao.util.UtilDao;
import com.baodian.model.role.Role;

@Repository("roleDao")
public class RoleDao extends UtilDao {

//c
//r
	/**
	 * 读取全部角色信息
	 */
	public List<Role> getRoles() {
		return super.find("from Role r order by r.sort");
	}
	public Role getRoleById(int id) {
		return (Role) sf.getCurrentSession().get(Role.class, id);
	}
	/**
	 * 用户注册默认角色
	 */
	public List<Role> getDefaultRoles() {
		return super.find("select new Role(r.id)" +
				"from Role r where r.defaultSet=1");
	}
//u
	public void update(Role role) {
		sf.getCurrentSession().update(role);
	}
	public void updateName(Role role) {
		super.bulkUpdate("update Role r set r.name=? where r.id=?", role.getName(), role.getId());
	}
	public void updateSort(int id, int pid, int sort, int defaultSet) {
		StringBuilder sql = new StringBuilder();
		if(pid != -1) {
			if(pid == 0)
				sql.append(",r.parent.id=null");
			else
				sql.append(",r.parent.id=" + pid);
		}
		if(sort != -1)
			sql.append(",r.sort=" + sort);
		if(defaultSet != -1)
			sql.append(",r.defaultSet=" + defaultSet);
		if(sql.length() != 0) {
			super.bulkUpdate("update Role r set " + sql.substring(1, sql.length()) +
					" where r.id=" + id);
		}
	}
//d
	public void delete(Role role) {
		sf.getCurrentSession().delete(role);
	}
}

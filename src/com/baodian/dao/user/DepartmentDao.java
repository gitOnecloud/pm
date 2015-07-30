package com.baodian.dao.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.baodian.dao.user.DepartmentDao;
import com.baodian.dao.util.UtilDao;
import com.baodian.model.user.Department;

@Repository("departmentDao")
public class DepartmentDao extends UtilDao {

//c

//r
	/**
	 * 读取所有的部门信息
	 * @return List<Department>
	 */
	public List<Department> getAll() {
		return super.find("from Department d order by d.sort");
	}
	/**
	 * 查找子部门数
	 * @param id 要查找的部门id
	 * @return 数量
	 */
	public int getChildrenNum(int id) {
		return super.countNum("select count(*) " +
				"from Department d where d.parent.id=" + id);
	}
//u
	/**
	 * 更改部门名称
	 * @param department (id, name)
	 */
	public void updateName(Department d) {
		super.bulkUpdate("update Department d set d.name=? where d.id=?", d.getName(), d.getId());
	}
	/**
	 * 更新顺序
	 */
	public void updateSort(int id, int pid, int sort) {
		StringBuilder sql = new StringBuilder();
		if(pid != -1) {
			if(pid == 0)
				sql.append(",d.parent.id=null");
			else
				sql.append(",d.parent.id=" + pid);
		}
		if(sort != -1)
			sql.append(",d.sort=" + sort);
		if(sql.length() != 0) {
			super.bulkUpdate("update Department d set " + sql.substring(1, sql.length()) +
					" where d.id=" + id);
		}
	}
//d

}

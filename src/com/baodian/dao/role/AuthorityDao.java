package com.baodian.dao.role;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.baodian.dao.role.AuthorityDao;
import com.baodian.dao.util.UtilDao;
import com.baodian.model.role.Authority;

@Repository("authorityDao")
public class AuthorityDao extends UtilDao {

//c	

//r
	public List<Authority> getAuthorities() {
		return super.find("from Authority");
	}
	/**
	 * 获取所有的角色与权限关系
	 * @return
	 */
	public List<Authority> getAll_AR_() {
		// where a.url!='#'
		return super.find("from Authority a join fetch a.role_auths");
	}
	/**
	 * 排序读取权限(菜单)
	 */
	public List<Authority> getAsOnsort() {
		return super.find("from Authority a order by a.sort");
	}
	/**
	 * 排序读取显示的权限(菜单)
	 * @return
	 */
	public List<Authority> getAsOnsd() {
		return super.find("from Authority a where a.display=1 order by a.sort");
	}
	public Authority getAuthorityById(int id) {
		return (Authority) sf.getCurrentSession().get(Authority.class, id);
	}
	/**
	 * 获取子权限个数
	 * @param id
	 * @return
	 */
	public int getChildrenNum(int id) {
		return super.countNum("select count(*) " +
				"from Authority a where a.parent.id=" + id);
	}
	/**
	 * 获取无角色的权限id
	 * @return
	 */
	public List<Integer> getA_OnNoRole() {
		return super.find("select a.id from Authority a where a.display=1 and " +
				"a.id not in (select distinct ra.authority.id from Role_Auth ra)");
	}
	
//u
	public void update(Authority authority) {
		sf.getCurrentSession().update(authority);
	}
	/**
	 * 更新权限的父节点 顺序 显示
	 * @param id
	 * @param pid
	 * @param display 1显示 0不显示 -1不更新
	 * @param sort -1不更新 其它为顺序
	 */
	public void updateSort(int id, int pid, int display, int sort) {
		String sql = "";
		if(pid != -1) {
			if(pid == 0)
				sql = sql.concat(",a.parent.id=null");
			else
				sql = sql.concat(",a.parent.id=" + pid);
		}
		if(display != -1)
			sql = sql.concat(",a.display=" + display);
		if(sort != -1)
			sql = sql.concat(",a.sort=" + sort);
		if(!sql.isEmpty())
			super.bulkUpdate("update Authority a set " + sql.substring(1, sql.length()) +
					" where a.id=" + id);
	}
	/**
	 * 更改权限的名字和地址
	 * @param authority
	 */
	public void updateA_nu(Authority a) {
		if(a.getName().isEmpty()) {
			if(!a.getUrl().isEmpty()) {
				super.bulkUpdate("update Authority a set a.url=? where a.id=?", a.getUrl(), a.getId());
			}
		} else {
			if(a.getUrl().isEmpty()) {
				super.bulkUpdate("update Authority a set a.name=? where a.id=?", a.getName(), a.getId());
			} else {
				super.bulkUpdate("update Authority a set a.name=?,a.url=? where a.id=?", a.getName(), a.getUrl(), a.getId());
			}
		}
	}
//d

}

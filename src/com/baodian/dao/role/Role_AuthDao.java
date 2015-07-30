package com.baodian.dao.role;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.baodian.dao.role.Role_AuthDao;
import com.baodian.dao.util.UtilDao;
import com.baodian.model.role.Role_Auth;

@Repository("roleAuthDao")
public class Role_AuthDao extends UtilDao {

//c

//r
	public List<Role_Auth> getRole_Auths() {
		return super.find("from Role_Auth");
	}
	public List<Role_Auth> getAIdsById(int roleId) {
		return super.find("from Role_Auth ra where ra.role.id=?", roleId);
	}
	/**
	 * 通过角色id获取权限id
	 */
	public List<Integer> getAIdsByRIds(String rids) {
		return super.find("select a.id from Authority a where a.display=1 and " +
				"a.id in(select distinct ra.authority.id from Role_Auth ra " +
				"where ra.role.id in(" + rids + "))");
	}
	/**
	 * 获取角色与权限的关系
	 * @return [{rid,aid}]
	 */
	public String getRoAus() {
		List<Role_Auth> roAus = super.find("from Role_Auth");
		if(roAus.size() == 0) {
			return "[]";
		}
		StringBuilder json = new StringBuilder();
		json.append('[');
		for(Role_Auth ra : roAus) {
			json.append("{\"rid\":" + ra.getRole().getId() + 
					",\"aid\":" + ra.getAuthority().getId() + "},");
		}
		return json.substring(0, json.length()-1) + ']';
	}
//d
	/**
	 * 通过角色id删除角色与权限的关系
	 */
	public void deleteR_A_ByRid(int id) {
		super.bulkUpdate("delete from Role_Auth r where r.role.id=?", id);
	}
	/**
	 * 通过权限id删除权限与角色的关系
	 */
	public void deleteR_A_ByAid(int id) {
		super.bulkUpdate("delete from Role_Auth r where r.authority.id=?", id);
	}
}

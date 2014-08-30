package com.baodian.dao.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.baodian.dao.user.User_RoleDao;
import com.baodian.dao.util.UtilDao;
import com.baodian.model.user.User_Role;

@Repository("userRoleDao")
public class User_RoleDao extends UtilDao {
//c

//r
	/**
	 * 用户角色
	 * @return user(id) role(id)
	 */
	public List<User_Role> getU_R_ByUId(int id) {
		return super.find("from User_Role ur where ur.user.id=?", id);
	}
	/**
	 * 用户角色
	 * @return user(id) role(id, name)
	 */
	public List<User_Role> get_U_RByUId(int id) {
		return super.find("from User_Role ur join fetch ur.role where ur.user.id=?", id);
	}
//d
	/**
	 * 通过用户id删除角色
	 */
	public void delete(int userId) {
		super.bulkUpdate("delete from User_Role ur where ur.user.id=?", userId);
	}
	/**
	 * 通过角色id删除用户
	 */
	public void deleteU_R_ByRid(int roleId) {
		super.bulkUpdate("delete from User_Role ur where ur.role.id=?", roleId);
	}
	/**
	 * 通过用户和角色id删除
	 */
	public void delete(int userId, int roleId) {
		super.bulkUpdate("delete from User_Role ur where ur.user.id=" + userId +
			" and ur.role.id=" + roleId);
	}
}

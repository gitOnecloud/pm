package com.baodian.model.user;

import java.io.Serializable;

import com.baodian.model.role.Role;

@SuppressWarnings("serial")
public class User_RolePK implements Serializable {
	private User user;
	private Role role;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	@Override
	public boolean equals(Object o) {//判断逻辑属性和物理属性是否相同
		if(o instanceof User_RolePK) {
			User_RolePK pk = (User_RolePK)o;
			if(this.role.getId() == pk.role.getId() && this.user.getId() == pk.user.getId()) {
			  return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {//方便查找(主键)对应的对象
		return this.user.hashCode() + this.role.hashCode();
	}
}

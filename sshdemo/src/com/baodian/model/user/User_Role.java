package com.baodian.model.user;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.baodian.model.role.Role;

@Entity
@IdClass(User_RolePK.class)
@Table(name="user_role")
public class User_Role {
	private User user;
	private Role role;
	public User_Role() {}
	public User_Role(int userId, int roleId) {
		this.user = new User(userId);
		this.role = new Role(roleId);
	}
	public User_Role(Role role) {
		this.role = role;
	}
	public User_Role(int userId, Role role) {
		this.user = new User(userId);
		this.role = role;
	}
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
}

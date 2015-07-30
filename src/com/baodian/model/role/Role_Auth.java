package com.baodian.model.role;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(Role_AuthPK.class)
@Table(name="role_auth")
public class Role_Auth {
	private Authority authority;
	private Role role;
	public Role_Auth() {};
	public Role_Auth(int roleId, int authId) {
		this.role = new Role(roleId);
		this.authority = new Authority(authId);
	}
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	public Authority getAuthority() {
		return authority;
	}
	public void setAuthority(Authority authority) {
		this.authority = authority;
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

package com.baodian.model.role;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Role_AuthPK implements Serializable {
	private Authority authority;
	private Role role;
	public Authority getAuthority() {
		return authority;
	}
	public void setAuthority(Authority authority) {
		this.authority = authority;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	@Override
	public boolean equals(Object o) {//判断逻辑属性和物理属性是否相同
		if(o instanceof Role_AuthPK) {
			Role_AuthPK pk = (Role_AuthPK)o;
			if(this.role.getId() == pk.role.getId() && this.authority.getId() == pk.authority.getId()) {
			  return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {//方便查找(主键)对应的对象
		return this.role.hashCode();
	}
}

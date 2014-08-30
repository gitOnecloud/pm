package com.baodian.model.role;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="role")
public class Role {
	private int id;
	private String name;
	private int sort;
	private Role parent;
	private int defaultSet;
	
	private List<Role_Auth> role_Authorities;
	
	public Role() {}
	public Role(int id) {
		this.id = id;
	}
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=20, nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(columnDefinition="tinyint(1) not null")
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	public Role getParent() {
		return parent;
	}
	public void setParent(Role parent) {
		this.parent = parent;
	}
	@OneToMany(mappedBy="role")//,fetch=FetchType.LAZY)
	public List<Role_Auth> getRole_Authorities() {
		return role_Authorities;
	}
	public void setRole_Authorities(List<Role_Auth> role_Authorities) {
		this.role_Authorities = role_Authorities;
	}
	//0:不设置 1:设为默认
	@Column(columnDefinition="tinyint(1) not null default 0")
	public int getDefaultSet() {
		return defaultSet;
	}
	public void setDefaultSet(int defaultSet) {
		this.defaultSet = defaultSet;
	}
}

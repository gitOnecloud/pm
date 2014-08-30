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
@Table(name="authority")
public class Authority {
	private int id;
	private String name;
	private String url;
	private int sort;
	private Authority parent;
	private List<Authority> children;
	private int display;
	private List<Role_Auth> role_auths;
	
	public Authority() {}
	public Authority(int id) {
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
	@Column(columnDefinition="default'#'", length=50, nullable=false)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(columnDefinition="tinyint(1) not null")
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	public Authority getParent() {
		return parent;
	}
	public void setParent(Authority parent) {
		this.parent = parent;
	}
	@OneToMany(mappedBy="parent")
	public List<Authority> getChildren() {
		return children;
	}
	public void setChildren(List<Authority> children) {
		this.children = children;
	}
	@Column(columnDefinition="tinyint(1) not null default'1'",  insertable=false)
	public int getDisplay() {
		return display;
	}
	public void setDisplay(int display) {
		this.display = display;
	}
	@OneToMany(mappedBy="authority")
	public List<Role_Auth> getRole_auths() {
		return role_auths;
	}
	public void setRole_auths(List<Role_Auth> role_auths) {
		this.role_auths = role_auths;
	}
	
}

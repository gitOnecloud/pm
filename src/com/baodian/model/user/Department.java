package com.baodian.model.user;

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
@Table(name="department")
public class Department {
	private int id;
	private String name;
	private int sort;
	private Department parent;
	private List<Department> children;
	private List<User> users;
	
	public Department() {}
	public Department(int id) {
		this.id = id;
	}
	//User.init
	public Department(int id, String name) {
		this.id = id;
		this.name = name;
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
	public Department getParent() {
		return parent;
	}
	public void setParent(Department parent) {
		this.parent = parent;
	}
	@OneToMany(mappedBy="parent")
	public List<Department> getChildren() {
		return children;
	}
	public void setChildren(List<Department> children) {
		this.children = children;
	}
	@OneToMany(mappedBy="dpm")
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	/**
	 * check name
	 * @return 空:-1, 超出20:-2, 其他返回长度
	 */
	public int ckName() {
		if(name == null) {
			return -1;
		}
		if(name.length() > 20) {
			return -2;
		}
		return name.length();
	}
}

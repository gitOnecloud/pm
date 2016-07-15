package com.baodian.model.pm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baodian.model.util.UtilModel;
import com.baodian.util.StaticMethod;

@Entity
@Table
public class Project extends UtilModel {
	private int id;
	private String name;//简称
	private String fname;//全称
	private String remark;//备注
	private Date beginTime;//项目开始时间
	private Date endTime;//项目结束时间
	
	public Project() {}
	public Project(int id) {
		this.id = id;
	}
//
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(nullable=false)
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
//
	/**
	 * 验证输入是否符合要求
	 * @return 符合返回true
	 */
	public boolean validate() {
		if(! StaticMethod.checkStr(getName(), 1, 50)) {
			return false;
		} else if(! StaticMethod.checkStr(getFname(), 1, 50)) {
			return false;
		} else if(! StaticMethod.checkStr(this.getRemark(), 0, 80, true)) {
			return false;
		}
		return true;
	}
}

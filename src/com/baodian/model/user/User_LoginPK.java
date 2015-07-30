package com.baodian.model.user;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class User_LoginPK implements Serializable {
	private User user;
	private Date date;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public boolean equals(Object o) {//判断逻辑属性和物理属性是否相同
		if(o instanceof User_LoginPK) {
			User_LoginPK pk = (User_LoginPK)o;
			if(this.date.getTime() == pk.date.getTime()
					&& this.user.getId() == pk.user.getId()) {
			  return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {//方便查找(主键)对应的对象
		return this.user.hashCode() + this.date.hashCode();
	}
}

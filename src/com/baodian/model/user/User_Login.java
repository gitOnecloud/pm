package com.baodian.model.user;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@IdClass(User_LoginPK.class)
@Table(name="user_login")
public class User_Login {
	private User user;
	private Date date;
	public User_Login() {}
	public User_Login(int userId, Date date) {
		this.user = new User(userId);
		this.setDate(date);
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}

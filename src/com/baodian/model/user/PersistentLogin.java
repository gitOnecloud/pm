package com.baodian.model.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 将spring security remember me 持久化到数据库
 * @author LF_eng
 * 2014-10-17 20:25:56
 */
@Entity
@Table(name="persistent_logins")
public class PersistentLogin {

	private String username;
	private String series;
	private String token;
	private Date last_used;
	
	@Column(length=20, nullable=false)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Id
	@Column(length=64, nullable=false)
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	@Column(columnDefinition="char(32)", nullable=false)
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Column(columnDefinition="timestamp not null default current_timestamp")
	public Date getLast_used() {
		return last_used;
	}
	public void setLast_used(Date last_used) {
		this.last_used = last_used;
	}
	
}

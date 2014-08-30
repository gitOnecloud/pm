package com.baodian.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

import com.baodian.model.User;
import com.baodian.util.StaticMethod;

/**
 * 添加用户使用
 * @author LF_eng 2014-3-21 11:07:49
 */
public class UserDao extends NamedParameterJdbcDaoSupport {

//c
	/**
	 * 添加
	 */
	public void add(String account, String pass) {
		super.getJdbcTemplate().update(
			"insert into user(account, password, date, changeDate) values(?, ?, ?, ?)",
				account, pass, new Date(), new Date());
	}
//r
	/**
	 * 根据账号获取状态
	 */
	public List<User> getStatus(final String[] accounts) {
		StringBuilder params = new StringBuilder();
		for(int i=0; i<accounts.length; i++) {
			params.append(", ?");
		}
		return super.getJdbcTemplate().query(
			"select account,status from user where account in (" + params.substring(1) + ")",
			new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps)
						throws SQLException {
					for(int i=0; i<accounts.length; i++) {
						ps.setString(i+1, StaticMethod.hex2Str(accounts[i]));
					}
				}
			},
			ParameterizedBeanPropertyRowMapper.newInstance(User.class));
	}
	/**
	 * 判断账号是否已经使用
	 */
	public boolean accountIsUse(String account) {
		try{
			super.getJdbcTemplate().queryForInt("select id from user where account=? limit 1", account);
			return true;
		} catch(EmptyResultDataAccessException e) {
			return false;
		}
	}
	/**
	 * 判断账号是否可以更新
	 */
	public boolean accountIsUse(String account, String oldAccount) {
		try{
			super.getJdbcTemplate().queryForInt("select id from user where account=? and account!=? limit 1", account, oldAccount);
			return true;
		} catch(EmptyResultDataAccessException e) {
			return false;
		}
	}
	/**
	 * 检查密码
	 */
	public boolean checkPass(String account, String password) {
		try{
			super.getJdbcTemplate().queryForInt("select id from user where account=? and password=? limit 1", account, password);
			return true;
		} catch(EmptyResultDataAccessException e) {
			return false;
		}
	}
//u
	/**
	 * 更改账号和密码
	 */
	public void changeAccount(String oldAccount, String account, String pass) {
		super.getJdbcTemplate().update(
			"update user set account=?, password=?, changeDate=? where account=?",
				account, pass, new Date(), oldAccount);
	}
	/**
	 * 更改密码
	 */
	public void changePass(String account, String password) {
		super.getJdbcTemplate().update(
			"update user set password=?, changeDate=? where account=?",
				password, new Date(), account);
	}
	/**
	 * 更改状态
	 */
	public void updateStatus(final String[] accounts, int status) {
		StringBuilder params = new StringBuilder();
		for(int i=0; i<accounts.length; i++) {
			params.append(", ?");
		}
		super.getJdbcTemplate().update(
			"update user set status=" + status +
			" where account in (" + params.substring(1) + ")",
			new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps)
						throws SQLException {
					for(int i=0; i<accounts.length; i++) {
						ps.setString(i+1, StaticMethod.hex2Str(accounts[i]));
					}
				}
			});
	}

}

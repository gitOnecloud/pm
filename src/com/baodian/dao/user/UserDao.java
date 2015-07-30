package com.baodian.dao.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.baodian.dao.user.UserDao;
import com.baodian.dao.util.UtilDao;
import com.baodian.model.user.User;
import com.baodian.service.util.InitData;
import com.baodian.util.StaticMethod;
import com.baodian.util.page.UserPage;

@Repository("userDao")
public class UserDao extends UtilDao {

//c
	public void save(User user) {
		if(InitData.useCas) {
			//cas方式不需要本地保存密码，但要置为空，防止非cas方式使用时出错
			user.setPassword("");
		}
		user.setAllowLoginTime(new Date());
		super.save(user);
	}
//r
	/**
	 * 分页查找用户的id name account department
	 */
	public List<User> getU_inadOnPage(UserPage page) {
		StringBuilder sql = new StringBuilder("where 1=1");
		List<String> params = new ArrayList<String>();
		if(page.getDpmId() != 0) {
			sql.append(" and u.dpm.id=" + page.getDpmId());
		}
		if(StaticMethod.StrSize(page.getName()) > 0) {
			sql.append(" and u.name like ?");
			params.add(page.getName() + "%");
		}
		if(StaticMethod.StrSize(page.getAccount()) > 0) {
			sql.append(" and u.account like ?");
			params.add(page.getAccount() + "%");
		}
		return super.findByPage("select count(*) from User u " + sql.toString(),
				"select new User(u.id, u.name, u.account, u.date, u.status, d.id, d.name)" +
					"from User u, Department d " + sql.toString() +
					" and u.dpm.id=d.id order by d.id, u.id", page, params);
	}
	/**
	 * 查找用户的id name account department
	 * @param id
	 * @return
	 */
	public User getU_inadById(int id) {
		List<User> users = super.findByPart("select new User(u.id, u.name, u.account, d.id, d.name) " +
				"from User u, Department d where u.id=" + id, 1);
		if(users.size() > 0)
			return users.get(0);
		else
			return null;
	}
	/**
	 * 根据账号获取密码
	 * @param account 账号
	 * @return 不存在返回null
	 */
	public String getU_passByAccount(String account) {
		List<String> strs = super.findByPart("select u.password from User u where u.account=lower(?)", 1, account);
		if(strs.size() > 0)
			return strs.get(0);
		else
			return null;
	}
	/**
	 * 根据账号查看是否存在
	 * @param account 账号
	 */
	public boolean checkAccount(String account) {
		if((Long) find("select count(*) from User u where u.account=lower(?)", account).get(0) == 0) {
			return false;
		}
		return true;
	}
	/**
	 * 根据部门id查找此部门人数
	 * @param id 部门id
	 * @return
	 */
	public int getU_NumbyD_id(int id) {
		return super.countNum("select count(*) " +
				"from User u where u.dpm.id=" + id);
	}
	/**
	 * 根据账号查找用户个数，在注册和更改时用于判断此账号是否已经有人使用
	 * @param account
	 * @param uId 使用于更改账号时的查询
	 * @return
	 */
	public int getU_NumByAc(String account, int uId) {
		List<Integer> uids = super.find("select u.id " +
				"from User u where u.account=?", account);
		if(uids.size() > 0) {
			if(uId == uids.get(0)) {
				return 0;
			} else {
				return uids.size();
			}
		} else {
			return 0;
		}
	}
	/**
	 * 根据部门id组查找此部门组中所有人员
	 * @param pids "1,2,3"
	 * @return User(id name Department(did))
	 */
	public List<User> getU_ByDids(String dids) {
		return super.find("select new User(u.id, u.name, u.dpm.id)" +
				"from User u where u.dpm.id in(" + dids + ")");
	}
	/**
	 * 根据部门id查找此部门中所有人员
	 * @param did 1
	 * @return User(id name)
	 */
	public List<User> getU_ByDid(int did) {
		return super.find("select new User(u.id, u.name)" +
				"from User u where u.dpm.id=" + did);
	}
	/**
	 * 获取账号
	 */
	public String getAccountById(int id) {
		List<String> strs = super.findByPart("select u.account from User u where u.id=" + id, 1);
		if(strs.size() > 0)
			return strs.get(0);
		else
			return null;
	}
	/**
	 * 根据ids获取账号
	 */
	public List<String> getAccounts(Set<Integer> uids) {
		return super.find("select u.account from User u where u.id in(" +
				StaticMethod.arrayToStr(uids, ",")+ ")");
	}
	/**
	 * 根据账号查询用户状态，如果不存在返回-1
	 */
	public int getStatus(String account) {
		List<Integer> strs = super.findByPart("select u.status from User u where u.account=?", 1, account);
		if(strs.size() > 0)
			return strs.get(0);
		else
			return -1;
	}
	/**
	 * 根据账号获取用户信息
	 * @return 不存在返回null
	 */
	public User getUserByAccount(String account) {
		List<User> users = super.findByPart("from User u where u.account=?", 1, account);
		if(users.size() > 0)
			return users.get(0);
		else
			return null;
	}
	/**
	 * 根据账号获取id
	 * @return 不存在返回-1
	 */
	public int getUserIdByAccount(String account) {
		List<Integer> strs = super.findByPart("select u.id from User u where u.account=?", 1, account);
		if(strs.size() > 0)
			return strs.get(0);
		else
			return -1;
	}
	/**
	 * 一分钟登录错误次数
	 */
	public long getOneMinuteLoginNum(int userId) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -1);
		return (Long) find("select count(*) " +
				"from User_Login ul where ul.user.id=" + userId +
				" and ul.date>?", cal.getTime()).get(0);
	}
	/**
	 * 一分钟登录错误次数
	 */
	public long getOneDayLoginNum(int userId) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return (Long) find("select count(*) " +
				"from User_Login ul where ul.user.id=" + userId +
				" and ul.date>=?", cal.getTime()).get(0);
	}
//u
	/**
	 * 更改用户信息及部门
	 */
	public void update(User user) {
		String sql = "update User u set ";
		List<String> params = new ArrayList<String>();
		if(StaticMethod.checkStr(user.getName(), 2, 20)) {
			sql = sql.concat("u.name=?,");
			params.add(user.getName());
		}
		if(StaticMethod.checkStr(user.getAccount(), 2, 20)) {
			sql = sql.concat("u.account=?,");
			params.add(user.getAccount());
		}
		if(StaticMethod.StrSize(user.getPassword()) > 1) {
			sql = sql.concat("u.password=?,");
			params.add(user.getPassword());
		}
		if(user.getDpm() != null && user.getDpm().getId() > 0) {
			sql = sql.concat("u.dpm.id=" + user.getDpm().getId() + ",");
		}
		if(!sql.equals("update User u set ")) {
			sql = sql.substring(0, sql.length()-1)
					.concat(" where u.id=" + user.getId());
			super.bulkUpdate(sql, params);
		}
	}
	/**
	 * 根据账号更改密码
	 */
	public void updatePW(String account, String password) {
		super.bulkUpdate("update User u set u.password=? where u.account=?", password, account);
	}
	/**
	 * 为用户更改部门
	 */
	public void updateDpm(int uid, int did) {
		super.bulkUpdate("update User u set u.dpm.id=" + did + " where u.id=" + uid);
	}
	/**
	 * 更改状态，同时将允许登录时间改为现在
	 */
	public void updateStatus(int status, Set<Integer> uids) {
		super.bulkUpdate("update User u set u.status=" + status + ",u.allowLoginTime=? where u.id in(" +
				StaticMethod.arrayToStr(uids, ",")+ ")", new Date());
	}
	/**
	 * 更改最早可登录时间
	 */
	public void updateLoginTime(int userId, int addMinute) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, addMinute);
		super.bulkUpdate("update User u set u.allowLoginTime=?" +
				" where u.id=" + userId, cal.getTime());
	}
	/**
	 * 锁定用户
	 */
	public void lockAccount(int userId) {
		super.bulkUpdate("update User u set u.status=1" +
				" where u.id=" + userId);
	}
//d
	/**
	 * 删除用户登录错误的记录
	 */
	public void deleteUserLogin(Set<Integer> uids) {
		super.bulkUpdate("delete from User_Login ul where ul.user.id in(" +
				StaticMethod.arrayToStr(uids, ",")+ ")");
	}
	/**
	 * 删除昨天的登录错误信息
	 */
	public void deleteOldUserLogin(int id) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		super.bulkUpdate("delete from User_Login ul where ul.user.id=" + id +
				" and ul.date<?", cal.getTime());
	}
//o
	public User getUserByName(String username) {
		return (User) super.find("from User where name=?",username).get(0);
	}
	public List<String> getAllUserName() {
		List<String> usernames = super.find("select u.name from User u");
		return usernames;
	}

}

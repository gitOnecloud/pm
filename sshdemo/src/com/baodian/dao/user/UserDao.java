package com.baodian.dao.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.baodian.dao.user.UserDao;
import com.baodian.dao.util.UtilDao;
import com.baodian.model.user.User;
import com.baodian.util.StaticMethod;
import com.baodian.util.page.UserPage;

@Repository("userDao")
public class UserDao extends UtilDao {

//c
//r
	/**
	 * 分页查找用户的id name account department
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> getU_inadOnPage(final UserPage page) {
		final StringBuilder sql = new StringBuilder();
		if(page.getDpmId() != 0) {
			sql.append(" and u.dpm.id=" + page.getDpmId());
		}
		if(page.getName()!=null && page.getName().length()!=0) {
			sql.append(" and u.name like '" + page.getName().replace("'", "") + "%'");
		}
		if(page.getAccount()!=null && page.getAccount().length()!=0) {
			sql.append(" and u.account like '" + page.getAccount().replace("'", "") + "%'");
		}
		if(sql.length() != 0)
			sql.replace(0, 4, "where");
		long nums = (Long) super.find("select count(*) from User u " + sql.toString()).get(0);
		page.countPage((int) nums);
		if(nums == 0) {
			return Collections.emptyList();
		}
		if(sql.length() != 0)
			sql.append(" and u.dpm.id=d.id");
		else
			sql.append("where u.dpm.id=d.id");
		List<User> result = sf.getCurrentSession().createQuery("select new User(u.id, u.name, u.account, u.date, u.status, d.id, d.name)" +
				"from User u, Department d " + sql.toString() + " order by d.id")
			.setFirstResult(page.getFirstNum())
			.setMaxResults(page.getNum())
			.list();
		return result;
	}
	/**
	 * 查找用户的id name account department
	 * @param id
	 * @return
	 */
	public User getU_inadById(int id) {
		List<User> users = super.find("select new User(u.id, u.name, u.account, d.id, d.name) " +
				"from User u, Department d where u.id=?", id);
		if(users.size() > 0)
			return users.get(0);
		else
			return null;
	}
	/**
	 * 根据账号获取密码
	 * @param account 账号
	 * @return 密码
	 */
	/*public String getU_passByAccount(String account) {
		List<String> strs = super.find("select u.password from User u where u.account=lower(?)", account);
		if(strs.size() > 0)
			return strs.get(0);
		else
			return null;
	}*/
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
	 * 根据账号获取用户信息, 前提:此账户一定存在
	 * @param account 账号
	 * @return user
	 */
	public User getUserByU_a(String account) {
		return (User) super.find("from User u where u.account=?", account).get(0);
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
		List<String> strs = super.find("select u.account from User u where u.id=" + id);
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
		List<Integer> strs = super.find("select u.status from User u where u.account=?", account);
		if(strs.size() > 0)
			return strs.get(0);
		else
			return -1;
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
	 * @param user
	 */
	/*public void updatePW(User user) {
		super.bulkUpdate("update User u set u.password=? where u.account=?", user.getPassword(), user.getAccount());
	}*/
	/**
	 * 为用户更改部门
	 */
	public void updateDpm(int uid, int did) {
		super.bulkUpdate("update User u set u.dpm.id=" + did + " where u.id=" + uid);
	}
	/**
	 * 更改状态
	 */
	public void updateStatus(int status, Set<Integer> uids) {
		super.bulkUpdate("update User u set u.status=" + status + " where u.id in(" +
				StaticMethod.arrayToStr(uids, ",")+ ")");
	}
//d
//o
	public User getUserByName(String username) {
		return (User) super.find("from User where name=?",username).get(0);
	}
	public List<String> getAllUserName() {
		List<String> usernames = super.find("select u.name from User u");
		return usernames;
	}

}

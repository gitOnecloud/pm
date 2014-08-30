package com.baodian.service.user;

import java.security.interfaces.RSAPrivateKey;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jasig.cas.client.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import com.baodian.dao.role.RoleDao;
import com.baodian.dao.user.DepartmentDao;
import com.baodian.dao.user.UserDao;
import com.baodian.dao.user.User_RoleDao;
import com.baodian.model.role.Role;
import com.baodian.model.user.Department;
import com.baodian.model.user.User;
import com.baodian.model.user.User_Role;
import com.baodian.service.role.RoleManager;
import com.baodian.service.role.SecuManager;
import com.baodian.service.util.InitData;
import com.baodian.service.util.StaticData;
import com.baodian.util.StaticMethod;
import com.baodian.util.page.UserPage;
import com.baodian.util.rsa.RSACoder;

@Service("userManager")
public class UserManager {

	private UserDao userDao;
	@Resource(name="userDao")
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	private User_RoleDao user_RoleDao;
	@Resource(name="userRoleDao")
	public void setUser_RoleDao(User_RoleDao user_RoleDao) {
		this.user_RoleDao = user_RoleDao;
	}
	private RoleDao roleDao;
	@Resource(name="roleDao")
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
	private RoleManager rm;
	@Resource(name="roleManager")
	public void setRoleManager(RoleManager rm) {
		this.rm = rm;
	}
	private DepartmentDao ddao;
	@Resource(name="departmentDao")
	public void setDdao(DepartmentDao ddao) {
		this.ddao = ddao;
	}
	private StaticData sdata;
	@Resource(name="staticData")
	public void setSdata(StaticData sdata) {
		this.sdata = sdata;
	}
	@Autowired
	private SessionRegistry sessionRegistry;
	private Format ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
//c
	/**
	 * 添加用户
	 */
	public String save(User user, String json, RSAPrivateKey priKey) {
		//对客户端传入的密码等进行解密，并把解密结果重新加密，传给cas服务端
		StringBuilder url = new StringBuilder();
		url.append(InitData.casData.get("serverUserUrl"));
		url.append("?action=save&client=" + InitData.casData.get("client"));
		try {
			String decodePass = null;
			if(priKey == null || user.getPassword() == null) {//使用默认密码
				decodePass = StaticMethod.encodeSha1(user.getAccount() + "123456");
			} else {
				decodePass = RSACoder.decryptByPrivateKey(user.getPassword(), priKey);
			}
			String encodeUser = StaticMethod.rsaPUBEncode(decodePass + "_" + user.getAccount());
			url.append("&user=" + encodeUser);
		} catch (Exception e) {
			e.printStackTrace();
			return StaticMethod.errorMess("RSA加密出错！");
		}
		//把加密的数据传给cas服务器，进行账号注册
		try {
			//添加进cas服务器，参数client=域名，user=rsa(sha1(pass+account)+'_'+account)
			String result = CommonUtils.getResponseFromServer(url.toString(), "UTF-8");
			//System.out.println(result);
			JSONObject jo = JSONObject.fromObject(result);
			//服务端已经存在，继续添加客户端
			if(jo.getInt("status") != 0 &&
					! jo.getBoolean("accountIsUse")) {
				return result;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return StaticMethod.errorMess("cas服务器出错！");
		}
		
		if(userDao.checkAccount(user.getAccount())) {
			return "{\"status\":1,\"mess\":\"此账户已存在！\",\"account\":false}";
		}
		userDao.save(user);
		if(StaticMethod.StrSize(json) > 0) {
			Set<Integer> taskIds = new HashSet<Integer>();
			int rId = 0;
			for(String s : json.split("-")) {
				try {
					rId = Integer.parseInt(s);
					if(taskIds.add(rId) && rId > 0) {
						user_RoleDao.save(new User_Role(user.getId(), rId));
					}
				} catch(NumberFormatException e) {}
			}
		}
		return StaticMethod.addSucc;
	}
	/**
	 * 添加用户，自助注册
	 */
	public void save(User user) {
		userDao.save(user);
		List<Role> roles = roleDao.getDefaultRoles();
		if(roles != null) {
			for(Role role : roles) {
				user_RoleDao.save(new User_Role(user.getId(), role));
			}
		}
	}
//r
	/**
	 * 分页查找用户的id name account department
	 * @param page 分页信息 部门id
	 * @return json格式
	 */
	public String findU_inadOnPage(UserPage page) {
		List<User> users = userDao.getU_inadOnPage(page);
		if(users.size() == 0) {
			return "{\"total\":0,\"rows\":[]}";
		}
		//组装参数，传给cas服务端
		StringBuilder url = new StringBuilder();
		url.append(InitData.casData.get("serverUserStatusUrl"));
		url.append("?client=" + InitData.casData.get("client"));
		try {
			String encodeAction = StaticMethod.rsaPUBEncode("read");
			url.append("&action=" + encodeAction);
		} catch (Exception e) {
			e.printStackTrace();
			return StaticMethod.errorMess("RSA加密出错！");
		}
		for(User user : users) {
			url.append("&account=" + StaticMethod.Str2Hex(user.getAccount()));
		}
		//System.out.println(url.toString());
		//cas服务端状态索引，<账号，状态>
		Map<String, Integer> casStatus = new HashMap<String, Integer>();
		//访问cas服务器，返回账号状态
		try {
			//添加进cas服务器，参数client=域名，action=rsa(read/change),account=hex(账号)
			String result = CommonUtils.getResponseFromServer(url.toString(), "UTF-8");
			//System.out.println(result);
			JSONObject jo = JSONObject.fromObject(result);
			if(jo.getInt("status") != 0) {
				return result;
			}
			JSONArray array = jo.getJSONArray("user");
			for(int i=0; i<array.size(); i++) {
				JSONObject userJSON = array.getJSONObject(i);
				casStatus.put(userJSON.getString("account"), userJSON.getInt("status"));
			}
		} catch(Exception e) {
			e.printStackTrace();
			return StaticMethod.errorMess("cas服务器出错！");
		}
		
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		for(User user : users) {
			List<User_Role> urs = user_RoleDao.get_U_RByUId(user.getId());
			List<Integer> rids = new ArrayList<Integer>();
			String rnames = "";
			for(int j=0;j<urs.size();j++) {
				Role role = urs.get(j).getRole();
				if(j < 3) {
					rnames = rnames.concat(role.getName() + ",");
				}
				rids.add(role.getId());
			}
			if(urs.size() > 0) {
				if(urs.size() > 3)
					rnames = rnames.substring(0, rnames.length() -1 ).concat("...");
				else
					rnames = rnames.substring(0, rnames.length() -1 );
			}
			JSONObject userJson = new JSONObject();
			userJson.put("id", user.getId());
			userJson.put("user.name", user.getName());
			userJson.put("user.account", user.getAccount());
			userJson.put("role", rnames);
			userJson.put("dpmName", user.getDpm().getName());
			userJson.put("roleId", rids);
			userJson.put("depmId", user.getDpm().getId());
			userJson.put("status", user.getStatus());
			userJson.put("casStatus", casStatus.get(user.getAccount()));
			userJson.put("date", StaticMethod.DateToString(user.getDate()));
			array.add(userJson);
		}
		json.put("rows", array);
		return json.toString();
	}
	/**
	 * 根据id查找用户id name account department role
	 * @param id 用户id
	 * @return
	 */
	public User findU_inadrById(int id) {
		User user = userDao.getU_inadById(id);
		if(user == null)
			return null;
		user.setUser_roles(user_RoleDao.get_U_RByUId(id));
		return user;
	}
	/**
	 * 根据账号查看是否存在
	 * @param account 账号
	 */
	/*public boolean checkAccount(String account) {
		return userDao.checkAccount(account);
	}*/
	/**
	 * 根据部门id查找此部门中的子部门及用户，为0时从根节点取
	 * @param depmId
	 * @return [{id,1,name:"",pId:2}]
	 */
	public String findU_Ds(int did) {
		StringBuilder json = new StringBuilder();
		json.append('[');
		List<Integer> dids = StaticData.dchildren.get(did);
		if(dids != null)
			for(int id : dids) {
				Department dept = StaticData.depts.get(id);
		    	json.append("{\"id\":" + dept.getId() +"," +
						"\"name\":\"" + dept.getName() +"\"");
		    	if(StaticData.dchildren.get(id)!=null ||
		    			userDao.getU_NumbyD_id(id)!=0)//存在子部门或者用户
		    		json.append(",\"isParent\":true");
				json.append("},");
			}
		if(did != 0) {
			List<User> us = userDao.getU_ByDid(did);
			for(User u : us) {
				json.append("{\"id\":-" + u.getId() + "," +
						"\"name\":\"" + u.getName() + "\",\"iconSkin\":\"user\"},");
			}
		}
		if(json.length() != 1)
			return json.substring(0, json.length()-1) + ']';
		return json.toString() + ']';
	}
	/**
	 * 根据已读部门读取未读部门，及其的子部门和用户
	 * @param dpms 需要读的部门A已经读取的部门
	 * @return [{pid:0,list:[{id,1,name:"",pId:2}]}]
	 */
	public String findU_Ds(String dpms) {
		if(dpms==null || dpms.isEmpty()) return "[]";
		//已经检查过的
		Set<Integer> check = new HashSet<Integer>();
		//初步需要读取的
		List<Integer> unrd = new ArrayList<Integer>();
		String[] strs = dpms.split("A");
		int a = 0;
		for(String s : strs[0].split("a")) {
			try {
				a = Integer.parseInt(s);
				unrd.add(a);
			} catch(NumberFormatException e) {}
		}
		//最终需要读取的
		List<Integer> need = new ArrayList<Integer>();
		if(strs.length > 1)
			for(String s : strs[1].split("a")) {
				try {
					a = Integer.parseInt(s);
					check.add(a);
				} catch(NumberFormatException e) {}
			}
		//还未读取任何一个
		else need.add(0);
		for(int did : unrd) {
			addNeddPid(did, need, check);
		}
		StringBuilder json = new StringBuilder();
		json.append('[');
		for(int did : need) {
			json.append("{\"pid\":" + did + ",\"list\":" + findU_Ds(did) + "},");
		}
		if(json.length() != 1)
			return json.substring(0, json.length()-1) + ']';
		return json.toString() + ']';
	}
	/**
	 * 根据部门查找其父辈节点
	 * @param did 部门id
	 * @param need 保存父辈节点
	 * @param check 已经检查过的节点
	 */
	private void addNeddPid(int did, List<Integer> need, Set<Integer> check) {
		Department dpm = StaticData.depts.get(did);
		if(dpm == null) return;
		if(dpm.getParent() != null) {
			int a = dpm.getParent().getId();
			if( ! check.contains(a)) {
				addNeddPid(a, need, check);
			}
		}
		if( ! check.contains(did)) {
			need.add(did);
			check.add(did);
		}
	}
	/**
	 * 返回全部角色和部门
	 * @return {roles:[{id,name,pId}], dpms:[{id,name,pId}]}
	 */
	public String findDRs() {
		return "{\"roles\":" + rm.findRoles() +
				",\"dpms\":" + sdata.findDept() + "}";
	}
	/**
	 * 根据账号查询用户状态，如果不存在返回-1
	 */
	public int findStatus(String account) {
		return userDao.getStatus(account);
	}
//u
	/**
	 * 更改用户信息部门角色
	 * @param user 值为空不更改
	 * @param depmId 为-1时不更改
	 */
	public String change(User user, String json, RSAPrivateKey priKey) {
		if(StaticMethod.checkStr(user.getAccount(), 2, 20)) {//需要更新密码
			if(userDao.getU_NumByAc(user.getAccount(), user.getId()) != 0) {
				return "{\"status\":1,\"mess\":\"此账户已存在！\",\"account\":false}";
			}
			//更新服务端密码
			String oldAccount = userDao.getAccountById(user.getId());
			if(oldAccount == null) {
				return StaticMethod.inputError;
			}
			StringBuilder url = new StringBuilder();
			url.append(InitData.casData.get("serverUserUrl"));
			url.append("?action=changeAccount&client=" + InitData.casData.get("client"));
			url.append("&oldAccount=" + StaticMethod.Str2Hex(oldAccount));
			try {
				String decodePass = null;
				if(priKey == null || user.getPassword() == null) {//使用默认密码
					decodePass = StaticMethod.encodeSha1(user.getAccount() + "123456");
				} else {
					decodePass = RSACoder.decryptByPrivateKey(user.getPassword(), priKey);
				}
				String encodeUser = StaticMethod.rsaPUBEncode(decodePass + "_" + user.getAccount());
				url.append("&user=" + encodeUser);
			} catch (Exception e) {
				e.printStackTrace();
				return StaticMethod.errorMess("RSA加密出错！");
			}
			try {
				//修改cas服务器，参数client=域名，user=rsa(sha1(pass+account)+'_'+account)，oldAccount=hex()
				String result = CommonUtils.getResponseFromServer(url.toString(), "UTF-8");
				//System.out.println(result);
				JSONObject jo = JSONObject.fromObject(result);
				//jo.getBoolean("accountIsUse")
				if(jo.getInt("status") != 0) {
					return result;
				}
			} catch(Exception e) {
				e.printStackTrace();
				return StaticMethod.errorMess("cas服务器出错！");
			}
		}
		userDao.update(user);
		user_RoleDao.delete(user.getId());
		if(StaticMethod.StrSize(json) > 0) {
			Set<Integer> taskIds = new HashSet<Integer>();
			int rId = 0;
			for(String s : json.split("-")) {
				try {
					rId = Integer.parseInt(s);
					if(taskIds.add(rId) && rId > 0) {
						user_RoleDao.save(new User_Role(user.getId(), rId));
					}
				} catch(NumberFormatException e) {}
			}
		}
		return StaticMethod.changeSucc;
	}
	/**
	 * 更改密码
	 * @param user u.account旧密码 u.password新密码
	 * @return
	 */
	public String changePW(User user, RSAPrivateKey priKey) {
		String account = SecuManager.currentAccount();
		if(account.isEmpty()) {
			return StaticMethod.loginError;
		}
		if(user.getAccount() == null || user.getPassword() == null) {
			return StaticMethod.inputError;
		}
		StringBuilder url = new StringBuilder();
		url.append(InitData.casData.get("serverUserUrl"));
		url.append("?action=changePass&client=" + InitData.casData.get("client"));
		//RSA解密
		try {
			//新密码
			String password = RSACoder.decryptByPrivateKey(user.getPassword(), priKey);
			String encodeUser = StaticMethod.rsaPUBEncode(password + "_" + account);
			url.append("&user=" + encodeUser);
			//旧密码
			password = RSACoder.decryptByPrivateKey(user.getAccount(), priKey);
			encodeUser = StaticMethod.rsaPUBEncode(password);
			url.append("&oldPass=" + encodeUser);
		} catch (Exception e) {
			e.printStackTrace();
			return StaticMethod.errorMess("RSA加密出错！");
		}
		try {
			//修改cas服务器，参数client=域名，action=changePass，user=rsa(sha1(pass+account)+'_'+account)，oldPass=rsa()
			String result = CommonUtils.getResponseFromServer(url.toString(), "UTF-8");
			//密码错误返回 "{\"status\":1,\"mess\":\"密码错误！\",\"password\":false}";
			return result;
		} catch(Exception e) {
			e.printStackTrace();
			return StaticMethod.errorMess("cas服务器出错！");
		}
	}
	/**
	 * 移动用户到新部门
	 * @param json (dpmId A uid a uid)
	 */
	public String changeDpm(String json) {
		if(json==null || json.length()==0) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		String[] ids = json.split("A");
		int did = 0;
		try {
			did = Integer.parseInt(ids[0]);
		} catch(NumberFormatException e) {}
		if(! ddao.chkExit(did, "Department")) {
			return "{\"status\":1,\"mess\":\"部门不存在！\"}";
		}
		if(ids.length > 1) {
			int uid = 0;
			Set<Integer> uids = new HashSet<Integer>();
			for(String user : ids[1].split("a")) {
				try {
					uid = Integer.parseInt(user);
				} catch(NumberFormatException e) {}
				if(uids.add(uid) && userDao.chkExit(uid, "User")) {
					userDao.updateDpm(uid, did);
				}
			}
		}
		return StaticMethod.changeSucc;
	}
	/**
	 * 为用户更改角色
	 * @param json (type A roleIds A userIds) type 1替换 2增加 3删除
	 * @return
	 */
	public String changeRole(String json) {
		if(json==null || json.length()==0) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		String[] ids = json.split("A");
		if(ids.length < 3) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		int changeType = 1;
		try {
			changeType = Integer.parseInt(ids[0]);
		} catch(NumberFormatException e) {}
		int rid = 0;
		Set<Integer> rids = new HashSet<Integer>();
		List<Integer> roleIds = new ArrayList<Integer>();
		for(String role : ids[1].split("a")) {
			try {
				rid = Integer.parseInt(role);
			} catch(NumberFormatException e) {}
			if(rids.add(rid) && roleDao.chkExit(rid, "Role")) {
				roleIds.add(rid);
			}
		}
		if(roleIds.size() == 0) {
			return "{\"status\":1,\"mess\":\"角色不存在！\"}";
		}
		int uid = 0;
		Set<Integer> uids = new HashSet<Integer>();
		List<Integer> userIds = new ArrayList<Integer>();
		for(String user : ids[2].split("a")) {
			try {
				uid = Integer.parseInt(user);
			} catch(NumberFormatException e) {}
			if(uids.add(uid) && userDao.chkExit(uid, "User")) {
				userIds.add(uid);
			}
		}
		if(userIds.size() == 0) {
			return "{\"status\":1,\"mess\":\"用户不存在！\"}";
		}
		switch(changeType) {
			case 2://增加
				for(int userId : userIds) {
					for(int roleId : roleIds) {
						user_RoleDao.delete(userId, roleId);
						user_RoleDao.save(new User_Role(userId, roleId));
					}
				}
				break;
			case 3://删除
				for(int userId : userIds) {
					for(int roleId : roleIds) {
						user_RoleDao.delete(userId, roleId);
					}
				}
				break;
			default://替换
				for(int userId : userIds) {
					user_RoleDao.delete(userId);
					for(int roleId : roleIds) {
						user_RoleDao.save(new User_Role(userId, roleId));
					}
				}
		}
		return "{\"status\":0}";
	}
	/**
	 * 更改状态
	 * @param json status A type A id1 a id2
	 */
	public String changeStatus(String json) {
		if(StaticMethod.StrSize(json) < 5) {
			return StaticMethod.inputError;
		}
		String[] ids = json.split("A");
		if(ids.length != 3) {
			return StaticMethod.inputError;
		}
		int status = 1;
		int type = 0;
		try {
			status = Integer.parseInt(ids[0]);
			if(status < 0 || status > 1) {
				status = 1;
			}
			type = Integer.parseInt(ids[1]);
		} catch(NumberFormatException e) {}
		
		Set<Integer> uids = new HashSet<Integer>();
		for(String user : ids[2].split("a")) {
			try {
				uids.add(Integer.parseInt(user));
			} catch(NumberFormatException e) {}
		}
		if(uids.size() == 0) {
			return StaticMethod.inputError;
		}
		if(type == 0) {//更新本应用
			userDao.updateStatus(status, uids);
		} else {//更新cas服务器
			List<String> accounts = userDao.getAccounts(uids);
			//组装参数，传给cas服务端
			StringBuilder url = new StringBuilder();
			url.append(InitData.casData.get("serverUserStatusUrl"));
			url.append("?client=" + InitData.casData.get("client") + "&status=" + status);
			try {
				String encodeAction = StaticMethod.rsaPUBEncode("change");
				url.append("&action=" + encodeAction);
			} catch (Exception e) {
				e.printStackTrace();
				return StaticMethod.errorMess("RSA加密出错！");
			}
			for(String account : accounts) {
				url.append("&account=" + StaticMethod.Str2Hex(account));
			}
			//System.out.println(url.toString());
			//访问cas服务器，返回账号状态
			try {
				//添加进cas服务器，参数client=域名，action=rsa(read/change),account=hex(账号)
				String result = CommonUtils.getResponseFromServer(url.toString(), "UTF-8");
				//System.out.println(result);
				JSONObject jo = JSONObject.fromObject(result);
				if(jo.getInt("status") != 0) {
					return result;
				}
			} catch(Exception e) {
				e.printStackTrace();
				return StaticMethod.errorMess("cas服务器出错！");
			}
		}
		return StaticMethod.changeSucc;
	}
//d
	/**
	 * 删除用户，需要先核对自己的密码
	 * @param user u.id为要删除的id u.password为自己的密码
	 * @return
	 */
	/*public String remove(User user) {
		String account = SecuManager.currentAccount();
		String password = userDao.getU_passByAccount(account);
		if(user.getPassword().equals(password)) {
			user_RoleDao.delete(user.getId());
			userDao.delete(user.getId(), "User");
			return "{\"status\":0}";
		} else {
			return "{\"status\":1,\"mess\":\"密码错误！\",\"password\":false}";
		}
	}*/
//o
	/**
	 * 输出加密密码
	 */
	public void makePassword(String account, String password) {
		System.out.println(account + " -> " + "");
	}
	/**
	 * 登录用户数
	 */
	public int loginUserNums() {
		return sessionRegistry.getUserNum();
	}
	/**
	 * 登录用户
	 */
	public String findlgUser() {
		List<Object> slist = sessionRegistry.getAllPrincipals();
		StringBuilder json = new StringBuilder();
		json.append("共 " + sessionRegistry.getUserNum() + " 用户登录， " +
				sessionRegistry.getSessionNum() + " 处登录，服务器时间：" +
				ft.format(new Date()) + "<br>");
		for(int i=0; i<slist.size(); i++) {
			org.springframework.security.core.userdetails.User u = 
					(org.springframework.security.core.userdetails.User) slist.get(i);
			json.append("第 " + (i+1) + " 个用户，账号： " + u.getUsername() + "<br>");
			//包括被限制登录用户
			List<SessionInformation> ilist = sessionRegistry.getAllSessions(u, true);
			json.append("----" + ilist.size() + " 处登录<br>");
			for(int j=0; j<ilist.size(); j++) {
				SessionInformation sif = ilist.get(j);
				u = (org.springframework.security.core.userdetails.User) sif.getPrincipal();
				json.append("--------" + (j+1) +
						" 用户名：" + u.getStr()[0] +
						" 部门：" + StaticData.depts.get(u.getId()[1]).getName() +
						" 最后访问时间：" + ft.format(sif.getLastRequest()) +
						" ip：" +  sif.getRemoteIp() +
						//" session:" + sif.getSessionId() +
						//" 限制登录：" + sif.isExpired() +
						"<br>");
				//强制退出1.将其限制，2.将其移除，使用第一种比较好
				//sif.expireNow();
				//sessionRegistry.removeSessionInformation(sif.getSessionId());
			}
			json.append("<br>");
		}
		return json.toString();
	}
	/**
	 * 根据sessionID获取用户id，为防止伪造需要判断ip是否相同
	 * @param sessionID
	 * @param ip 用户ip
	 * @return 未登录返回0
	 */
	public int getUIDBySessionID(String sessionID, String ip) {
		try {
			
			SessionInformation sif = sessionRegistry.getSessionInformation(sessionID);
			if(sif == null) {
				return 0;
			}
			//System.out.println(sessionID + ": " + ip + ": " + sif.getRemoteIp());
			if(sif.getRemoteIp().equals(ip)) {
				return	((org.springframework.security.core.userdetails.User) sif.getPrincipal()).getId()[0];
			} else {
				//伪造session
				return 0;
			}
		} catch(Exception e) {
			//e.printStackTrace();
			return 0;
		}
	}
//
	public User getUserByName(String username) {
		return userDao.getUserByName(username);
	}

	@SuppressWarnings("rawtypes")
	public String getAllUserName() {
		String json = "[";
		String user_name;
		List usernames = userDao.getAllUserName();
		Iterator it = usernames.iterator();
		while(it.hasNext()){
			user_name = (String) it.next();
			json = json.concat("{\"id\":\""+user_name+"\",\"text\":\""+user_name+"\"},");
		}
		if(!usernames.isEmpty()){
			json = json.substring(0, json.length()-1);//去掉逗号
		}
		json = json.concat("]");
		return json;
	}
//spring security

}

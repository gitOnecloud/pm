package com.baodian.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.baodian.dao.ClientDao;
import com.baodian.dao.UserDao;
import com.baodian.model.Client;
import com.baodian.model.User;
import com.baodian.util.StaticMethod;
import com.baodian.util.rsa.RSACoder;

/**
 * 生成rsa密钥，私钥保存在session上，公钥返回给客户端
 * @author LF_eng
 * @create 2014-02-10 21:47
 * @update 
 */
public class UserController extends MultiActionController {

	private UserDao userDao;
	private ClientDao clientDao;
	
//c
	/**
	 * 添加用户
	 * 客户端传参格式： client=域名，action=changeAccount/save/changePass
	 * user=rsa(sha1(pass+account)+'_'+account)，oldAccount=hex()，oldPass=rsa()
	 */
	public ModelAndView saveOrChange_js(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		//查询客户端是否允许添加用户，条件为ip和rsa密钥相同
		String domain = request.getParameter("client");
		String user = request.getParameter("user");
		if(!StaticMethod.checkStr(domain, 1, 63) || user==null) {
			mv.addObject("status", 1);
			mv.addObject("mess", "服务端：输入有误！");
			return mv;
		}
		String remoteIp = StaticMethod.getRemoteAddr(request);
		Client client = clientDao.getRsa(remoteIp, domain);
		if(client == null) {
			mv.addObject("status", 1);
			mv.addObject("mess", "服务端：域名（" + domain + "）未注册或者ip（" + remoteIp + "）不允许！");
			return mv;
		}
		//进行RSA解密
		String decode = null;
		try {
			decode = RSACoder.decryptByPrivateKey(user, client.getPrikey(), client.getModkey());
			//System.out.println("服务端解密：" + decode);
		} catch(Exception e) {
			mv.addObject("status", 1);
			mv.addObject("mess", "服务端：RSA解密user失败！");
			return mv;
		}
		//添加user
		//user[0]=password user[1]=account
		String[] userStr = decode.split("_", 2);
		if(userStr.length != 2) {
			mv.addObject("status", 1);
			mv.addObject("mess", "服务端：输入有误！");
			return mv;
		}
		String action = request.getParameter("action");
		if(action.equals("save")) {//添加账号
			if(userDao.accountIsUse(userStr[1])) {
				mv.addObject("status", 1);
				mv.addObject("mess", "服务端：账号（" + userStr[1] + "）已经存在！");
				mv.addObject("accountIsUse", true);
				return mv;
			}
			try {
				userDao.add(userStr[1], StaticMethod.makePass(userStr[1], userStr[0]));
			} catch(Exception e) {
				e.printStackTrace();
				mv.addObject("status", 1);
				mv.addObject("mess", "服务端：添加失败！请检查输入");
				return mv;
			}
			mv.addObject("status", 0);
			mv.addObject("mess", "添加成功！");
			return mv;
		} else if(action.equals("changeAccount")) {//更新账号
			String oldAccount = StaticMethod.hex2Str(request.getParameter("oldAccount"));
			//System.out.println("oldAccount: " + oldAccount);
			if(userDao.accountIsUse(userStr[1], oldAccount)) {
				mv.addObject("status", 1);
				mv.addObject("mess", "服务端：账号（" + userStr[1] + "）已经存在！");
				//mv.addObject("accountIsUse", true);
				return mv;
			}
			try {
				userDao.changeAccount(oldAccount, userStr[1], StaticMethod.makePass(userStr[1], userStr[0]));
			} catch(Exception e) {
				e.printStackTrace();
				mv.addObject("status", 1);
				mv.addObject("mess", "服务端：更新失败！请检查输入");
				return mv;
			}
			mv.addObject("status", 0);
			mv.addObject("mess", "更新成功！");
			return mv;
		} else if(action.equals("changePass")) {//更改密码
			try {
				decode = RSACoder.decryptByPrivateKey(request.getParameter("oldPass"), client.getPrikey(), client.getModkey());
			} catch(Exception e) {
				mv.addObject("status", 1);
				mv.addObject("mess", "服务端：解密oldPass失败！");
				return mv;
			}
			if(userDao.checkPass(userStr[1], StaticMethod.makePass(userStr[1], decode))) {
				userDao.changePass(userStr[1], StaticMethod.makePass(userStr[1], userStr[0]));
				mv.addObject("status", 0);
				mv.addObject("mess", "更新成功！");
				return mv;
			} else {
				mv.addObject("status", 1);
				mv.addObject("mess", "服务端：密码错误！");
				mv.addObject("password", false);
				return mv;
			}
		}
		mv.addObject("status", 1);
		mv.addObject("mess", "服务端：参数action=" + action + " 错误！");
		return mv;
	}
	/**
	 * 用户状态
	 * 客户端传参格式： client=域名，action=rsa(read/change),account=hex(账号)，status=状态
	 */
	public ModelAndView status_js(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		//查询客户端是否允许添加用户，条件为ip和rsa密钥相同
		String domain = request.getParameter("client");
		String actionEncode = request.getParameter("action");
		if(!StaticMethod.checkStr(domain, 1, 63) || actionEncode==null) {
			mv.addObject("status", 1);
			mv.addObject("mess", "服务端：输入有误！");
			return mv;
		}
		String remoteIp = StaticMethod.getRemoteAddr(request);
		Client client = clientDao.getRsa(remoteIp, domain);
		if(client == null) {
			mv.addObject("status", 1);
			mv.addObject("mess", "服务端：域名（" + domain + "）未注册或者ip（" + remoteIp + "）不允许！");
			return mv;
		}
		//进行RSA解密
		String action = null;
		try {
			action = RSACoder.decryptByPrivateKey(actionEncode, client.getPrikey(), client.getModkey());
			//System.out.println("服务端解密：" + decode);
		} catch(Exception e) {
			mv.addObject("status", 1);
			mv.addObject("mess", "服务端：RSA解密action失败！");
			return mv;
		}
		String[] accounts = request.getParameterValues("account");
		if(accounts == null) {
			mv.addObject("status", 0);
			mv.addObject("mess", "没有账号！");
			return mv;
		}
		/*System.out.println("********************");
		for(String str : accounts) {
			System.out.println(StaticMethod.hex2Str(str));
		}
		System.out.println("********************");*/
		if(action.equals("read")) {//读取账号状态
			JSONObject json = new JSONObject();
			List<User> users = userDao.getStatus(accounts);
			mv = new ModelAndView("myJsonView");
			JSONArray array = new JSONArray();
			for(User user : users) {
				JSONObject userJSON = new JSONObject();
				userJSON.put("account", user.getAccount());
				userJSON.put("status", user.getStatus());
				array.add(userJSON);
			}
			json.put("status", 0);
			json.put("user", array);
			mv.addObject("json", json.toString());
			return mv;
		} else if(action.equals("change")) {//更改账号状态
			int status = 1;
			try {
				status = Integer.parseInt(request.getParameter("status"));
				if(status < 0 || status > 1) {
					status = 1;
				}
			} catch(NumberFormatException e) {}
			userDao.updateStatus(accounts, status);
			mv.addObject("status", 0);
			mv.addObject("mess", "更新成功！");
			return mv;
		}
		mv.addObject("status", 1);
		mv.addObject("mess", "服务端：参数action=" + action + " 错误！");
		return mv;
	}
	
//set get
	public UserDao getUserDao() {
		return userDao;
	}
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public ClientDao getClientDao() {
		return clientDao;
	}
	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}
}

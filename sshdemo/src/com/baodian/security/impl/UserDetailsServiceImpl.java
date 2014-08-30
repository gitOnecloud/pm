package com.baodian.security.impl;

import java.util.Map;

import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.baodian.security.UserOnLockException;
import com.baodian.service.user.UserManager;
import com.baodian.util.StaticMethod;

public class UserDetailsServiceImpl implements UserDetailsService {

	private UserManager userManager;
	private UserCache userCache;

	public UserManager getUserManager() {
		return userManager;
	}
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	public UserCache getUserCache() {
		return userCache;
	}
	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}

	@Override
	public UserDetails loadUserByUsername(String account)
			throws UsernameNotFoundException {
		return null;
	}
	
	@Override
	public UserDetails loadUserByUsername(Authentication authentication)
			throws UsernameNotFoundException {
		String account = authentication.getName().toLowerCase();
		//System.out.println(account);
		CasAssertionAuthenticationToken casToken = (CasAssertionAuthenticationToken)authentication;
		Map<String, Object> users = casToken.getAssertion().getPrincipal().getAttributes();
		//System.out.println(users);
		//账号在服务端状态
		int userStatus = StaticMethod.Str2Int((String) users.get("status"));
		if(userStatus != 0) {
			throw new UserOnLockException(account);
		}
		userStatus = userManager.findStatus(account);
		if(userStatus > 0) {
			throw new UserOnLockException(account);
		}
		if(userStatus == -1) {//账号不存在，就进行注册
			com.baodian.model.user.User user = casToken.getUser();
			if(user != null) {
				user.setAccount(account);
				try {
					userManager.save(user);
					return new User(account);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			throw new UsernameNotFoundException(account);
		}
		return new User(account);
	}
}

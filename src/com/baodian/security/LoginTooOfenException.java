package com.baodian.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 登录太频繁，不允许登录
 * @author LF_eng
 * @date 2014-9-17 17:39:33
 */
@SuppressWarnings("serial")
public class LoginTooOfenException extends UsernameNotFoundException {

	public LoginTooOfenException(String msg) {
		super(msg);
	}

}
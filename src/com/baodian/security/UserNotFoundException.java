package com.baodian.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 账号不存在
 * @author LF_eng
 * @date 2014-9-11 21:43:24
 */
@SuppressWarnings("serial")
public class UserNotFoundException extends UsernameNotFoundException {

	public UserNotFoundException(String msg) {
		super(msg);
	}

}

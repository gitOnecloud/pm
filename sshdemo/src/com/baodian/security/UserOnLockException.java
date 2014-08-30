package com.baodian.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 账号被锁定
 * @author LF_eng
 * @date 2014-4-6 22:54:10
 */
@SuppressWarnings("serial")
public class UserOnLockException extends UsernameNotFoundException {

	public UserOnLockException(String msg) {
		super(msg);
	}

}

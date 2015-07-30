package com.baodian.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * rsa解码失败
 * @author LF_eng
 * @date 2014-9-14 17:36:34
 */
@SuppressWarnings("serial")
public class RsaDecodeErrorException extends UsernameNotFoundException {

	public RsaDecodeErrorException(String msg) {
		super(msg);
	}

}

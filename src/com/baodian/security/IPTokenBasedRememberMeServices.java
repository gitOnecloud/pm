package com.baodian.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.DigestUtils;

import com.baodian.util.StaticMethod;

/**
 * 在remember me功能上加入ip、browser标志，但session是使用方单向生成，被盗取后不能使其失效
 */
@SuppressWarnings("deprecation")
public class IPTokenBasedRememberMeServices extends
		TokenBasedRememberMeServices {
	
	//设置和获取ThreadLocal HttpServletRequest
	private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();
	public HttpServletRequest getContext() {
		return requestHolder.get();
	}
	public void setContext(HttpServletRequest context) {
		requestHolder.set(context);
	}
	//用来为remember me处理设置cookie的值
	@Override
	public void onLoginSuccess(HttpServletRequest request,
			HttpServletResponse response,
			Authentication successfulAuthentication) {
		try {
			setContext(request);
			super.onLoginSuccess(request, response, successfulAuthentication);
		} finally {
			//用完之后要关闭
			setContext(null);
		}
	}
	//校验用户端提供的remember me cookie的内容
	@Override
	protected UserDetails processAutoLoginCookie(String[] cookieTokens,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			setContext(request);
			return super.processAutoLoginCookie(cookieTokens, request, response);
		} finally {
			setContext(null);
		}
	}

	//创建认证凭证的MD5哈希值(在这里加上IP)
	@Override
	protected String makeTokenSignature(long tokenExpiryTime, String username,
			String password) {
		return  DigestUtils.md5DigestAsHex((username + ":" + tokenExpiryTime + ":" +
					password + ":" + getKey() + ":" +
					StaticMethod.getRemoteAddr(getContext()) +
					StaticMethod.getBrowser(getContext())
				).getBytes());
	}
}

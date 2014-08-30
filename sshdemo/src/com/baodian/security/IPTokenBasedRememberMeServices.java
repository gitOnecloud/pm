package com.baodian.security;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.DigestUtils;

import com.baodian.util.StaticMethod;

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
	//获取IP地址
	protected String getUserIPAddress(HttpServletRequest request) {
		return StaticMethod.getRemoteAddr(request);
	}

	// 用来为remember me处理设置cookie的值
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
			// take off the last token
			String ipAddressToken = cookieTokens[cookieTokens.length - 1];
			if (!getUserIPAddress(request).equals(ipAddressToken)) {
				throw new InvalidCookieException(
						"Cookie IP Address did not contain a matching IP (contained '"
								+ ipAddressToken + "')");
			}
			return super.processAutoLoginCookie(
					Arrays.copyOf(cookieTokens, cookieTokens.length - 1),
					request, response);
		} finally {
			setContext(null);
		}
	}

	//创建认证凭证的MD5哈希值(在这里加上IP)
	@Override
	protected String makeTokenSignature(long tokenExpiryTime, String username,
			String password) {
		return DigestUtils.md5DigestAsHex((username + ":" + tokenExpiryTime + ":"
				+ password + ":" + getKey() + ":" + getUserIPAddress(getContext())).getBytes());
	}
	//保存cookie
	@Override
	protected void setCookie(String[] tokens, int maxAge,
			HttpServletRequest request, HttpServletResponse response) {
		// append the IP adddress to the cookie
		String[] tokensWithIPAddress = Arrays.copyOf(tokens, tokens.length + 1);
		tokensWithIPAddress[tokensWithIPAddress.length - 1] = getUserIPAddress(request);
		super.setCookie(tokensWithIPAddress, maxAge, request, response);
	}
}

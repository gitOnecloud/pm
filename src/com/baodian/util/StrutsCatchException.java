package com.baodian.util;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

@SuppressWarnings("serial")
//@Component//Struts异常拦截器，未使用
public class StrutsCatchException extends AbstractInterceptor {

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		try {
			return invocation.invoke();
		} catch(Exception e) {
			System.out.println("1111111");
			return "inputError";
		}
	}

}

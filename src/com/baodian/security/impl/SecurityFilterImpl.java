package com.baodian.security.impl;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

/**
 * 主要作用就是通过spring的IoC生成securityMetadataSource
 * securityMetadataSource相当于本包中自定义的MyInvocationSecurityMetadataSourceService
 * 该MyInvocationSecurityMetadataSourceService的作用提从数据库提取权限和资源，装配到HashMap中
 * 供Spring Security使用，用于权限校验。
 */
public class SecurityFilterImpl extends AbstractSecurityInterceptor implements Filter {

	//与applicationContext-security.xml里的myFilter的属性securityMetadataSource对应，  
    //其他的两个组件，已经在基类AbstractSecurityInterceptor定义
    private FilterInvocationSecurityMetadataSource securityMetadataSource;
	public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
		return securityMetadataSource;
	}
	public void setSecurityMetadataSource(
			FilterInvocationSecurityMetadataSource securityMetadataSource) {
		this.securityMetadataSource = securityMetadataSource;
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		FilterInvocation fi = new FilterInvocation(request, response, chain);  
		invoke(fi);
		
	}
	private void invoke(FilterInvocation fi) throws IOException, ServletException {
		//super.beforeInvocation(fi)执行：
		//1.获取请求资源的权限
		//	Collection<ConfigAttribute> attributes = SecurityMetadataSource.getAttributes(object);
		//2.是否拥有权限
        //	this.accessDecisionManager.decide(authenticated, object, attributes);
		InterceptorStatusToken token = super.beforeInvocation(fi);
		/*if(token == null) {//未在数据库中保存的url
			//System.out.print("未配置!!!\n");
			//throw new AccessDeniedException("未在数据库配置权限!!!");
		}*/
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }
	@Override
	public Class<? extends Object> getSecureObjectClass() {
		//类MyAccessDecisionManager的supports方面必须放回true,否则会提醒类型错误
        return FilterInvocation.class;
    }
	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}
	@Override
	public void destroy() {}
	@Override
	public void init(FilterConfig arg0) throws ServletException {}
	
}

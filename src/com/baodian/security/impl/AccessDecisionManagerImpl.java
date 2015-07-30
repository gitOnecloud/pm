package com.baodian.security.impl;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class AccessDecisionManagerImpl implements AccessDecisionManager{

	@Override
	/**
	 * authentication:当前用户的信息->包含属性：权限Authority(实际为角色id),用户Principal（可转换成User类）
	 * configAttributes：请求资源需要的权限
	 */
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes) throws AccessDeniedException,
			InsufficientAuthenticationException {
		if(configAttributes == null) {
//System.out.print("未配置权限!!!\n");
			return ;
		}
		Collection<? extends GrantedAuthority> auths = authentication.getAuthorities();
		if(!auths.isEmpty()) {
			Iterator<ConfigAttribute> ite = configAttributes.iterator();
			while(ite.hasNext()){
				String attr = ite.next().getAttribute();
				Iterator<? extends GrantedAuthority> itee = auths.iterator();
				while(itee.hasNext()) {
					if(itee.next().getAuthority().equals(attr)) {
//System.out.print("有权限访问!!!\n");
						return ;
					}
				}
			}
		}
//System.out.print("没有权限访问!!!\n");
		throw new AccessDeniedException("没有权限访问!");
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}
	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}

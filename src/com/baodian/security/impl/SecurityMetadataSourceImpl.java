package com.baodian.security.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.baodian.model.role.Authority;
import com.baodian.service.role.SecuManager;

/**
 * 最核心的地方，就是提供某个资源对应的权限定义，即getAttributes方法返回的结果。
 * 注意，我例子中使用的是AntUrlPathMatcher这个path matcher来检查URL是否与资源定义匹配，
 * 事实上你还要用正则的方式来匹配，或者自己实现一个matcher。
 *
 * 此类在启动web时就会初始化，为取得所有资源及其对应角色的定义，
 * 需要提供一个以可以查询数据库的Manager为参数的构造函数(普通注入是无效的)
 * 
 */
public class SecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource{
    private SecuManager secuManager;
    /**
     * 权限id -> 所需角色id
     */
    public static Map<String, Collection<ConfigAttribute>> authorityMap;
    /**
     * 菜单(权限)列表
     */
    public static List<Authority> menuList;
    /**
     * 无权限菜单
     */
    public static Set<Integer> defaultMenu;
    
    public SecurityMetadataSourceImpl(SecuManager secuManager) {
    	this.secuManager = secuManager;
    	//加载所有资源与权限的关系resourceMap
    	this.secuManager.initAll_RA_();
    	this.secuManager.initMenu();
    }
    
    //返回所请求资源所需要的权限
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
    	String requestUrl = ((FilterInvocation) object).getRequestUrl();
    	int i = requestUrl.indexOf("?");//过滤'?'后面的参数
        if(i != -1) {
        	requestUrl = requestUrl.substring(0, i);
        }
        //去掉action前面的'/'
        requestUrl = requestUrl.substring(requestUrl.lastIndexOf("/") + 1);
//System.out.print("[ " + requestUrl + " ] -> ");
        //不需要检查权限，改为直接在security.xml文件中配置
		/*if(requestUrl.endsWith("_no.action") || requestUrl.endsWith("_no_rd.action")) {
			return null;
		}*/
        return authorityMap.get(requestUrl);
    }
    @Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
    	Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();
        for (Map.Entry<String, Collection<ConfigAttribute>> entry : authorityMap.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }
        return allAttributes;
	}
	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}


cas_20140209:
	1.修改类：
	  a.cas-server-core.jar
		org.jasig.cas.web.flow.GenerateServiceTicketAction
			检查登录状态，返回json数据，标记为j_ajax_check
		org.jasig.cas.web.flow.GenerateLoginTicketAction
			检查登录状态，或者重新验证但失败，那么返回json数据，标记为j_ajax_check || j_ajax_validate
			生成rsa密钥
	    org.jasig.cas.web.flow.AuthenticationViaFormAction
			rsa解密后再登录
		org.jasig.cas.CentralAuthenticationService
			获取已登录的用户名
		org.jasig.cas.CentralAuthenticationServiceImpl
			获取已登录的用户名
	  b.spring-security-web.jar
	    org.springframework.security.web.access.expression.WebSecurityExpressionRoot
	   	 	增加spel:hasIpAndRole权限判断
	2.去除https
	  a.WEB-INF/spring-configuration/warnCookieGenerator.xml
		 	修改p:cookieSecure的值为false
	  b.WEB-INF/spring-configuration/ticketGrantingTicketCookieGenerator.xml
	  		同样修改p:cookieSecure的值为false
	  c.打开WEB-INF/deployerConfigContext.xml
	  		找到查找org.jasig.cas.authentication.handler.support.HttpBasedServiceCredentialsAuthenticationHandler 
	  		添加参数p:requireSecure="false"
	3.添加action
	  a.在WEB-INF/web.xml上添加
		<servlet-mapping>
			<servlet-name>cas</servlet-name>
			<url-pattern>/user</url-pattern>
		</servlet-mapping>
	  b.在cas-servlet.xml上添加
	  	<prop key="/user/*">clientController</prop>，在<bean id="handlerMappingC"><property name="mappings">中
		<bean id="userController" class="com.baodian.action.UserController"/>
	
cas_20140214:
	1.url添加参数：j_ajax=true可以返回ajax格式
	2.所以更改部分加上@update标记
	3.password = sha1( user + 'LF_eng' + sha1( user + pass ) )
		com.baodian.action.UserController.add_js()
		org.jasig.cas.web.flow.AuthenticationViaFormAction.submit()
	4.get传中文：
		StaticMethod.tomcatDecode(request, code)
		StaticMethod.tomcatDecode(code)

cas_20140405:
	1.deployerConfigContext.xml中的attributeRepository，登录后会把信息缓存到内存，需要先退出然后登录（直接登录无效）
	
	

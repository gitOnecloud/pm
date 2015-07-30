sshdemo_20130930
	*1.更换架构为struts2.3.15.2 + hibernate4.2.6 +
		spring3.2.4 + spring-security3.1.4
		1) spring-asm这个包已经不需要，因为spring-core已经包含进去了
		2) spring-transaction更新为：spring-tx
		3) ehcache从spring-context移到了spring-context-support
		4) commons-lang替换成commons-lang3
		5) 去除slf4j-api，slf4j-nop，commons-collections
			hibernate3，jta
		6) 增加hibernate-commons-annotations-4.0.2.Final.jar
			hibernate-core-4.2.6.Final.jar
			jboss-logging-3.1.0.GA.jar
			jboss-transaction-api_1.1_spec-1.0.1.Final.jar
			mchange-commons-java-0.2.3.4.jar
		7) beans.xml配置文件的更改
			a.sessionFactory
				从org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean
				换org.springframework.orm.hibernate4.LocalSessionFactoryBean
			b.transactionManager
				从org.springframework.orm.hibernate3.HibernateTransactionManager
				换org.springframework.orm.hibernate4.HibernateTransactionManager
			c.去掉hibernateTemplate，事务改为hibernate来实现
			d.sessionFactory的hibernateProperties加上
				<prop key="hibernate.current_session_context_class">thread</prop>
		8) Hibernate所有的openSession()改为getCurrentSession()，

sshdemo_20131224
	*1.原版本的StaticDataManager的init方法已不能正常使用，需要更改为
	*2.原版本SecuManager中的所有static方法失效，需要使用SpringUtil.context代替

sshdemo_20140126
	1.增加cas单点登录
	2.应该将ajax访问的标记由url控制，改为加参数"j_ajax=true"控制
	
sshdemo_20140318
	1.到服务器端检测登录
		http://192.168.10.118:8081/cas/login?callback=jQuery18205496470294220928_1395106376666&
			j_ajax_check=true&
			service=http://192.168.1.118:8080/sshdemo/j_spring_cas_security_check
		返回：jQuery18205496470294220928_1395106376666({
				"mess": "已登录！", "status": 0, "login": true, "userName": "b051"});
	2.已登录，进行验证
	  a.服务端验证登录
		http://192.168.10.118:8081/cas/login?callback=jQuery18205496470294220928_1395106376666&
			j_ajax=true&
			j_ajax_validate=true&
			service=http://192.168.1.118:8080/sshdemo/j_spring_cas_security_check
		返回：jQuery18205496470294220928_1395106376666({
				"clientUrl": "http://192.168.1.118:8080/sshdemo/j_spring_cas_security_check?ticket=ST-11-HflHbUe9UL9VgCfYDfsf-cas.baodian.com",
				"status":    0});
	  b.客户端登录
	  	http://192.168.1.118:8080/sshdemo/j_spring_cas_security_check?
	  		ticket=ST-12-1QEKBGGatkaTpirnjOec-cas.baodian.com&
	  		j_ajax=true
	  	返回：用户登录成功的信息
	3.重新登录
	  a.获取rsa公钥
		http://192.168.10.118:8081/cas/login?callback=jQuery18205496470294220928_1395106376666&
			j_ajax=true&
			renew=true&
			service=http://192.168.1.118:8080/sshdemo/j_spring_cas_security_check
		返回：jQuery18205496470294220928_1395106376666({
				"lt":            "LT-5-hRC62MxBjqR14kMIZ6nyLLhWfCi75f", 
				"execution":     "e1s1", 
				"rsa_publicKey": "9e179447cc074ab8a3540c6a84a7d5106bb38eb3fdc3d7f8d69e1782ad79dc1084c30872e09809d6edabe50048250270c16707a2b5bb1246af82aa1fe4242e95b2d092e00e81bfa355e8ee00765b86a2aa6320d2ff030f4496e851d7f159cc921c9d2538686766005c2b448e517df6afb6fd400d2240d91b59574ddd370f1ac9",
				"status":         0});
	  b.服务端请求登录
		http://192.168.10.118:8081/cas/login?callback=jQuery18205496470294220928_1395106376666&
			execution=e1s1&
			lt=LT-5-hRC62MxBjqR14kMIZ6nyLLhWfCi75f&
			_eventId=submit&
			j_ajax=true&
			renew=true&
			username=b051&
			password=8119179f911959ce64811921cb04cafb18d9a097c23985526c8080e3173c7b846ca51a6b5ae0b043bb358195970ed2267c30e95a4b19a102a6cba070beee45b267d659210020e01869591afa67da28314e4c11e7afe1563bdc271544a9d10009abf0bce78471b4dd2c4e1bf139fb12f43c3bee6c2c59f369785223fb6b39edb9
	 	返回：jQuery18205496470294220928_1395106376666({
	 			"clientUrl": "http://192.168.1.118:8080/sshdemo/j_spring_cas_security_check?ticket=ST-10-W6bjt9sVU6bcxLk05FPD-cas.baodian.com", 
	 			"status":    0});
	  c.客户端请求登录
	  	http://192.168.1.118:8080/sshdemo/j_spring_cas_security_check?
	  		ticket=ST-10-W6bjt9sVU6bcxLk05FPD-cas.baodian.com&
	  		j_ajax=true
		返回：用户登录成功的信息
	4.服务器端存在，客户端进行注册
	  a.先到服务器端进行验证登录状态，参照第2点a项
	  b.客户端注册
	  	http://192.168.1.118:8080/sshdemo/j_spring_cas_security_check?
	  		ticket=ST-11-HflHbUe9UL9VgCfYDfsf-cas.baodian.com&
	  		j_ajax=true&
	  		dpm=1&
	  		name=lbf
	5.服务端注册
	
sshdemo_20141017
	1.将记住密码功能持久化到数据库
	
	
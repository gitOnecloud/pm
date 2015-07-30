package com.baodian.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Service;

import com.baodian.dao.user.DepartmentDao;
import com.baodian.model.user.Department;
import com.baodian.service.util.StaticData;

/**
 * 保存静态数据，主要是从数据库读取的
 * 之前加@PostConstruct注解会失败，改用监听实现
 * @author LF_eng
 */
@Service("staticData")
public class StaticData implements ApplicationListener<ApplicationContextEvent> {
//static data
	/**
	 * 部门，使用get(did)返回这个部门Department
	 */
	public static Map<Integer, Department> depts = new HashMap<Integer, Department>();
	/**
	 * 部门顺序
	 */
	public List<Integer> dpmIndex = new ArrayList<Integer>();
	/**
	 * 子部门,使用get(did)返回其子部门,无子部门返回为null,get(0)返回根节点
	 */
	public static Map<Integer, List<Integer>> dchildren = new HashMap<Integer, List<Integer>>();
	/**
	 * 用来获取spring bean
	 * 使用方法： SpringUtil.context.getBean("beanName");
	 * 或者使用ApplicationContextAware去实现
	 */
	public static ApplicationContext context;
//dao
	@Resource(name="departmentDao")
	private DepartmentDao departmentDao;
//init
	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		StaticData.context = event.getApplicationContext();
		init();
	}
	public void init() {
		int a;
		//部门
		List<Department> dps = departmentDao.getAll();
		for(Department dpm : dps) {
			depts.put(dpm.getId(), dpm);
			dpmIndex.add(dpm.getId());
		}
		//子部门
		Iterator<Department> it = depts.values().iterator();
	    while(it.hasNext()) {
	    	Department dept = it.next();
	    	if(dept.getParent() != null)
		    	a = dept.getParent().getId();
	    	else a = 0;//根节点
	    	if(dchildren.containsKey(a)) {
	    		dchildren.get(a).add(dept.getId());
	    	} else {
	    		List<Integer> ids = new ArrayList<Integer>();
	    		ids.add(dept.getId());
	    		dchildren.put(a, ids);
	    	}
		}
		System.out.println("***初始化StaticData(静态数据)成功***");
	}
//read
	/**
	 * 返回所有部门
	 * @return [{id,name,sort,open,pId}]
	 */
	public String findDept() {
		if(depts.size() == 0) {
			return "[]";
		}
	    JSONArray array = new JSONArray();
		Department dept;
		for(int did : dpmIndex) {
			dept = depts.get(did);
			JSONObject deptJSON = new JSONObject();
			deptJSON.put("id", dept.getId());
			deptJSON.put("name", dept.getName());
			deptJSON.put("sort", dept.getSort());
			deptJSON.put("open", true);
			if(dept.getParent() != null) {
				deptJSON.put("pId", dept.getParent().getId());
			}
			array.add(deptJSON);
		}
		return array.toString();
	}
	/**
	 * 重新读取数据
	 */
	public void reload() {
		depts.clear();
		dpmIndex.clear();
		dchildren.clear();
		this.init();
	}
//update
//output
	public void output() {
		System.out.println();
		System.out.println("部门 => depts");
		System.out.println(findDept());
		System.out.println("子部门 => dchildren");
		for(Entry<Integer, List<Integer>> settt : dchildren.entrySet()) {
	    	List<Integer> iiii = settt.getValue();
	    	System.out.print(settt.getKey() + " => ");
	    	for(int i=0;i<iiii.size();i++)
	    		System.out.print(iiii.get(i) + " ");
	    	System.out.println();
		}
	}
}

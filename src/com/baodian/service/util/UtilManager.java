package com.baodian.service.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.baodian.dao.util.UtilDao;
import com.baodian.model.util.UtilModel;
import com.baodian.util.StaticMethod;

@Service//UtilDao如果用@Resource方式，会造成循环引用
public abstract class UtilManager extends UtilDao {

//c
	/**
	 * 验证通过后再保存
	 * @param obj
	 * @return 返回保存结果
	 */
	public String saveAfterValidate(UtilModel obj) {
		if(obj==null || ! obj.validate()) {
			return StaticMethod.errorMess("输入有误");
		}
		this.save(obj);
		return StaticMethod.addSucc;
	}
//r
//u
	/**
	 * 验证通过后再保存
	 * @param obj
	 * @return 返回保存结果
	 */
	public String changeAfterValidate(UtilModel obj) {
		if(obj==null || ! obj.validate()) {
			return StaticMethod.errorMess("输入有误");
		}
		this.update(obj);
		return StaticMethod.changeSucc;
	}
//d
	/**
	 * 根据id组，删除对象
	 * @param json id1-id2
	 * @param objName 要删除的对象名称
	 * @return 删除结果，json格式
	 */
	public String remove(String json, String objName) {
		if(StaticMethod.StrSize(json) < 1) {
			return StaticMethod.inputError;
		}
		Set<String> projectIds = new HashSet<String>();
		for(String s : json.split("-")) {
			try {
				Integer.parseInt(s);
				if(projectIds.add(s)) {
					this.delete(s, objName);
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.removeSucc;
	}
	
}

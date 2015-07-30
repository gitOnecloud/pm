package com.baodian.dao.log;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.baodian.dao.util.UtilDao;
import com.baodian.model.log.Log;
import com.baodian.util.StaticMethod;
import com.baodian.util.page.LogPage;

@Repository("logDao")
public class LogDao extends UtilDao {

//c
//r
	/**
	 * 按条件获取日志
	 * @param isAll true获取全部 false分页获取
	 */
	public List<Log> getLogOnPage(LogPage page, boolean isAll) {
		StringBuilder sql = new StringBuilder(" where 1=1");
		List<String> params = new ArrayList<String>();
		if(StaticMethod.isDate(page.getBeginDate())) {
			sql.append(" and log.date>='" + page.getBeginDate() + "'");
		}
		if(StaticMethod.isDate(page.getEndDate())) {
			sql.append(" and log.date<='" + page.getEndDate() + " 23:59:59'");
		}
		if(StaticMethod.StrSize(page.getType()) > 0) {
			sql.append(" and log.type=?");
			params.add(page.getType());
		}
		if(StaticMethod.StrSize(page.getContent()) > 0) {
			sql.append(" and log.content like ?");
			params.add("%" + page.getContent() + "%");
		}
		if(isAll) {
			return super.find("from Log log" + sql.toString(), params);
		} else {
			return super.findByPage("select count(*) from Log log" + sql.toString(),
					"from Log log" + sql.toString() +
					" order by log.id desc", page, params);
		}
		
	}
//u
//d

}

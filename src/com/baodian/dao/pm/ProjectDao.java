package com.baodian.dao.pm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.baodian.dao.util.UtilDao;
import com.baodian.model.pm.Project;
import com.baodian.util.StaticMethod;
import com.baodian.util.page.ProjectPage;

@Repository
public class ProjectDao extends UtilDao {
//c
//r
	/**
	 * 按条件获取
	 */
	public List<Project> getProjectOnPage(ProjectPage page) {
		StringBuilder sql = new StringBuilder(" where 1=1");
		List<String> params = new ArrayList<String>();
		if(StaticMethod.StrSize(page.getName()) > 0) {
			sql.append(" and (project.name like ? "
					+ "or project.fname like ? "
					+ "or project.remark like ?)");
			params.add("%" + page.getName() + "%");
			params.add("%" + page.getName() + "%");
			params.add("%" + page.getName() + "%");
		}
		return super.findByPage("select count(*) from Project project" + sql.toString(),
				"from Project project" + sql.toString() +
				" order by project.id desc", page, params);
		
	}
//u
//d
}

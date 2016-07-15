package com.baodian.service.pm;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.pm.ProjectDao;
import com.baodian.model.pm.Project;
import com.baodian.service.util.UtilManager;
import com.baodian.util.StaticMethod;
import com.baodian.util.page.ProjectPage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ProjectManager extends UtilManager {
	@Resource
	private ProjectDao pDao;
//c
//r
	public String findProjectOnPage(ProjectPage page) {
		List<Project> projects = pDao.getProjectOnPage(page);
		if(projects.size() == 0) {
			return "{\"total\":0,\"rows\":[]}";
		}
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		for(Project project : projects) {
			JSONObject projectJson = new JSONObject();
			projectJson.put("id", project.getId());
			projectJson.put("name", project.getName());
			projectJson.put("fname", project.getFname());
			projectJson.put("remark", project.getRemark());
			projectJson.put("beginTime", StaticMethod.DateToString(project.getBeginTime()));
			projectJson.put("endTime", StaticMethod.DateToString(project.getEndTime()));
			array.add(projectJson);
		}
		json.put("rows", array);
		return json.toString();
	}
//u
//d
	
}

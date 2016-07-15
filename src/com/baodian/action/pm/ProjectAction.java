package com.baodian.action.pm;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.baodian.action.UtilAction;
import com.baodian.model.pm.Project;
import com.baodian.service.pm.ProjectManager;
import com.baodian.util.page.ProjectPage;

@SuppressWarnings("serial")
@Controller("project")
@Scope("prototype")//必须注解为多态
public class ProjectAction extends UtilAction {
	static Logger logger = LogManager.getLogger(); 
	@Resource
	private ProjectManager pm;
	private ProjectPage page;
	private Project project;
//c
	public String save_js() {
		json = pm.saveAfterValidate(project);
		return JSON;
	}
//r
	public String list() {
		return SUCCESS;
	}
	public String list_js() {
		if(page == null)
			page = new ProjectPage();
		json = pm.findProjectOnPage(page);
		return JSON;
	}
//u
	public String change_js() {
		json = pm.changeAfterValidate(project);
		return JSON;
	}
//d
	public String remove_js() {
		json = pm.remove(json, new Project().getClass().getName());
		return JSON;
	}
//set get
	public ProjectPage getPage() {
		return page;
	}
	public void setPage(ProjectPage page) {
		this.page = page;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
}

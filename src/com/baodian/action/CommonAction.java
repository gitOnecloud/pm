package com.baodian.action;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class CommonAction extends ActionSupport {
	public String json;
	public String JSON = "json";
	public String EXCEL = "excel";
//set get
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
}

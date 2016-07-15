package com.baodian.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.service.log.LogManager;
import com.baodian.util.StaticMethod;
import com.baodian.util.page.LogPage;

@SuppressWarnings("serial")
@Component("log")
@Scope("prototype")//必须注解为多态
public class LogAction extends UtilAction {

	@Resource(name="logManager")
	private LogManager lm;
	private LogPage page;
	private String downloadFileName;
	private InputStream excelStream;
//c
//r
	public String list() {
		json = lm.findLogType();
		return SUCCESS;
	}
	public String list_js() {
		if(page == null)
			page = new LogPage();
		json = lm.findLogOnPage(page);
		return JSON;
	}
	public String download_rd() throws IOException {
		if(page == null)
			page = new LogPage();
		excelStream = lm.download(page);
		if(excelStream == null) {
			json = StaticMethod.inputError;
			return JSON;
		}
		downloadFileName = StaticMethod.tomcatEncode("日志"+StaticMethod.DateToDay(new Date()));
		return EXCEL;
	}
//u
//d
	public String remove_js() {
		json = lm.remove(json);
		return JSON;
	}
//set get
	public LogPage getPage() {
		return page;
	}
	public void setPage(LogPage page) {
		this.page = page;
	}
	public String getDownloadFileName() {
		return downloadFileName;
	}
	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}
	public InputStream getExcelStream() {
		return excelStream;
	}
	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}
}

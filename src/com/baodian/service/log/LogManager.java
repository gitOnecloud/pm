package com.baodian.service.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.baodian.dao.log.LogDao;
import com.baodian.model.log.Log;
import com.baodian.service.export.ExcelManager;
import com.baodian.service.util.InitData;
import com.baodian.util.StaticMethod;
import com.baodian.util.page.LogPage;

@Service("logManager")
public class LogManager {
	@Resource(name="logDao")
	private LogDao logDao;
	@Resource(name="excelManager")
	private ExcelManager em;
//r
	public String findLogOnPage(LogPage page) {
		List<Log> logs = logDao.getLogOnPage(page, false);
		if(logs.size() == 0) {
			return "{\"total\":0,\"rows\":[]}";
		}
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		for(Log log : logs) {
			JSONObject logJson = new JSONObject();
			logJson.put("id", log.getId());
			logJson.put("content", log.getContent());
			logJson.put("type", log.getType());
			logJson.put("date", StaticMethod.DateToString(log.getDate()));
			array.add(logJson);
		}
		json.put("rows", array);
		return json.toString();
	}
	/**
	 * 获取日志类型
	 */
	public String findLogType() {
		JSONArray array = new JSONArray();
		for(String str : InitData.logType) {
			JSONObject logJson = new JSONObject();
			logJson.put("value", str);
			logJson.put("text", str);
			array.add(logJson);
		}
		return array.toString();
	}
	/**
	 * 导出日志
	 * @throws IOException 
	 */
	public InputStream download(LogPage page) throws IOException {
		List<Log> logs = logDao.getLogOnPage(page, true);
		short[] rowHeight = {500, 320};
		int[] columnWidth = {30000, 2000, 5600};
		String[] head = {"内容", "类型", "时间"};
		int[] headType = {em.CENTER, em.CENTER, em.CENTER};
		List<String[]> cells = new ArrayList<String[]>();
		for(Log log : logs) {
			String[] column = {log.getContent(), log.getType(),
					StaticMethod.DateToString(log.getDate())};
			cells.add(column);
		}
		int[] cellType = {em.LEFT, em.CENTER, em.CENTER};
		return em.createExcel(rowHeight, columnWidth, head, headType, cells, cellType);
	}
//d
	public String remove(String json) {
		if(StaticMethod.StrSize(json) < 1) {
			return StaticMethod.inputError;
		}
		Set<String> logIds = new HashSet<String>();
		for(String s : json.split("-")) {
			try {
				Integer.parseInt(s);
				if(logIds.add(s)) {
					logDao.delete(s, "Log");
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.removeSucc;
	}
	
	
}

package com.baodian.service.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.baodian.service.file.FileManager;
import com.baodian.service.util.InitData;
import com.baodian.util.StaticMethod;
import com.baodian.util.file.NameComparator;
import com.baodian.util.file.SizeComparator;
import com.baodian.util.file.TypeComparator;

@Service("fileManager")
public class FileManager {

	private String rootPath;
	
	/**
	 * 获取用户的物理路径"/var/www/documents/"
	 */
	public String getRootPath(int userId) {
		if(rootPath == null) {
			rootPath = ServletActionContext.getServletContext().getRealPath("/");
			rootPath = rootPath.substring(0, rootPath.length() -1 );
			//47 is '/' 92 is '\'用来区分windows和linux
			int last = rootPath.lastIndexOf(92);
			if(last == -1) {
				last = rootPath.lastIndexOf(47);
			}
			rootPath = rootPath.substring(0, last);
		}
		return rootPath + InitData.upload[4] + userId + "/";
		//return ServletActionContext.getServletContext().getRealPath("/WEB-INF") +
			//	InitDataManager.upload[4] +  + userId + "/";
	}
	/**
	 * 获取用户的url路径"http://www.yoursite.com/documents/"
	 */
	public String getRootUrl(int userId) {
		return "/";
		//return ServletActionContext.getServletContext().getContextPath() +
			//	InitDataManager.upload[4] +  + userId + "/";
	}
	/**
	 * 查询文件夹信息
	 * @param userId 用户Id
	 * @param dirName 目录
	 * @param path 路径
	 * @param order 排序
	 * @return JSON格式保存的文件夹信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String filesMes(int userId, String dirName, String path, String order) {
		//根目录路径，可以指定绝对路径，比如 /var/www/documents/
		String rootPath = getRootPath(userId);
		//根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/documents/
		String rootUrl  = getRootUrl(userId);
		File currentPathFile = new File(rootPath);
		if (! currentPathFile.exists()) {
			currentPathFile.mkdirs();
		}
		if (dirName != null) {
			rootPath += dirName + "/";
			rootUrl += dirName + "/";
		}
		//根据path参数，设置各路径和URL
		path = path != null ? path : "";//-----------------------------
		String currentPath = rootPath + path;
		String currentUrl = rootUrl + path;
		String currentDirPath = path;
		String moveupDirPath = "";
		if (!"".equals(path)) {
			String str = currentDirPath.substring(0, currentDirPath.length() - 1);
			moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
		}
		//排序形式，name or size or type
		order = order != null ? order.toLowerCase() : "name";
		//不允许使用..移动到上一级目录
		if (path.indexOf("../") >= 0) {
			return StaticMethod.jsonMess(1, "不允许访问！");
		}
		//最后一个字符不是/
		if (!"".equals(path) && !path.endsWith("/")) {
			return StaticMethod.jsonMess(1, "参数有误！");
		}
		//目录不存在或不是目录
		currentPathFile = new File(currentPath);
		if(!currentPathFile.isDirectory()){
			return StaticMethod.jsonMess(1, "文件夹不存在，或者访问的是文件！");
		}
		//遍历目录取的文件信息
		List<Hashtable> fileList = new ArrayList<Hashtable>();
		if(currentPathFile.listFiles() != null) {
			String fileName, fileExt;
			for (File file : currentPathFile.listFiles()) {
				Hashtable<String, Object> hash = new Hashtable<String, Object>();
				fileName = file.getName();
				if(file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.list().length != 0));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if(file.isFile()){
					fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", InitData.imageSet.contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("fullpath", currentDirPath + fileName);
				hash.put("datetime", StaticMethod.LongToDate(file.lastModified()));
				fileList.add(hash);
			}
		}
		if ("size".equals(order)) {
			Collections.sort(fileList, new SizeComparator());
		} else if ("type".equals(order)) {
			Collections.sort(fileList, new TypeComparator());
		} else {
			Collections.sort(fileList, new NameComparator());
		}
		JSONObject result = new JSONObject();
		result.put("dirSize", getFileSize(currentPathFile));
		result.put("moveup_dir_path", moveupDirPath);
		result.put("current_dir_path", currentDirPath);
		result.put("current_url", currentUrl);
		result.put("total", fileList.size());
		result.put("rows", fileList);
		return result.toString();
	}
	/**
	 * 获取目录的大小
	 * @param dir
	 * @return
	 */
	public long getDirSize(String dir) {
		File folder = new File(dir);
		if (! folder.exists()) {
			return 0;
		}
		if(folder.isFile()) {
			return folder.length();
		}
		return getFileSize(folder);
	}
	/**
	 * 递归计算文件夹的大小
	 * @param folder
	 * @return
	 */
	private long getFileSize(File folder) {
		long foldersize = 0;
		File[] filelist = folder.listFiles();
		for (int i = 0; i < filelist.length; i++) {
			if (filelist[i].isDirectory()) {
				foldersize += getFileSize(filelist[i]);
			} else {
				foldersize += filelist[i].length();
			}
		}
		return foldersize;
	}
	/**
	 * 移动文件或目录
	 * @return 0-成功  1-非法名称  2-已存在 3-文件不存在
	 */
	public int changeDir(int userId, String oldPath, String newPath) {
		// 新文件
		File file = new File(getRootPath(userId) + newPath);
		// 原文件
		File oldFile = new File(getRootPath(userId) + oldPath);
		if (oldFile.exists()) {
			if(file.exists()) {
				return 2;
			} else {
				if(oldFile.renameTo(file)) {
					return 0;
				} else {
					return 1;
				}
			}
		} else {
			return 3;
		}
	}
}

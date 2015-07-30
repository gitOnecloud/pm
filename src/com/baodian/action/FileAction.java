package com.baodian.action;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;
import com.baodian.service.file.FileManager;
import com.baodian.service.role.SecuManager;
import com.baodian.service.user.UserManager;
import com.baodian.service.util.InitData;
import com.baodian.util.StaticMethod;

@SuppressWarnings("serial")
@Component("file")
@Scope("prototype")//必须注解为多态
public class FileAction extends ActionSupport implements ServletResponseAware,ServletRequestAware {
	private File imgFile;// 文件
	//private String imgFileContentType;// 文件的类型
	private String imgFileFileName;// 文件的名称
	private String dir;//保存目录
	private String path;
	private String order;
	private String json;
	private String id;//专门为异步加载目录使用
	private String[] paths;//专门为移动文件使用
	@Resource(name = "userManager")
	private UserManager userManager;
	@Resource(name = "fileManager")
	private FileManager fileManager;
	
	private HttpServletResponse servletResponse;
	private HttpServletRequest servletRequest;
	
	public String manager_rd() {
		return SUCCESS;
	}
	/**
	 * 获取JSESSIONID
	 */
	public String session_js() {
		json = "{\"status\":0,\"session\":\"" + servletRequest.getSession().getId() + "\"}";
		return "json";
	}
	/**
	 * 读取目录
	 */
	public String message_no() {
		int userId = SecuManager.currentId();
		if(userId == 0) {
			json = "{\"message\":\"请先登录！\"}";
			return "json";
		}
		json = fileManager.filesMes(userId, dir, path, order);
		return "json";
	}
	/**
	 * 创建目录
	 */
	public String mkdir() {
		int userId = SecuManager.currentId();
		if(userId == 0) {
			json = StaticMethod.loginError;
			return "json";
		}
		if(dir== null || dir.isEmpty()) {
			json = getError("未选择文件或目录！");
			return "json";
		}
		if(dir.indexOf("../") >= 0) {
			json = getError("非法目录名！");
			return "json";
		}
		// 获得项目路径  
		String realpath = fileManager.getRootPath(userId) + dir;
		File file = new File(realpath);
		if (!file.exists()) {
			if(file.mkdir()) json = "{\"status\":0}";
			else json = getError("请检查文件夹名称！");
		} else {
			json = getError("名称已存在！");
		}
		return "json";
	}
	/**
	 * 更改名称
	 */
	public String change_no() {
		int userId = SecuManager.currentId();
		if(userId == 0) {
			json = StaticMethod.loginError;
			return "json";
		}
		if(path==null || path.isEmpty()) {
			json = getError("未选择文件或目录！");
			return "json";
		}
		if(path.indexOf("../") >= 0) {
			json = getError("非法名称！X1");
			return "json";
		}
		if(dir== null || dir.isEmpty()) {
			json = getError("新名称为空！");
			return "json";
		}
		if(dir.indexOf("../") >= 0) {
			json = getError("非法名称！X2");
			return "json";
		}
		// 新文件名
		String realpath = fileManager.getRootPath(userId) + dir;
		File file = new File(realpath);
		// 原文件名
		realpath = fileManager.getRootPath(userId) + path;
		File oldFile = new File(realpath);
		if (oldFile.exists()) {
			if(file.exists()) {
				json = getError("文件或目录已存在！");
			} else {
				/*if(oldFile.isFile()) {
					if(! InitDataManager.extSet.contains(dir.substring(dir.lastIndexOf(".") + 1).toLowerCase())) {
						json = getError("文件扩展名只允许以下格式。 \\nimage: " + InitDataManager.upload[0] +
								"\\nflash: " + InitDataManager.upload[1] +
								"\\nmedia: " + InitDataManager.upload[2] +
								"\\nfile:  " + InitDataManager.upload[3]);
						return "json";
					}
				}*/
				if(oldFile.renameTo(file)) json = "{\"status\":0}";
				else json = getError("非法名称！X3");
			}
		} else {
			json = getError("原文件或目录不存在！");
		}
		return "json";
	}
	/**
	 * 移动文件
	 */
	public String move_no() {
		int userId = SecuManager.currentId();
		if(userId == 0) {
			json = StaticMethod.loginError;
			return "json";
		}
		if(dir== null) {
			json = getError("未选中新目录！");
			return "json";
		}
		if(dir.indexOf("../") >= 0) {
			json = getError("非法名称！X2");
			return "json";
		}
		if(path==null ) {
			json = getError("未选择文件或目录！X1");
			return "json";
		}
		if(path.indexOf("../") >= 0) {
			json = getError("非法名称！X1");
			return "json";
		}
		if(paths==null || paths.length==0) {
			json = getError("未选择文件或目录！X2");
			return "json";
		}
		int fail = 0;
		for(String str : paths) {
			if(str.indexOf("../")>=0 || fileManager.changeDir(userId, path+str, dir+str)!=0) {
				fail ++;
			}
		}
		if(fail == 0) {
			json = StaticMethod.jsonMess(0, "成功移动" + paths.length + "个文件！");
		} else {
			json = StaticMethod.jsonMess(1, "移动" + fail + "个文件失败，文件已存在或者名称不合法！");
		}
		return "json";
	}
	/**
	 * 删除文件
	 */
	public String remove() {
		int userId = SecuManager.currentId();
		if(userId == 0) {
			json = StaticMethod.loginError;
			return "json";
		}
		if(dir== null || dir.isEmpty()) {
			json = getError("未选择文件或目录！");
			return "json";
		}
		if(dir.indexOf("../") >= 0) {
			json = getError("非法文件或目录！");
			return "json";
		}
		// 获得项目路径  
		String realpath = fileManager.getRootPath(userId) + dir;
		File file = new File(realpath);
		if (file.exists()) {
			if(file.isDirectory())
				try {
					FileUtils.deleteDirectory(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			else
				file.delete();
		}
		json = "{\"status\":0}";
		return "json";
	}
	/**
	 * 上传文件
	 */
	public String upload_no() {
		int userId = SecuManager.currentId();
		if(userId == 0) {
			//flash上传时会重建session，通过参数将原来的取过来
			userId = userManager.getUIDBySessionID(json, StaticMethod.getRemoteAddr(servletRequest));
			if(userId == 0) {
				json = StaticMethod.loginError;
				//因为上传调用的是iframe，所以不能返回x-json数据流，否则返回的数据会当做文件下载
				return "jhtml";
			}
		}
		if (imgFile == null) {
			json = getError("未选择文件！");
			return "jhtml";
		}
		if(dir== null) {
			dir = "";
			/*json = getError("未选择上传目录！");
			return "jhtml";*/
		}
		//检查dir路径的合法性
		if(dir.indexOf("../") != -1) {
			json = getError("目录名非法！");
			return "jhtml";
		}
		// 获得项目路径  
		String savePath = fileManager.getRootPath(userId) + dir;
		//创建文件夹
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		//检查目录写权限
		if(!file.canWrite()){
			json = getError("上传目录没有写权限！");
			return "jhtml";
		}
		Long dirSize = fileManager.getDirSize(fileManager.getRootPath(userId));
		if (dirSize >= InitData.longPms[0]) {
			json = getError("储存空间超过" + InitData.upload[5] + "！");
			return "jhtml";
		}
		//检查文件大小
		InputStream is = null;
		try {
			is = new FileInputStream(imgFile);
			if(is.available() >= InitData.longPms[1]) {
				json = getError("上传文件超出" + InitData.upload[6] + "！");
				return "jhtml";
			}
		} catch (FileNotFoundException e) {
			json = getError("上传失败！X1");
			return "jhtml";
		} catch (IOException e) {
			json = getError("上传失败！X2");
			return "jhtml";
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		//检查扩展名
		/*String fileExt = imgFileFileName.substring(imgFileFileName.lastIndexOf(".") + 1).toLowerCase();
		if(dir.equals("image")) {
			if(! InitDataManager.imageSet.contains(fileExt)) {
				json = getError("上传的图片只允许是以下格式：\\n" + InitDataManager.upload[0]);
				return "jhtml";
			}
		} else if(! InitDataManager.extSet.contains(fileExt)) {
			json = getError("只允许上传以下格式文件。 \\nimage: " + InitDataManager.upload[0] +
					"\\nflash: " + InitDataManager.upload[1] +
					"\\nmedia: " + InitDataManager.upload[2] +
					"\\nfile:  " + InitDataManager.upload[3]);
			return "jhtml";
		}*/
		try {
			file = new File(file, imgFileFileName);
			if(file.exists()) {
				json = getError("文件已存在！");
				return "jhtml";
			} else {
				FileUtils.copyFile(imgFile, file);
			}
			json = "{\"status\":0,\"url\":\"" + "/" + imgFileFileName+"\"}";
			return "jhtml";
		} catch (IOException e) {
			json = getError("上传失败！X3");
			return "jhtml";
		}
	}
	private String getError(String message) {
		return "{\"status\":1,\"mess\":\"" + message + "\"}";
	}
//read
	/**
	 * 读取目录
	 */
	public String message_js() {
		int userId = SecuManager.currentId();
		if(userId == 0) {
			json = StaticMethod.loginError;
		} else {
			if(id!=null && !id.isEmpty()) {
				path = id + "/";
			}
			json = fileManager.filesMes(userId, dir, path, order);
		}
		return "json";
	}
	public String download_rd() {
		int userId = SecuManager.currentId();
		if(userId == 0) {
			json = "login";
			return "mess";
		}
		boolean isfname = true;
		if(path==null || path.isEmpty()) {
			isfname = false;
		} else {
			try {
				path = URLDecoder.decode(path, "UTF8");
			} catch (Exception e1) {
				isfname = false;
			}
		}
		if(! isfname) {
			json = "文件名错误！";
			return "mess";
		}
		//先检查path的合法性
		if(path.indexOf("../") != -1) {
			json = "非法文件路径！";
			return "mess";
		}
		File downFile = new File(fileManager.getRootPath(userId) + path);
		String fileName = downFile.getName();
		
		InputStream in = null;
		OutputStream os = null;
		try {
			in = new BufferedInputStream(new FileInputStream(downFile));
			servletResponse.reset();
			servletResponse.setCharacterEncoding("UTF-8");
			//用来解决下载时的中文问题
			if(servletRequest.getHeader("User-Agent").toUpperCase().indexOf("MSIE") >0) {
				//fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");	
				fileName = URLEncoder.encode(fileName, "UTF-8");
			} else {
				fileName = new String(fileName.getBytes("UTF8"), "ISO-8859-1");
			}
			servletResponse.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
			servletResponse.setHeader("Content-Length", "" + downFile.length());
			servletResponse.setContentType("application/octet-stream;charset=utf-8");
			
			os = new BufferedOutputStream(servletResponse.getOutputStream());
			byte[] buffer = new byte[16 * 1024];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				os.write(buffer, 0, len);
			}
			os.flush();
		} catch(IOException e) {
		} finally {
			try {
				if(in != null) {
					in.close();
				}
				if(os != null) {
					os.close();
				}
			} catch (IOException ee) {}
		}
		return null;
	}
//setget
	public File getImgFile() {
		return imgFile;
	}
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}
	public String getImgFileFileName() {
		return imgFileFileName;
	}
	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String[] getPaths() {
		return paths;
	}
	public void setPaths(String[] paths) {
		this.paths = paths;
	}
	//用户登录信息
	public int getLoginNums() {
		return this.userManager.loginUserNums();
	}
	public org.springframework.security.core.userdetails.User getUser() {
		return SecuManager.currentUser();
	}
}

package com.baodian.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.baodian.dao.ClientDao;
import com.baodian.model.Client;

public class ClientController extends MultiActionController {
	private ClientDao clientDao;
//c
	/**
	 * 添加或者更新
	 */
	public ModelAndView saveOrChange_js(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		Client client = new Client();
		String domain = request.getParameter("domain");
		if(domain.length()<4 || domain.length()>63) {
			mv.addObject("status", 1);
			mv.addObject("mess", "请正确输入domain（4~63个字符）！");
			return mv;
		}
		client.setDomain(domain);
		client.setName(request.getParameter("name"));
		client.setIp(request.getParameter("ip"));
		boolean isChange = false;
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			if(id > 0) {
				isChange = true;
				client.setId(id);
			} else {
				mv.addObject("status", 1);
				mv.addObject("mess", "ID输入有误！");
				return mv;
			}
		} catch(java.lang.NumberFormatException e) {}
		try {
			if(isChange) {
				clientDao.update(client);
				mv.addObject("mess", "更新成功！");
			} else {
				clientDao.add(client);
				mv.addObject("mess", "添加成功！");
			}
			mv.addObject("status", 0);
		} catch(Exception e) {
			mv.addObject("status", 1);
			if(isChange) {
				mv.addObject("mess", "更新失败！");
			} else {
				mv.addObject("mess", "添加失败！");
			}
			mv.addObject("detail", e.getMessage());
		}
		return mv;
	}
//r
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("myClientView");
	}
	public ModelAndView list_js(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("myJsonView");
		List<Client> clients = clientDao.getAll();
		JSONArray array = new JSONArray();
		for(Client cl : clients) {
			JSONObject clJSON = new JSONObject();
			clJSON.put("id", cl.getId());
			clJSON.put("name", cl.getName());
			clJSON.put("domain", cl.getDomain());
			clJSON.put("pubKey", cl.getPubkey());
			clJSON.put("priKey", cl.getPrikey());
			clJSON.put("modKey", cl.getModkey());
			clJSON.put("ip", cl.getIp());
			array.add(clJSON);
		}
		mv.addObject("json", array.toString());
		return mv;
	}
//u
	/**
	 * 更新RSA密钥
	 */
	public ModelAndView changeRsa_js(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		String id = request.getParameter("id");
		try {
			if(Integer.parseInt(id) > 0) {
				clientDao.updateRsa(request.getParameter("id"));
				mv.addObject("status", 0);
				mv.addObject("mess", "更新成功！");
				return mv;
			}
		} catch(java.lang.NumberFormatException e) {}
		mv.addObject("status", 1);
		mv.addObject("mess", "输入有误！");
		return mv;
	}
	
//d
	public ModelAndView delete_js(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		String id = request.getParameter("id");
		try {
			if(Integer.parseInt(id) > 0) {
				clientDao.delete(request.getParameter("id"));
				mv.addObject("status", 0);
				mv.addObject("mess", "删除成功！");
				return mv;
			}
		} catch(java.lang.NumberFormatException e) {}
		mv.addObject("status", 1);
		mv.addObject("mess", "输入有误！");
		return mv;
	}
//set get
	public ClientDao getClientDao() {
		return clientDao;
	}
	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

}

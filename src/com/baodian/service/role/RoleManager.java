package com.baodian.service.role;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.baodian.dao.role.RoleDao;
import com.baodian.dao.role.Role_AuthDao;
import com.baodian.dao.user.User_RoleDao;
import com.baodian.model.role.Role;
import com.baodian.model.role.Role_Auth;
import com.baodian.util.JSONValue;

@Service("roleManager")
public class RoleManager {
	@Resource(name="roleDao")
	private RoleDao roleDao;
	@Resource(name="roleAuthDao")
	private Role_AuthDao role_AuthDao;
	@Resource(name="userRoleDao")
	private User_RoleDao user_RoleDao;
//c
	/**
	 * 保存角色及其权限
	 * @param role
	 * @param ids 权限id组
	 */
	public void save(Role role, int[] ids) {
		roleDao.save(role);
		if(ids != null) {
			for(int aId : ids) {
				role_AuthDao.save(new Role_Auth(role.getId(), aId));
			}
		}
	}
//r
	/**
	 * 读取角色
	 * @return [{id,name,sort,open,pId,defaultSet}]
	 */
	public JSONArray findRoles() {
		JSONArray array = new JSONArray();
		List<Role> roles = roleDao.getRoles();
		if(roles.size() == 0) {
			return array;
		}
		for(Role r : roles) {
			JSONObject roleJson = new JSONObject();
			roleJson.put("id", r.getId());
			roleJson.put("name", r.getName());
			roleJson.put("sort", r.getSort());
			roleJson.put("open", true);
			roleJson.put("checked", r.getDefaultSet()==0?false:true);
			if(r.getParent() != null) {
				roleJson.put("pId", r.getParent().getId());
			}
			array.add(roleJson);
		}
		return array;
	}
	/**
	 * 根据id查找角色及它的权限
	 * @param id
	 * @return
	 */
	public Role findRA_ByR_id(int id) {
		Role role = roleDao.getRoleById(id);
		role.setName(JSONValue.escape(role.getName()));
		if(role != null)
			role.setRole_Authorities(role_AuthDao.getAIdsById(id));
		return role;
	}
//u
	/**
	 * 更改角色的权限
	 * @param id 角色
	 * @param ids 权限
	 */
	public void changeRA(int id, int[] ids) {
		role_AuthDao.deleteR_A_ByRid(id);
		if(ids != null)
			for(int aId : ids)
				role_AuthDao.save(new Role_Auth(id, aId));
	}
	/**
	 * 更改角色名称
	 * @param role
	 */
	public void changeName(Role role) {
		roleDao.updateName(role);
	}
	/**
	 * 更改角色父节点、顺序、默认设置
	 */
	public void changeSort(String[] roles) {
		for(String role : roles) {
			String[] ids = role.split("_");
			if(ids.length == 4) {
				try {
					roleDao.updateSort(Integer.parseInt(ids[0]),
						Integer.parseInt(ids[1]), Integer.parseInt(ids[2]),
						Integer.parseInt(ids[3]));
				} catch(NumberFormatException e) {}
			}
		}
	}
//d
	/**
	 * 删除角色 1.删除角色与用户的关系 2.删除角色与权限的关系 3.删除角色本身
	 * @param role
	 */
	public void remove(Role role) {
		user_RoleDao.deleteU_R_ByRid(role.getId());
		role_AuthDao.deleteR_A_ByRid(role.getId());
		role.setSort(0);
		role.setName("");
		roleDao.delete(role);
	}
}

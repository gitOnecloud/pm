package com.baodian.dao.util;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.baodian.util.page.Page;

@Repository("utilDao")
public abstract class UtilDao{

	@Resource(name="sessionFactory")
	public SessionFactory sf;
//c
	/**
	 * 添加对象
	 */
	public void save(Object obj) {
		sf.getCurrentSession().save(obj);
	}

//r
	/**
	 * 检查此对象的id是否存在
	 * @return true存在	false不存在
	 */
	@SuppressWarnings("unchecked")
	public boolean chkExit(int id, String obj) {
		List<Integer> objs = sf.getCurrentSession().createQuery("select obj.id from " + obj + " obj where obj.id=" + id).list();
		return objs.size()==0? false: true;
	}
	/*---begin---*/
	/**
	 * 根据分页查找对象，无参数
	 */
	public <T> List<T> findByPage(String countSql, String selectSql, Page page) {
		return findByPage(countSql, selectSql, page, null);
	}
	/**
	 * 根据分页查找对象，参数保存在list中
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByPage(String countSql, String selectSql,
			Page page, List<String> params) {
		Query query = sf.getCurrentSession().createQuery(countSql);
		if (params != null) {
			for (int i=0; i<params.size(); i++) {
				query.setParameter(i, params.get(i));
			}
		}
		page.countPage(((Long) query.list().get(0)).intValue());
		if(page.getCountNums() == 0) {
			return Collections.emptyList();
		}
		query = sf.getCurrentSession().createQuery(selectSql)
			.setFirstResult(page.getFirstNum())
			.setMaxResults(page.getNum());
		if (params != null) {
			for (int i=0; i<params.size(); i++) {
				query.setParameter(i, params.get(i));
			}
		}
		return query.list();
	}
	/*---end---*/
	/*---begin---*/
	/**
	 * 按条件获取部分对象，无参数
	 * @param num 获取的数量
	 */
	public <T> List<T> findByPart(String sql, int num) {
		return findByPart(sql, num, (Object[]) null);
	}
	/**
	 * 按条件获取部分对象，带一个参数
	 * @param num 获取的数量
	 */
	public <T> List<T> findByPart(String sql, int num, Object value) {
		return findByPart(sql, num, new Object[] {value});
	}
	/**
	 * 按条件获取部分对象，带多个参数
	 * @param num 获取的数量
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByPart(String sql, int num, Object... values) {
		Query query = sf.getCurrentSession().createQuery(sql).setMaxResults(num);
		if (values != null) {
			for (int i=0; i<values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query.list();
	}
	/*---end---*/
	/*---begin---*/
	/**
	 * 获取全部对象，无参数
	 */
	public <T> List<T> find(String sql) {
		return find(sql, (Object[]) null);
	}
	/**
	 * 获取全部对象，带一个参数
	 */
	public <T> List<T> find(String sql, Object value) {
		return find(sql, new Object[] {value});
	}
	/**
	 * 获取全部对象，带多个参数
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> find(String sql, Object... values) {
		Query query = sf.getCurrentSession().createQuery(sql);
		if (values != null) {
			for (int i=0; i<values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query.list();
	}
	/**
	 * 获取全部对象，带List参数
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> find(String sql, List<String> params) {
		Query query = sf.getCurrentSession().createQuery(sql);
		if (params != null) {
			for (int i=0; i<params.size(); i++) {
				query.setParameter(i, params.get(i));
			}
		}
		return query.list();
	}
	/*---end---*/
	/**
	 * 统计数量
	 */
	public int countNum(String sql) {
		return ((Long) find(sql).get(0)).intValue();
	}
//u
	public void update(Object obj) {
		sf.getCurrentSession().update(obj);
	}
	/*---begin---*/

	/**
	 * 更新或者删除对象，无参数
	 */
	public int bulkUpdate(String queryString) {
		return bulkUpdate(queryString, (Object[]) null);
	}
	/**
	 * 更新或者删除对象，带一个参数
	 */
	public int bulkUpdate(String queryString, Object value){
		return bulkUpdate(queryString, new Object[] {value});
	}
	/**
	 * 更新或者删除对象，带多个参数
	 * @return the number of instances updated/deleted
	 */
	public int bulkUpdate(String queryString, Object... values) {
			Query queryObject = sf.getCurrentSession().createQuery(queryString);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					queryObject.setParameter(i, values[i]);
				}
			}
			return queryObject.executeUpdate();
	}
	/**
	 * 更新或者删除对象，带多个参数
	 * @return the number of instances updated/deleted
	 */
	public int bulkUpdate(String queryString, List<String> values) {
		Query queryObject = sf.getCurrentSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				queryObject.setParameter(i, values.get(i));
			}
		}
		return queryObject.executeUpdate();
	}
	/*---end---*/
//d
	/**
	 * 根据对象id删除此对象
	 */
	public void delete(String id, String obj) {
		bulkUpdate("delete from " + obj + " obj where obj.id=" + id);
	}
	/**
	 * 根据对象id删除此对象
	 */
	public void delete(int id, String obj) {
		this.delete(String.valueOf(id) , obj);
	}
	public void delete(Object obj) {
		sf.getCurrentSession().delete(obj);
	}

}

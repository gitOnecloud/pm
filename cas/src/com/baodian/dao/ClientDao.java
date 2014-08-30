package com.baodian.dao;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

import com.baodian.model.Client;
import com.baodian.util.rsa.RSACoder;

/**
 * 保存客户端的rsa和ip
 * @author LF_eng 2014-3-21 11:08:35
 */
public class ClientDao extends NamedParameterJdbcDaoSupport {
//c
	public void add(Client client) {
		KeyPair keyPair = RSACoder.initKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		super.getJdbcTemplate().update(
				"insert into client(name, domain, ip, pubkey, prikey, modkey) values(?, ?, ?, ?, ?, ?)",
					client.getName(),
					client.getDomain(),
					client.getIp(),
					publicKey.getPublicExponent().toString(16),
					privateKey.getPrivateExponent().toString(16),
					publicKey.getModulus().toString(16));
	}
//r
	/**
	 * 获取客户端信息
	 * @return
	 */
	public List<Client> getAll() {
		return super.getJdbcTemplate().query(
				"select * from client",
				ParameterizedBeanPropertyRowMapper.newInstance(Client.class));
		
		/*super.getJdbcTemplate().query(
				"select id,domain from client where domain like (?)",
				new Object[] { "mis%" },
				ParameterizedBeanPropertyRowMapper.newInstance(Client.class));*/
		/*super.getJdbcTemplate().query(
				"select id,domain from client where domain like (?)",
				new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setString(1, "mis%");
					}
				},
				ParameterizedBeanPropertyRowMapper.newInstance(Client.class));*/
		/*Map<String, String> paramMap = new HashMap<String, String>();
		return getNamedParameterJdbcTemplate().queryForList(
				"select * from client", paramMap, Client.class);*/
	}
	/**
	 * 获取Rsa
	 */
	public Client getRsa(String ip, String domain) {
		try{
			return super.getJdbcTemplate().queryForObject(
					"select * from client where domain=? and ip like('%#" + ip + "#%')",
					new Object[] { domain},
					ParameterizedBeanPropertyRowMapper.newInstance(Client.class));
		} catch(EmptyResultDataAccessException e) {
			return null;
		}
	}
//u
	public void update(Client client) {
		super.getJdbcTemplate().update(
				"update client set name=?, domain=?, ip=? where id=" +client.getId(),
				client.getName(), client.getDomain(), client.getIp());
	}
	/**
	 * 更新RSA
	 */
	public void updateRsa(String id) {
		KeyPair keyPair = RSACoder.initKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		super.getJdbcTemplate().update(
				"update client set pubkey=?, prikey=?, modkey=? where id=" + id,
				publicKey.getPublicExponent().toString(16),
				privateKey.getPrivateExponent().toString(16),
				publicKey.getModulus().toString(16));
	}
//d
	public void delete(String id) {
		super.getJdbcTemplate().update("delete from client where id=" + id);
	}
}

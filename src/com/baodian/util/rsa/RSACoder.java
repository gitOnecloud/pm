package com.baodian.util.rsa;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

/**
 * RSA安全编码组件
 * @author LF_eng
 * @create 2014-02-11 22:24
 * @update 
 */
public class RSACoder {
	/**
	 * 用私钥对信息生成数字签名
	 */
	public static String sign(String data, RSAPrivateKey privateKey)
			throws Exception {
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initSign(privateKey);
		signature.update(data.getBytes("UTF-8"));	
		return new BigInteger(1, signature.sign()).toString(16);
	}
	/**
	 * 用公钥校验数字签名
	 */
	public static boolean verify(String data, RSAPublicKey publicKey,
			String sign) throws Exception {
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initVerify(publicKey);
		signature.update(data.getBytes("UTF-8"));
		return signature.verify(new BigInteger(sign, 16).toByteArray());
	}
	/**
	 * 加密 用公钥加密
	 * @param data 普通字符串
	 * @return 16进制表示的字符串
	 */
	public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
			throws Exception {
		// Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm(), new
		// BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		//全部转换成正数
		return new BigInteger(1, cipher.doFinal(data.getBytes("UTF-8"))).toString(16);
	}
	/**
	 * 加密 用公钥加密
	 * @param data 普通字符串
	 * @return 16进制表示的字符串
	 */
	public static String encryptByPublicKey(String data, String pub, String mod)
			throws Exception {
		BigInteger pubKey = new BigInteger(pub, 16);
		//BigInteger priKey = new BigInteger(pri, 16);
		BigInteger modKey = new BigInteger(mod, 16);
		RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(modKey, pubKey);
		//RSAPrivateKeySpec privateSpec = new RSAPrivateKeySpec(modKey, priKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicSpec);
		//RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateSpec);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		//全部转换成正数
		return new BigInteger(1, cipher.doFinal(data.getBytes("UTF-8"))).toString(16);
	}
	/**
	 * 加密 用私钥加密
	 * @param data 普通字符串
	 * @return 16进制表示的字符串
	 */
	public static String encryptByPrivateKey(String data,
			RSAPrivateKey privateKey) throws Exception {
		// 对数据加密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return new BigInteger(1, cipher.doFinal(data.getBytes("UTF-8"))).toString(16);
	}
	/**
	 * 解密 用公钥解密
	 * @param data 16进制表示的字符串
	 * @return 普通字符串
	 */
	public static String decryptByPublicKey(String data, RSAPublicKey publicKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		byte[] bt = new BigInteger(data, 16).toByteArray();
		if(bt.length == 129) {
			return new String(cipher.doFinal(bt, 1, 128), "UTF-8");
		}
		return new String(cipher.doFinal(bt), "UTF-8");
	}
	/**
	 * 解密 用私钥解密
	 * @param data 16进制表示的字符串
	 * @return 普通字符串
	 */
	public static String decryptByPrivateKey(String data,
			RSAPrivateKey privateKey) throws Exception {
		// Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm(), new
		// BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] bt = new BigInteger(data, 16).toByteArray();
		//将第一位的0去掉
		if(bt.length == 129) {
			return new String(cipher.doFinal(bt, 1, 128), "UTF-8");
		}
		return new String(cipher.doFinal(bt), "UTF-8");
	}
	/**
	 * 解密 用私钥解密
	 * @param data 16进制表示的字符串
	 * @return 普通字符串
	 */
	public static String decryptByPrivateKey(String data, String pri, String mod)
			throws Exception {
		//BigInteger pubKey = new BigInteger(pub, 16);
		BigInteger priKey = new BigInteger(pri, 16);
		BigInteger modKey = new BigInteger(mod, 16);
		//RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(modKey, pubKey);
		RSAPrivateKeySpec privateSpec = new RSAPrivateKeySpec(modKey, priKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		//RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicSpec);
		RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateSpec);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] bt = new BigInteger(data, 16).toByteArray();
		//将第一位的0去掉
		if(bt.length == 129) {
			return new String(cipher.doFinal(bt, 1, 128), "UTF-8");
		}
		return new String(cipher.doFinal(bt), "UTF-8");
	}
	/**
	 * 初始化密钥
	 */
	public static KeyPair initKey() {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
			keyPairGen.initialize(1024);
			return keyPairGen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 初始化密钥
	 */
	public static KeyPair initKey(String pub, String pri, String mod) {
		BigInteger pubKey = new BigInteger(pub, 16);
		BigInteger priKey = new BigInteger(pri, 16);
		BigInteger modKey = new BigInteger(mod, 16);
		RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(modKey, pubKey);
		RSAPrivateKeySpec privateSpec = new RSAPrivateKeySpec(modKey, priKey);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicSpec);
			RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateSpec);
			return new KeyPair(publicKey, privateKey);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
			return null;
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			return null;
		}
	}
}

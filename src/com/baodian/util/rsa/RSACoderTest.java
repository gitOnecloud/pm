package com.baodian.util.rsa;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;

public class RSACoderTest {
	private static RSAPublicKey publicKey;
	private static RSAPrivateKey privateKey;

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyPair keyPair = RSACoder.initKey();
		publicKey = (RSAPublicKey) keyPair.getPublic();
		privateKey = (RSAPrivateKey) keyPair.getPrivate();

		String pubString = "10001";
		String priString = "5dd3dedbf17fdc06a6e366d8302b70d51bb67b4141797cceb00971e0aeb21a3572b864de557ca3e50350afc61f81be9bccf82d1379ed60f0a8f6f59b69507b87aefe1a90b47199b24ad064357aada1dc8fb0edc6b0a9cb1583184f3d35333368e5953468c28475d7539bee56d365772a780c8e5f2de476130ca6ec136e68bc1";
		String modString = "8c296f01c6a6a33c54458ab601243b02208ecabc3321c98624bd29e2b0012c1c75284894f93fa275dd951e3221a80aa106474b5173f612d90842a70a13acef874227bdb97ace7e57f2edd0271245a8494a4f7ab00539892c09e0267bfa310aa5649dfe9c9b54eef0a0bc24a2b1957ee0fb393a609c381c259aaceea9eb1625a5";
		keyPair = RSACoder.initKey(pubString, priString, modString);
		publicKey = (RSAPublicKey) keyPair.getPublic();
		privateKey = (RSAPrivateKey) keyPair.getPrivate();
		
		System.out.println("pub\"" + publicKey.getPublicExponent().toString(16) + "\",\n" +
				"pri\"" + privateKey.getPrivateExponent().toString(16) + "\",\n" +
				"mod\"" + publicKey.getModulus().toString(16) + "\"");
		//
		System.out.println("\n*****公钥加密——私钥解密*****");
		String data = "12345中文6789";
		System.out.println("加密前: " + data);
		String encodedData = null;
		String decodedData = null;
		String inputEn = "";
		try {
			encodedData = RSACoder.encryptByPublicKey(data, publicKey);
			System.out.println("加密后: \n" + encodedData);
			decodedData = RSACoder.decryptByPrivateKey(encodedData, privateKey);
			System.out.println("解密后: " + decodedData);
			
			inputEn = "3a9510d16e8f4896c9e5810f129a007df6497b5597d00d745f9b08fd36018b65cc3bb9013c30e44d6f5749e9ef7fbc58ac38f63fe8923b83d4c30c766454c9ae133bfa1c8f458b582da988235d8a1ae94c8c0f36d04ce6da8152902f442b7fd843ceba564d36b6b5e6dc35b0a46ee27bff152d3f32f405e5f43aedcf51cc6144";
			System.out.println("解密后: " + RSACoder.decryptByPrivateKey(inputEn, privateKey));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("解密失败");
		}
		System.out.println("\n*****字符串公钥加密——私钥解密*****");
		try {
			System.out.println("file.encoding:" + new Properties(System.getProperties()).getProperty("file.encoding"));
			
			encodedData = RSACoder.encryptByPublicKey(data, pubString, modString);
			System.out.println("加密后: \n" + encodedData);

			decodedData = RSACoder.decryptByPrivateKey(encodedData, priString, modString);
			System.out.println("解密后: " + decodedData);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//
		System.out.println("\n*****第二种公钥加密——私钥解密*****");
		data = "12345中文6789";
		System.out.println("加密前: " + data);
		inputEn = "";
		try {
			encodedData = RSACoder.encryptByPublicKey(data, pubString, modString);
			System.out.println("加密后: \n" + encodedData);
			decodedData = RSACoder.decryptByPrivateKey(encodedData, priString, modString);
			System.out.println("解密后: " + decodedData);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("解密失败");
		}
		
		//
		System.out.println("\n*****私钥加密——公钥解密*****");
		data = "345中文2452535";
		System.out.println("加密前: " + data);
		try {
			encodedData = RSACoder.encryptByPrivateKey(data, privateKey);
			System.out.println("加密后: \n" + encodedData);
			decodedData = RSACoder.decryptByPublicKey(encodedData, publicKey);
			System.out.println("解密后: " + decodedData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("\n*****私钥签名——公钥验证签名*****");
		// 产生签名
		String sign = null;
		try {
			sign = RSACoder.sign(encodedData, privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("签名:\r" + sign);

		// 验证签名
		boolean status = false;
		try {
			status = RSACoder.verify(encodedData, publicKey, sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("状态:" + status);
		
	}
}

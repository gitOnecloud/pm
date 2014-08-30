package com.baodian.util.rsa;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

public class RSACoderTest {
	private static RSAPublicKey publicKey;
	private static RSAPrivateKey privateKey;

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyPair keyPair = RSACoder.initKey();
		publicKey = (RSAPublicKey) keyPair.getPublic();
		privateKey = (RSAPrivateKey) keyPair.getPrivate();

		String pubString = "10001";
		String priString = "4b922003a202bd922e4fd8604c95453c884d69f90db9def204d32f2702a050fb7426300f449076817a7f99780bd24a0bc8169326cdb1e6c0b30f8b3b37ece27cedf6e624fcd57d00e4d61e9cb4dd4637a314b3b44168c1a386b7cac7937a9dfdb2c2ceb7a871343ba1f2bd944c06bf953038e648c274231e611f59263bcc88b1";
		String modString = "9b3defb987d4aa50cdd2f9179b73a733853e53bedf46b52c1b9d0302bc095e885fdb0ec4b6996f041bc5d6e585a5d66f4013bbd61eacb3fd8b05faffba7a63ec6a58750f8c3c8bb620dbc4aa2a1eb60e2512dd4a5baa9ac222e703370a583215fa84b6e8f7dd2f699b2d22e7999aa2cd296de7362aaefa4b76a0a5a5ad573729";
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
			
			inputEn = "4995d74905da7ed5601d67969eadc119aa662bb5a88af0530c0d80ef08dd36ed7d58c97772d0514f829a3a4629406303a3f18b38377b251198d6e053ce8f13a0f3abe11290013a22142fd56ec23cf8f0fe1d7f8f53273e79cb8c7e5313055f964fe4b2170fe3c1ba27e296a4493dfd37b533e085c891bd870b01d0a5bd812df3";
			System.out.println("解密后: " + RSACoder.decryptByPrivateKey(inputEn, privateKey));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("解密失败");
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
		data = "3452452535";
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

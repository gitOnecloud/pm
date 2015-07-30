package com.baodian.util.test;

import org.springframework.security.crypto.password.StandardPasswordEncoder;

import com.baodian.util.StaticMethod;


public class TestPassword {
	public static void main(String[] args) {
		StandardPasswordEncoder passEncode = new StandardPasswordEncoder();
		String encode = passEncode.encode("123456");
		System.out.println("encode: " + encode);
		String password = "96025b5d0d394f2fed1989163b845d5bee7253d8d23a51d46d34c5487a0184499cc0740c2a9d7a28";
		String code = StaticMethod.encodeSha1("admin1");
		System.out.println("code: " + code);
		//code=142a3a94781417765b9a3f3b63c3c37d2ffd4497
		System.out.println("equal: " + passEncode.matches(code, password));
		
	}
}

package com.hzsoft.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
	public static String encryptMD5(String strInput) {
		String strOutput = new String("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(strInput.getBytes());
			byte[] b = md.digest();
			for (int i = 0; i < b.length; ++i) {
				char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
						'9', 'A', 'B', 'C', 'D', 'E', 'F' };
				char[] ob = new char[2];
				ob[0] = digit[(b[i] >>> 4 & 0xF)];
				ob[1] = digit[(b[i] & 0xF)];
				strOutput = strOutput + new String(ob);
			}
		} catch (NoSuchAlgorithmException nsae) {
			System.out.println("没有这种算法");
		}

		return strOutput;
	}

	public static void main(String[] args) {
		System.out.println(encryptMD5("091372be283d44c8494ba5baea72c966"));
	}
}
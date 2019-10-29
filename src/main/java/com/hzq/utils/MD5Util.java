package com.hzq.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.util.Random;

/**
 * @Auther: blue
 * @Date: 2019/10/3
 * @Description: 加密工具类
 * @version: 1.0
 */
public class MD5Util {
	 
	/**
	 * 加盐MD5
	 */
		public static String generate(String password) {
			Random r = new Random();
	 		StringBuilder sb = new StringBuilder(16);
	 		sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
	 		int len = sb.length();
	 		if (len < 16) {
	 			for (int i = 0; i < 16 - len; i++) {
	 				sb.append("0");
	 			}
	 		}
	 		String salt = sb.toString();
	 		password = md5Hex(password + salt);
			char[] cs = new char[48];
	 		if (password != null) {
				for (int i = 0; i < 48; i += 3) {
					cs[i] = password.charAt(i / 3 * 2);
					char c = salt.charAt(i / 3);
					cs[i + 1] = c;
					cs[i + 2] = password.charAt(i / 3 * 2 + 1);
				}
			}
			return new String(cs);
		}
 
		/**
		 * 校验加盐后是否和原文一致
		 */
		public static boolean verify(String password, String md5) {
	 		char[] cs1 = new char[32];
			char[] cs2 = new char[16];
			for (int i = 0; i < 48; i += 3) {
				cs1[i / 3 * 2] = md5.charAt(i);
				cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
				cs2[i / 3] = md5.charAt(i + 1);
			}
			String salt = new String(cs2);
			return (new String(cs1)).equals(md5Hex(password + salt));
		}
 
		/**
		 * 获取十六进制字符串形式的MD5摘要
		 */
		private static String md5Hex(String src) {
			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				byte[] bs = md5.digest(src.getBytes());
				return new String(new Hex().encode(bs));
			} catch (Exception e) {
				return null;
			}
		}

	// 测试主函数  265c50a54658e13392d4af9403563fc57b7e936c3dc46998
	public static void main(String[] args) {
		// 原文
		String plaintext = "111111";
		// 获取加盐后的MD5值
		String ciphertext = "265c50a54658e13392d4af9403563fc57b7e936c3dc46998";
				//MD5Util.generate(plaintext);
		//System.out.println("加盐后MD5：" + ciphertext);
		System.out.println("是否是同一字符串:" + MD5Util.verify(plaintext, ciphertext));

	}
	 
	 
}

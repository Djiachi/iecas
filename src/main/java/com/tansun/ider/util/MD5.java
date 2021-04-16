package com.tansun.ider.util;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5 {
	private static Logger logger = LoggerFactory.getLogger(MD5.class);
	private static String md5Str = "ts9o8i7u6y5t4r3e2w1q0pl1k3j48877";

	public static String ecode(String enStr) {
		String originstr = enStr + md5Str;
		String result = "";
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		if (originstr != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] source = originstr.getBytes("UTF-8");
				md.update(source);
				byte[] tmp = md.digest();
				char[] str = new char[32];
				for (int i = 0, j = 0; i < 16; i++) {
					byte b = tmp[i];
					str[j++] = hexDigits[b >>> 4 & 0xf];
					str[j++] = hexDigits[b & 0xf];
				}
				result = new String(str);// 结果转换成字符串用于返回
			} catch (Exception e) {
				logger.error("Error:", e);
			}
		}
		return result;
	}

	public static void main(String[] args) {
		String as = MD5.ecode("admin127.0.0.1Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/600.7.12 (KHTML, like Gecko) Version/8.0.7 Safari/600.7.12");

		System.out.println(as);

	}

}

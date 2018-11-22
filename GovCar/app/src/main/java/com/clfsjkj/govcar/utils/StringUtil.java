package com.clfsjkj.govcar.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 字符串工具类
 * 
 * @author LionLiu
 */
public class StringUtil {

	public static String emptyIfNull(String s) {
		return s == null ? "" : s;
	}

	/**
	 * 判断字符串是否为空，或者空串
	 */
	public static boolean isEmpty(String s) {
		return null == s || "".equals(s);

	}

	/**
	 * MD5加密
	 */
	public static String getMD5Str(String str) {
		if (str == null) {
			return null;
		}
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			Log.d("Exception", e.toString());
		}
		byte[] byteArray = messageDigest.digest();
		StringBuilder md5StrBuff = new StringBuilder();
		for (byte aByteArray : byteArray) {
			if (Integer.toHexString(0xFF & aByteArray).length() == 1) {
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & aByteArray));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & aByteArray));
			}
		}
		// 16位加密，从第9位到25位
		return md5StrBuff.substring(8, 24).toUpperCase();
	}

}

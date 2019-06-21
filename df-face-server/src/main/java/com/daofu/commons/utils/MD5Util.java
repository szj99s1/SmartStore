package com.daofu.commons.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util

{
	public static String encryption(String plainText) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			re_md5 = buf.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5;
	}
	
	

	public static void main(String[] args) {
		System.out.println(new MD5Util().encryption("admine10adc3949ba59abbe56e057f20f883e"));
		System.out.println(new MD5Util().encryption("844025"));
	}
}

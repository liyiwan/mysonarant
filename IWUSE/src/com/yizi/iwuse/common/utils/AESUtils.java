package com.yizi.iwuse.common.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/***
 * AES 加密算法
 * 
 * @author zhangxiying
 *
 */
public class AESUtils {
	private static final String TAG = "AESUtils";

	// 配置文件中登录密码是否为明文
	public static boolean isClearText = false;

	/**
	 * 对字符串进行加密
	 * 
	 * @param deskey
	 *            对称密钥
	 * @param encryptStr
	 *            待加密字符串
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String deskey, String encryptStr) {
		byte[] result = null;
		try {
			byte[] rawKey = getRawKey(deskey.getBytes());
			result = encrypt(rawKey, encryptStr.getBytes());
		} catch (Exception e) {
			ILog.e(TAG, "加密失败：" + e);
		}
		return toHex(result);
	}

	/**
	 * 对字符串进行解密
	 * 
	 * @param deskey
	 *            对称密钥
	 * @param encryptedStr
	 *            已加密字符串
	 * @param keyName
	 *            待解密字段名称
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String deskey, String encryptedStr) {
		isClearText = false;
		byte[] result = null;
		try {
			byte[] rawKey = getRawKey(deskey.getBytes());
			byte[] enc = toByte(encryptedStr);
			result = decrypt(rawKey, enc);
		} catch (Exception e) {
			isClearText = true;
			ILog.e(TAG, "解密失败：" + e);
			return encryptedStr;
		}

		return new String(result);
	}

	/**
	 * 生成密钥
	 * 
	 * @param deskey
	 * @return
	 * @throws Exception
	 */
	private static byte[] getRawKey(byte[] deskey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(deskey);
		kgen.init(128, sr); // 生成密钥长度为128位bits （192 and 256 bits may not be
							// available）
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	/**
	 * 真正加密的方法
	 * 
	 * @param deskeyByte
	 * @param encryptByte
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] deskeyByte, byte[] encryptByte)
			throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(deskeyByte, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(encryptByte);
		return encrypted;
	}

	/**
	 * 真正解密的方法
	 * 
	 * @param deskeyByte
	 * @param encryptedByte
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] deskeyByte, byte[] encryptedByte)
			throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(deskeyByte, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encryptedByte);
		return decrypted;
	}

	/**
	 * 将加密后的字符串转成16进制byte数据
	 * 
	 * @param encryptedStr
	 * @return
	 */
	public static byte[] toByte(String encryptedStr) {
		int len = encryptedStr.length() / 2;

		byte[] result = new byte[len];

		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(
					encryptedStr.substring(2 * i, 2 * i + 2), 16).byteValue();

		return result;
	}

	// 声明16进制字符串
	private final static String HEX = "0123456789ABCDEF";

	/**
	 * 把byte数据转成16进制数据
	 * 
	 * @param buf
	 * @return
	 */
	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			result.append(HEX.charAt((buf[i] >> 4) & 0x0f)).append(
					HEX.charAt(buf[i] & 0x0f));
		}

		return result.toString();
	}

}

package com.yizi.iwuse.framework.service.impl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * https连接时证书的验证。
 * 
 * 因为没有证书，默认信任所有证书。
 * 
 */
public class MyTrustManager implements X509TrustManager {

	/**
	 * 验证客户端证书
	 * 
	 * @param chain
	 *            证书
	 * @param authType
	 *            演示类型
	 * @exception CertificateException
	 *                可能出现的异常
	 */
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// 客户端不需要校验

	}

	/**
	 * 验证服务器端的证书
	 * 
	 * @param chain
	 *            证书
	 * @param authType
	 *            演示类型
	 * @exception CertificateException
	 *                可能出现的异常
	 */
	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// 信息服务器端的证书

	}

	/**
	 * 所有信任的证书
	 * 
	 * @return 所有信任的证书
	 */
	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// 返回所有信任的证书
		return null;
	}
}
package com.yizi.iwuse.framework.service.impl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * 服务器名称验证。
 * 
 */
public class MyHostnameVerifier implements HostnameVerifier {

	/**
	 * 验证服务器名称
	 * 
	 * 返回true，表示验证一直成功
	 * 
	 * @param hostname
	 *            String
	 * @param session
	 *            SSLSession
	 * @return 是否验证成功
	 */
	@Override
	public boolean verify(String hostname, SSLSession session) {
		// 默认信任服务器
		return true;
	}

}

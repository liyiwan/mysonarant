package com.yizi.iwuse.framework.model;

/**
 * 网络连接对象
 * 
 */
public abstract class ConnInfo {

	/** 无效的端口 */
	public static final int INVALID_PORT = -1;

	/** 连接时的用户名 */
	public String userName = "";

	/** 连接时的密码 */
	public String userPwd = "";

	/** 连接对端的ip地址 */
	public String ipAddr = "";

	/** 连接对端的端口 */
	public int ipPort = INVALID_PORT;

	/** 身份验证是否通过 */
	public boolean certificationFlag = false;

	public ConnInfo() {

	}

	/**
	 * @param user
	 *            用户名
	 * @param pwd
	 *            密码
	 * @param addr
	 *            登录地址
	 */
	public ConnInfo(String fuser, String fpwd, String addr) {
		userName = fuser;
		userPwd = fpwd;
		ipAddr = addr;
	}

	/**
	 * @param user
	 *            用户名
	 * @param pwd
	 *            密码
	 * @param addr
	 *            登录地址
	 * @param fport
	 *            登录端口
	 */
	public ConnInfo(String fuser, String fpwd, String addr, int fport) {
		userName = fuser;
		userPwd = fpwd;
		ipAddr = addr;
		ipPort = fport;
	}

	/**
	 * 清除数据
	 */
	public void clearData() {
		userName = "";
		userPwd = "";
		ipAddr = "";
		ipPort = INVALID_PORT;
		certificationFlag = false;
	}
}

package com.yizi.iwuse.framework.model;

import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.common.utils.ILog;

/***
 * 通过HTTP连接到远程服务器信息
 * 
 * @author zhangxiying
 *
 */
public class HttpConnInfo extends ConnInfo {
	/** The logging tag used by this class with ILog. */
	protected static final String TAG = "HttpConnInfo";

	/** http 的协议常量 */
	public static final String CGI_CMD_FLAG = "action.cgi?ActionID=";

	/** http 的协议常量 */
	public static final String HTTP_PROL = "HTTP";

	/** https 的协议常量 */
	public static final String HTTPS_PROL = "HTTPS";

	/** http 的默认登录端口 */
	public static final int HTTP_PORT_DFT = 80;

	/** https 的默认登录端口 */
	public static final int HTTPS_PORT_DFT = 443;

	/** http连接时所采用协议，http或者https */
	public String protocol = HTTPS_PROL;

	/** 配置文件中保存的session 的key */
	public static final String SESSION_KEY = "SessionId";

	/** http连接的URL公共部分 */
	private String cmdUrl = "";

	/** http获取文件的前缀 */
	private String fileUrlPre = "";

	/** 一个http客户端连接到web主机的session id */
	private String sessionId = "";

	public HttpConnInfo() {
	}

	public HttpConnInfo(String user, String pwd, String addr, String prol) {
		super(user, pwd, addr);
		protocol = prol;
		ipPort = (prol.equalsIgnoreCase(HTTP_PROL) ? HTTP_PORT_DFT
				: HTTPS_PORT_DFT);
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @return the cmdUrl
	 */
	public String getCmdUrl() {
		return cmdUrl;
	}

	/**
	 * @param cmdUrl
	 *            the cmdUrl to set
	 */
	public void setCmdUrl(String cmdUrl) {
		this.cmdUrl = cmdUrl;
	}

	/**
	 * @return the fileUrlPre
	 */
	public String getFileUrlPre() {
		return fileUrlPre;
	}

	/**
	 * @param fileUrlPre
	 *            the fileUrlPre to set
	 */
	public void setFileUrlPre(String fileUrlPre) {
		this.fileUrlPre = fileUrlPre;
	}

	/**
	 * @return the ipPort
	 */
	public int getIpPort() {
		ILog.d(TAG, "get port,protocol:" + protocol);
		// 每次都根据协议来返回，目前端口不能自定义，跟协议绑定
		return (protocol.equalsIgnoreCase(HTTP_PROL)) ? HTTP_PORT_DFT
				: HTTPS_PORT_DFT;
	}

	/**
	 * @param protocol
	 *            the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		AppContext.instance().appParams.setParam(SESSION_KEY, sessionId);
		this.sessionId = sessionId;
	}

	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public void setSessionIdTemporary(String sessionId) {
		this.sessionId = sessionId;
	}

	public void clearData() {
		super.clearData();
		sessionId = "";
		protocol = HTTPS_PROL;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("ip:").append(ipAddr)
				.append(",port:").append(ipPort).append(",prol:")
				.append(protocol).append(",session:").append(sessionId)
				.toString();

	}
}

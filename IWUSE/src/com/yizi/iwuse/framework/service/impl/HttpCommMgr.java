package com.yizi.iwuse.framework.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpStatus;

import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.common.utils.ILog;
import com.yizi.iwuse.constants.PublicConst;
import com.yizi.iwuse.framework.exception.ConnException;
import com.yizi.iwuse.framework.model.CmdResultInfo;
import com.yizi.iwuse.framework.model.ConnInfo;
import com.yizi.iwuse.framework.model.HttpConnInfo;
import com.yizi.iwuse.framework.service.AbsConnMgr;
import com.yizi.iwuse.framework.service.CmdSendAdapter;
import com.yizi.iwuse.framework.service.MsgInterface.CmdInterface;

/**
 * http的连接管理器
 */
public class HttpCommMgr extends AbsConnMgr {

	/** The logging tag used by this class with ILog. */
	protected static final String TAG = "HttpConnMgr";

	/** 服务器链接超时时间 */
	public static final int CONN_TIME_OUT = 9900;

	public static final int UI_THREAD_CONN_TIME_OUT = 4200;

	/** 解析响应消息，组装为CmdResultInfo */
	private CmdRspParser cmdRspParser;

//	private JsonObjectRequest jsonObjectRequest;

	public HttpCommMgr() {
		init();
	}

	/**
	 * 设置连接信息，初始化http url的部分常量
	 */
	@Override
	public void init() {
		cmdRspParser = new CmdRspParser();

		// 放一个默认ip防止出错
		mconnInfo = new HttpConnInfo("", "", "127.0.0.1",
				HttpConnInfo.HTTP_PROL);
		setConnInfo(mconnInfo);
		try {
			initHttpsSSL();
		} catch (Exception e) {
			ILog.e(TAG, e);
		}

	}

	public void initHttpsSSL() throws NoSuchAlgorithmException,
			KeyManagementException {
		// TLS验证
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, new TrustManager[] { new MyTrustManager() },
				new SecureRandom());

		// android 以前版本存在bug' it's doing an unnecessary DNS reverse lookup.
		// 定义一个自定义的工厂类覆写其和dns解析相关的代码
		final SSLSocketFactory delegate = sc.getSocketFactory();
		SSLSocketFactory factory = new SSLSocketFactory() {
			@Override
			public Socket createSocket(String host, int port)
					throws IOException, UnknownHostException {
				InetAddress addr = InetAddress.getByName(host);
				injectHostname(addr, host);
				return delegate.createSocket(addr, port);
			}

			@Override
			public Socket createSocket(InetAddress host, int port)
					throws IOException {
				return delegate.createSocket(host, port);
			}

			@Override
			public Socket createSocket(String host, int port,
					InetAddress localHost, int localPort) throws IOException,
					UnknownHostException {
				return delegate.createSocket(host, port, localHost, localPort);
			}

			@Override
			public Socket createSocket(InetAddress address, int port,
					InetAddress localAddress, int localPort) throws IOException {
				return delegate.createSocket(address, port, localAddress,
						localPort);
			}

			private void injectHostname(InetAddress address, String host) {
				try {
					Field field = InetAddress.class
							.getDeclaredField("hostName");
					field.setAccessible(true);
					field.set(address, host);
				} catch (Exception ignored) {
				}
			}

			@Override
			public Socket createSocket(Socket s, String host, int port,
					boolean autoClose) throws IOException {
				injectHostname(s.getInetAddress(), host);
				return delegate.createSocket(s, host, port, autoClose);
			}

			@Override
			public String[] getDefaultCipherSuites() {
				return delegate.getDefaultCipherSuites();
			}

			@Override
			public String[] getSupportedCipherSuites() {
				return delegate.getSupportedCipherSuites();
			}
		};

		HttpsURLConnection.setDefaultSSLSocketFactory(factory);

		HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
	}

	public String getFile(String fileName) {
		// TODO
		return null;
	}

	public CmdResultInfo sendCmd(String cmdStr) throws ConnException {

		return sendHttpCommand("", cmdStr);
	}

	@Override
	public CmdResultInfo sendCmd(String cmdStr, String content)
			throws ConnException {
		if (null != content) {
			return sendHttpCommand(content, cmdStr);
		} else {
			return sendCmd(cmdStr);
		}
	}

	public synchronized void setConnInfo(ConnInfo connInfo) {
		super.setConnInfo(connInfo);
		HttpConnInfo info = (HttpConnInfo) mconnInfo;

		// 将IP,端口号以及协议和方法名拼接成URL字符串,设置conninfo中获取文件的url和执行命令的url的前缀
		StringBuilder url = new StringBuilder();
		url.append(info.getProtocol()).append("://").append(info.ipAddr);
		url.append(":").append(info.getIpPort()).append("/");

		// http://10.11.26.86:80/
		info.setFileUrlPre(url.toString());
		// http://10.11.26.86:80/action.cgi?ActionID=
		info.setCmdUrl(url.append(HttpConnInfo.CGI_CMD_FLAG).toString());
		ILog.v(TAG, "setConnInfo URL:" + url);
	}

	/**
	 * 发送http(s)命令，并获取响应结果，调用相应的handler去进行处理
	 * 
	 * @param json
	 *            json流
	 * @param method
	 *            命令字
	 * @return 命令的结果信息
	 */
	private CmdResultInfo sendHttpCommand(String json, String method) {
		HttpConnInfo info = (HttpConnInfo) mconnInfo;

		final CmdResultInfo bean = new CmdResultInfo();
		/****
		 * volley 的JsonObjectRequest方式 try { jsonObjectRequest = new
		 * JsonObjectRequest(info.getCmdUrl() + method, new JSONObject(json),
		 * new Response.Listener<JSONObject>(){
		 * 
		 * @Override public void onResponse(JSONObject jsObj) { // 空的json响应 if
		 *           (jsObj == null || jsObj.toString().length() == 0) {
		 *           bean.faultNo = PublicConst.RSP_JASON_ERR_CODE; } else { //
		 *           根据json消息中标志的success不为1，则将返回消息错误置为RSP_JASON_ERR_CODE if
		 *           (PublicConst.RSP_MAILBOX_SUCCESS !=
		 *           jsObj.optInt("success")) { bean.faultNo =
		 *           cmdRspParser._checkErrorAndException(jsObj, bean); } }
		 *           bean.resInfo = jsObj; }
		 * 
		 *           }, new Response.ErrorListener(){
		 * @Override public void onErrorResponse(VolleyError error) { // if
		 *           (error. instanceof NetworkResponse) // { // bean.faultNo =
		 *           PublicConst.RSP_NET_TIMEOUT_CODE; // } // else // {
		 *           bean.faultNo = PublicConst.RSP_UNKNOW_ERR_CODE; // } }
		 * 
		 *           });
		 *           AppContext.instance().requestQueue.add(jsonObjectRequest);
		 *           } catch (Exception e) { if (e instanceof
		 *           SocketTimeoutException) { bean.faultNo =
		 *           PublicConst.RSP_NET_TIMEOUT_CODE; } else { bean.faultNo =
		 *           PublicConst.RSP_UNKNOW_ERR_CODE; } // 方法执行错误的处理，需要弹出错误消息
		 *           ILog.e(TAG, "Command Type:" + method + " error:", e); //
		 *           返回一个用户自定义的错误信息 bean.paramList = null; } return bean;
		 ****/

		HttpURLConnection conn = null;
		OutputStream out = null;
		try {
			if (!method.equals(CmdInterface.GetMail_BoxData
					.getCmds(CmdInterface.GetMail_BoxData)[0])) {
				ILog.v(TAG, "URL:" + info.getCmdUrl() + method);
			}
			conn = (HttpURLConnection) new URL(info.getCmdUrl() + method)
					.openConnection();

			// 设置连接信息
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// 判断线程，如果当前是UI线程，则将超时时间改为3.5s,后台线程为10S，防止通过UI引起的远程操作造成弹出“强制关闭的界面”
			if (Thread.currentThread().getName()
					.equalsIgnoreCase(PublicConst.MAIN_THREAD_FLAG)) {
				conn.setConnectTimeout(UI_THREAD_CONN_TIME_OUT);
				conn.setReadTimeout(UI_THREAD_CONN_TIME_OUT);
			} else {
				conn.setConnectTimeout(CONN_TIME_OUT);
				conn.setReadTimeout(CONN_TIME_OUT);
			}

			conn.setRequestProperty("accept", "text/xml;text/html");
			conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			conn.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("iwuseversion",
					AppContext.instance().iwuseVer
							+ AppContext.instance().iwuseReleaseDate);
			conn.setRequestProperty("padtype", "android");
			conn.setDefaultUseCaches(true);
			conn.setUseCaches(true);
			conn.setRequestMethod("POST");
			// 当发送的命令不是取session id的命令时，需要在header中添加session
			if (CmdSendAdapter.SESSION_CMD.equals(method)) {
				conn.setRequestProperty("Cookie", "SessionID=");
			} else {
				// 当发送的命令是取session id的命令时,在cookie中添加SessionID项。
				String sessionId = info.getSessionId();
				conn.setRequestProperty("sessionid", sessionId);
				conn.setRequestProperty("Cookie", "SessionID=" + sessionId);
			}

			// 如果是发送日志文件的
			if (method.equals(CmdInterface.PUBLIC_SendLogCmd
					.getCmds(CmdInterface.PUBLIC_SendLogCmd)[0])) {
				sendBigFile(json, conn);
			} else {
				if (json.length() > 0) {
					sendJsonString(json, conn);
				}
			}

			// 建立连接
			conn.connect();

			// 获取返回数据
			int responseCode = conn.getResponseCode();
			if (responseCode == HttpStatus.SC_OK) {
				// 如果是获取sessionID的消息，需要单独在http报文头中取出sessionID
				if (method.equals("Web_RequestSessionID")) {
					String str = conn.getHeaderField("Set-Cookie");
					if (str != null) {
						String[] session = str.split("=");
						if (session[1] != null) {
							info.setSessionIdTemporary(session[1]);
						}
					}
				}

				InputStream is = conn.getInputStream();
				return cmdRspParser.parseReturnInfo(is, method);
			} else
			// 响应为失败
			{

				if (CmdSendAdapter.SESSION_CMD.equals(method)) {
					info.setSessionId(null);
				}

				// 返回结果不是200时的处理
				ILog.e(TAG, "Response Error Code:" + responseCode);

				// 返回一个用户自定义的错误信息
				bean.faultNo = PublicConst.RSP_HTTP_ERR_CODE;
				bean.paramList = null;
				return bean;
			}
		} catch (Exception e) {
			if (e instanceof SocketTimeoutException) {
				bean.faultNo = PublicConst.RSP_NET_TIMEOUT_CODE;
			} else {
				bean.faultNo = PublicConst.RSP_UNKNOW_ERR_CODE;
			}
			// 方法执行错误的处理，需要弹出错误消息
			ILog.e(TAG, "Command Type:" + method + " error:", e);
			// 返回一个用户自定义的错误信息
			bean.paramList = null;
			return bean;
		} finally {
			// 关闭输出流的处理
			if (out != null) {
				try {
					out.close();
				} catch (IOException ex) {
					ILog.e(TAG, "Stream close error:", ex);
					// 返回一个用户自定义的错误信息

					bean.faultNo = PublicConst.RSP_UNKNOW_ERR_CODE;
					bean.paramList = null;
				}
			}

			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	/**
	 * 发送大文件
	 * 
	 * @param fileName
	 *            大文件内容
	 * @param conn
	 *            当前的连接管理器
	 * @throws IOException
	 */
	public void sendBigFile(String fileName, HttpURLConnection conn)
			throws IOException {
		FileInputStream fr = new FileInputStream(fileName);
		byte[] buffer = new byte[1024];

		OutputStream os = null;
		try {
			os = conn.getOutputStream();
			while (fr.read(buffer) != -1) {
				os.write(buffer);
			}
		} catch (Exception e) {
			ILog.e(TAG, e);
		} finally {
			if (null != os) {
				os.close();
			}
		}
	}

	/**
	 * 发送json的字符串
	 * 
	 * @param json
	 *            要发生的字符串
	 * @param conn
	 *            当前的连接管理器
	 * @throws IOException
	 */
	public void sendJsonString(String json, HttpURLConnection conn)
			throws IOException {
		OutputStream out = null;
		try {
			out = conn.getOutputStream();
			out.write(json.getBytes("utf-8"));
		} catch (Exception e) {
			ILog.e(TAG, e);
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}

	/**
	 * 创建URL字符串
	 * 
	 * @param name
	 *            方法名或者是文件名
	 * @param getFileFlag
	 *            是否是获取文件
	 * 
	 * @return
	 */
	public String createURL(String name, boolean getFileFlag) {
		// 将IP,端口号以及协议和方法名拼接成URL字符串
		HttpConnInfo info = (HttpConnInfo) mconnInfo;
		String ipAddress = info.ipAddr;
		int port = info.ipPort;
		String protocol = info.getProtocol();

		StringBuffer url = new StringBuffer();
		if (HttpConnInfo.HTTPS_PROL.equalsIgnoreCase(protocol)) {
			url.append(HttpConnInfo.HTTPS_PROL).append("://");
			port = HttpConnInfo.HTTPS_PORT_DFT;
		} else {
			url.append(HttpConnInfo.HTTP_PROL).append("://");
			port = HttpConnInfo.HTTP_PORT_DFT;
		}

		if (getFileFlag) {
			// 如果是获取文件信息时的处理
			url.append(ipAddress).append(":").append(port).append("/")
					.append(name).toString();
		} else {
			// 需要调用接口时的处理
			url.append(ipAddress).append(":").append(port)
					.append("/action.cgi").append("?").append("ActionID")
					.append("=").append(name).toString();
		}

		return url.toString();
	}

}

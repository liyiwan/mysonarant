package com.yizi.iwuse.constants;

/**
 * 公共的定义常量
 * 
 */
public class PublicConst {
	/** 主线程标志 */
	public static final String MAIN_THREAD_FLAG = "main";

	/** 换行符 */
	public static final String SPLIT_LINE = "\n";

	/** 无效的资源id */
	public static final int INVALID_RES_ID = 1;

	/**
	 * HTTP
	 */
	public static final String COMMON_HTTP = "HTTP";

	/**
	 * HTTPS
	 */
	public static final String COMMON_HTTPS = "HTTPS";

	/** 通用等待超时 S */
	public static final int COMM_WAIT_TIMEOUT = 15;

	/**
	 * 逗号
	 */
	public static final String COMMA = ",";

	/** IWuse 版本号前缀 */
	public static final String VER_PRE = "IWuse";

	/** 响应正确的返回码 */
	public static final int RSP_OK_CODE = 0;

	/** 响应消息中标志该响应结果是正确的 */
	public static final int RSP_MAILBOX_SUCCESS = 1;
	/** 用户自定义的未知的错误ID */
	public static final int RSP_UNKNOW_ERR_CODE = -1;

	/** HTTP返回结果结果不是200时定义的错误ID */
	public static final int RSP_HTTP_ERR_CODE = 10000;

	/** 响应的jason流为空或解析时格式出错的异常 或是响应消息中的'success'字段为0 */
	public static final int RSP_JASON_ERR_CODE = 9998;

	/** session id 为null */
	public static final int RSP_SESSION_NULL_CODE = 10001;

	/** 用户自定义的网络超时错误ID */
	public static final int RSP_NET_TIMEOUT_CODE = 0x10002;

	/*** 图片的缓存大小 ***/
	public static final int IMAGE_MAXCACHE_SIZE = 10 * 1024 * 1024;
	
	/**大版本号***/
	public static final String VERSION = "V1.0.0";

	public static final int CLICKINTERVALTIME = 500;
}

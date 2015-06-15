package com.yizi.iwuse.framework.service;

import com.yizi.iwuse.common.utils.ILog;
import com.yizi.iwuse.framework.exception.ConnException;
import com.yizi.iwuse.framework.model.CmdResultInfo;
import com.yizi.iwuse.framework.model.ConnInfo;

/****
 * 网络连接抽象
 * 
 * @author zhangxiying
 *
 */
public abstract class AbsConnMgr {

	/** The logging tag used by this class with ILog. */
	protected static final String TAG = "AbsConnMgr";

	/** 连接相关的信息类 */
	public ConnInfo mconnInfo;

	/**
	 * 从连接端获取远程的文件
	 * 
	 * @param fileName
	 *            要获取的文件名
	 * @return 返回放入本地的文件路径
	 */
	public abstract String getFile(String fileName);

	/**
	 * 管理器的初始化
	 */
	public abstract void init();

	/**
	 * 发送命令给连接端
	 * 
	 * @param cmdStr
	 *            命令字
	 * @param content
	 *            消息字符串
	 * @return 响应结果
	 * @throws ConnException
	 */
	public abstract CmdResultInfo sendCmd(String cmdStr, String content)
			throws ConnException;

	/**
	 * 发送命令给连接端
	 * 
	 * @param cmdStr
	 *            含有命令字和参数的命令消息字符串
	 * @throws ConnException
	 *             发送过程中出现连接异常
	 */
	public abstract CmdResultInfo sendCmd(String cmdStr) throws ConnException;

	/**
	 * 设置连接相关的信息类。
	 * 
	 * @param connInfo
	 *            the connInfo to set
	 */
	public void setConnInfo(ConnInfo connInfo) {
		mconnInfo = connInfo;
		ILog.d(TAG, "update ConnInfo");
	}

	/**
	 * 创建URL字符串
	 * 
	 * @param name
	 *            方法名或者是文件名
	 * @param getFileFlag
	 *            是否是获取文件
	 * @return
	 */
	public abstract String createURL(String name, boolean getFileFlag);

}

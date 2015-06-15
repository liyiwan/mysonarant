package com.yizi.iwuse.framework.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.common.utils.ILog;
import com.yizi.iwuse.constants.PublicConst;
import com.yizi.iwuse.framework.exception.ConnException;
import com.yizi.iwuse.framework.handler.AbsCmdMsgHandler;
import com.yizi.iwuse.framework.handler.HandlerFactory;
import com.yizi.iwuse.framework.model.CmdResultInfo;
import com.yizi.iwuse.framework.service.MsgInterface.CmdInterface;

/**
 * 发送的命令适配层,在上层,使用经过抽象的命令
 */
public class CmdSendAdapter {
	/** The logging tag used by this class with ILog. */
	protected static final String TAG = "CmdSendAdapter";

	/**
	 * 业务层用来发送命令的接口
	 * 
	 * @param cmd
	 *            业务命令字
	 * @param bean
	 *            和命令字相关的数据类，为空用null；
	 * @return 返回针对此业务命令处理后的http的立即响应结果
	 */
	public static CmdResultInfo sendCmd(CmdInterface cmd, Object bean) {
		ILog.v(TAG, "---------[" + cmd.toString() + "]------begin----");
		// 获取全局的连接管理器
		AbsConnMgr connMgr = AppContext.instance().connMgr;

		// 获取消息处理器
		AbsCmdMsgHandler msgHandler = HandlerFactory.getInstance()
				.getCmdMsgHandlerByCmd(cmd);

		// 获取命令字
		String[] interfaceCmd = cmd.getCmds(cmd);

		// 命令结果
		List<CmdResultInfo> results = new ArrayList<CmdResultInfo>();

		// 如果抽象命令对应的具体命令不存在，表示当前对接设备不用支持该命令，返回一个成功的响应结果
		if (null == interfaceCmd || interfaceCmd.length <= 0) {
			ILog.w(TAG, cmd + " is not specified command.");
			return new CmdResultInfo();
		}

		for (String scmd : interfaceCmd) {
			try {
				// 如果为空，则抛出异常，让下面去统一获取
				if (null == scmd || scmd.length() <= 0) {
					throw new IllegalArgumentException("the cmd is null.");
				}

				// 如果命令参数填充时出现异常，则发送空的命令参数过去，一般不会出现此情况
				String cmdContent = null;
				try {
					cmdContent = msgHandler.getSendMsdDetail(cmd, scmd, bean);
					ILog.v(TAG, "[send param]:" + cmdContent);
				} catch (JSONException e) {
					ILog.e(TAG, "send cmd:[" + scmd
							+ "] ,cmd content is error,", e);
				}

				// 真正发送的地方
				CmdResultInfo resultInfo = connMgr.sendCmd(scmd, cmdContent);
				results.add(resultInfo);

				// 如果某个字命令出现异常，则后续的命令停止执行
				if (resultInfo.faultNo != PublicConst.RSP_OK_CODE) {
					break;
				}
			}
			// 捕获所有的异常，防止出现空指针之类的异常，挂死线程
			catch (Throwable e) {
				ILog.e(TAG, "send cmd:[" + scmd + "] failed.", e);
				CmdResultInfo resultInfo = new CmdResultInfo();
				resultInfo.faultNo = PublicConst.RSP_UNKNOW_ERR_CODE;
				results.add(resultInfo);
			}
		}

		CmdResultInfo resultInfo = null;
		// 调用消息处理器处理结果,如果一个抽象命令对应多个命令，暂时定为多个命令结束后，统一将结果交给handler进行处理
		try {
			resultInfo = msgHandler.handleCmdRsp(cmd, results);
		} catch (Throwable e) {
			ILog.e(TAG, e);
			resultInfo = new CmdResultInfo();
			resultInfo.faultNo = PublicConst.RSP_HTTP_ERR_CODE;
		}
		return resultInfo;
	}

	/**
	 * 获取session
	 * 
	 * @return
	 */
	public static CmdResultInfo sendSessionCmd() {
		// 获取全局的连接管理器
		AbsConnMgr connMgr = AppContext.instance().connMgr;
		try {
			return connMgr.sendCmd(SESSION_CMD, "");
		} catch (ConnException e) {
			ILog.e(TAG, "send get session command falied" + e);
			CmdResultInfo resultInfo = new CmdResultInfo();
			resultInfo.faultNo = PublicConst.RSP_UNKNOW_ERR_CODE;
			return resultInfo;
		}
	}

	/**
	 * 发送日志文件
	 * 
	 * @return
	 */
	public static CmdResultInfo sendLogCmd(String fileName) {
		// 获取全局的连接管理器
		AbsConnMgr connMgr = AppContext.instance().connMgr;
		try {
			// 如果没有定义logout命令，直接返回
			return connMgr.sendCmd(CmdInterface.PUBLIC_SendLogCmd
					.getCmds(CmdInterface.PUBLIC_SendLogCmd)[0], fileName);
		} catch (ConnException e) {
			ILog.e(TAG, "send hid log command falied" + e);
			CmdResultInfo resultInfo = new CmdResultInfo();
			resultInfo.faultNo = PublicConst.RSP_UNKNOW_ERR_CODE;
			return resultInfo;
		}
	}

	// 获取session的命令接口
	public static final String SESSION_CMD = "WEB_RequestSessionIDAPI";

	// 注销登陆的命令接口
	public static final String LOG_OUT_CMD = "WEB_LogoutAPI";

}

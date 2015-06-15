package com.yizi.iwuse.framework.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.common.utils.ILog;
import com.yizi.iwuse.common.utils.IWuseUtil;
import com.yizi.iwuse.constants.PublicConst;
import com.yizi.iwuse.framework.model.CmdResultInfo;
import com.yizi.iwuse.framework.service.MsgInterface.CmdInterface;

/**
 * 命令响应消息的解析器
 */
public class CmdRspParser {
	/** The logging tag used by this class with ILog. */
	protected static final String TAG = "CmdRspParser";

	// 对接主机时若主机返回的错误码中的padId字段若为3则表示请求越权
	private static final int WEB_SERVER_NO_RIGHT_CONNECTION = 3;

	/**
	 * 解析服务器响应的字符串流，调用相应的命令处理器进行处理，返回处理结果
	 * 
	 * @param is
	 *            连接端响应的字符串流
	 * @param cmd
	 *            命令字符串
	 * @return 处理结果
	 */
	public CmdResultInfo parseReturnInfo(InputStream is, String cmd) {
		String rspStr = convertStreamToString(is, cmd);
		boolean isNeedPrintLog = true;
		// 屏蔽历史记录和邮箱打印，因为信息太多。
		if (cmd.equals(CmdInterface.GetMail_BoxData
				.getCmds(CmdInterface.GetMail_BoxData)[0])) {
			isNeedPrintLog = false;
		} else {

		}

		if (isNeedPrintLog) {
			ILog.v(TAG, "[rsp]:" + rspStr);
		}

		return _parserResponseStream(rspStr, cmd);
	}

	/**
	 * 解析命令的即时返回结果
	 * 
	 * @param jsReturnInfo
	 *            命令返回信息
	 * @throws JSONException
	 *             JSON解析出错信息
	 */
	private static boolean checkReturnInfofaultNo(JSONObject obj) {
		boolean flag = false;
		int status = 0;
		try {
			status = obj.getInt("faultNo");

			if (status == PublicConst.RSP_OK_CODE) {
				return true;
			}
		} catch (Exception e) {
			flag = true;
		}

		return flag;
	}

	/**
	 * 检查消息的错误或者异常
	 * 
	 * @return
	 */
	public int _checkErrorAndException(JSONObject obj, CmdResultInfo result) {
		int padId = 0;
		// 1、优先处理error对象，解析error中的code字段
		if (obj.has("error")) {
			try {
				// 先寻找error中code字段的int值，作为faultNo
				JSONObject errJson = obj.getJSONObject("error");

				// 如果错误对应没有消息参数，则不处理
				JSONArray errMsgParams = errJson.optJSONArray("params");
				if (null != errMsgParams) {
					for (int i = 0; i < errMsgParams.length(); i++) {
						result.paramList.add((String) errMsgParams.get(i));
					}
				}

				return spelHandleErrCode(errJson.getInt("code"));
			} catch (JSONException e) {
				return PublicConst.RSP_JASON_ERR_CODE;
			}
		}

		// 2、解析exception对象，解析exception中的padid字段
		if (obj.has("exception")) {
			try {
				JSONObject error = obj.getJSONObject("exception");
				if (error.has("padid")) {
					ILog.v(TAG, "对接解析exception中的padid");

					padId = error.getInt("padid");
					if (WEB_SERVER_NO_RIGHT_CONNECTION == padId) {
						ILog.v(TAG, "请求越权，即将重启");
						IWuseUtil
								.restartIWuse(AppContext.instance().globalContext);
					}
					return padId;
				} else {
					return error.getInt("id");
				}
			} catch (JSONException e) {
				return PublicConst.RSP_JASON_ERR_CODE;
			}
		}

		return PublicConst.RSP_JASON_ERR_CODE;
	}

	/**
	 * 解析连接端响应的字符串流，调用相应的命令处理器进行处理，返回处理结果
	 * 
	 * @param is
	 *            连接端响应的字符串流
	 * @param cmd
	 *            命令字符串
	 * @return 处理结果
	 */
	private CmdResultInfo _parserResponseStream(String rspStr, String cmd) {
		JSONObject jsObj = null;
		// 检查响应消息是否正常
		CmdResultInfo result = new CmdResultInfo();
		try {
			String response = new String(rspStr.getBytes(), "utf-8");
			jsObj = new JSONObject(response);

			// 空的json响应
			if (jsObj == null || jsObj.toString().length() == 0) {
				result.faultNo = PublicConst.RSP_JASON_ERR_CODE;
			} else {
				// 根据json消息中标志的success不为1，则将返回消息错误置为RSP_JASON_ERR_CODE
				if (PublicConst.RSP_MAILBOX_SUCCESS != jsObj.getInt("success")) {
					result.faultNo = _checkErrorAndException(jsObj, result);
				}
			}
		} catch (Exception e) {
			ILog.e(TAG, "check rsp message exception:", e);
			result.faultNo = PublicConst.RSP_JASON_ERR_CODE;
		}

		if (PublicConst.RSP_OK_CODE != result.faultNo) {
			return result;
		} else {
			result.resInfo = jsObj;
			// 获取返回信息数据
			try {
				if (!"".equalsIgnoreCase(jsObj.getString("data"))) {
					result.dataJson = new JSONObject(jsObj.getString("data"));
				} else {
					result.dataJson = new JSONObject();
				}
			} catch (JSONException e) {
				ILog.e(TAG, e);
				result.dataJson = new JSONObject();
			}
			return result;
		}
	}

	/**
	 * 将web返回的响应的json的流转化为字符串
	 * 
	 * @param is
	 *            输入流
	 * @param cmd
	 *            命令字
	 * @return 响应的字符串
	 */
	private static String convertStreamToString(InputStream is, String cmd) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is),
				10240);
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				// 过滤掉头信息，只返回JSON字符串
				if (line.startsWith("{")) {
					sb.append(line);
				}
			}
		} catch (IOException e) {
			ILog.e(TAG, "read respone for cmd:" + cmd, e);
		} finally {
			try {
				if (null != is) {
					is.close();
				}

				if (null != reader) {
					reader.close();
				}
			} catch (IOException e) {
				ILog.e(TAG, "Close input stream error:", e);
			}
		}
		return sb.toString();
	}

	/**
	 * 处理对服务器时的code字段为0，和我们的faultNo 的0表示正确冲突的问题
	 * 
	 * @param code
	 * @return
	 */
	private int spelHandleErrCode(int code) {
		// 当code为0时和我们成功时的faultNo冲突改为json error
		if (code == 0) {
			code = PublicConst.RSP_JASON_ERR_CODE;
		}
		return code;
	}
}

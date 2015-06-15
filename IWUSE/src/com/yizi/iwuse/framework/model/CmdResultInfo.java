package com.yizi.iwuse.framework.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.yizi.iwuse.constants.PublicConst;

/***
 * 发送命令的的响应结果的信息类。
 * 
 * @author zhangxiying
 */
public class CmdResultInfo {

	/** 邮箱消息的处理结果类型 */
	public static final int RST_TYPE_MAILBOX = 0;

	/** 即时响应的命令的结果类型，即发送命令之后马上得到响应，不需要通过邮箱消息的命令结果 */
	public static final int RST_TYPE_INSTANT = 1;

	/** 临时的结果，只表示该命令已经发送成功，并成功响应，至于命令的具体处理结果要等邮箱消息的 */
	public static final int RST_TYPE_TMP = 2;

	/** 无效的消息结果类型，目前支持不了的 */
	public static final int RST_TYPE_INVALID = -1;

	/** 发送命令结果的错误码，0为OK，非0为错误码 */
	public int faultNo = PublicConst.RSP_OK_CODE;

	/** 错误码中对应的消息串中的可变参数 */
	public List<String> paramList = new ArrayList<String>();

	/** 相应的具体json格式的信息 */
	public JSONObject resInfo = null;

	/** 即时消息中的'data'字段对应的json */
	public JSONObject dataJson = null;

	/** 消息的类型 */
	public int type = RST_TYPE_INVALID;

	// 'subMsgID' 字段
	public int subMsgID;

	// 'param1' 字段
	public int param1;

	// 'param2' 字段
	public int param2;

	/** 扩展的自定义字段 */
	public Object customObj;

}

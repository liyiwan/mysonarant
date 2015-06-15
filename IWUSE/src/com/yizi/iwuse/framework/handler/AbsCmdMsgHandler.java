package com.yizi.iwuse.framework.handler;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.yizi.iwuse.framework.model.CmdResultInfo;
import com.yizi.iwuse.framework.model.MailBoxMsgBean;
import com.yizi.iwuse.framework.service.MsgInterface.CmdInterface;

/***
 * 响应消息的处理器，每个命令类型共用一个处理器，所以注意
 * <p>
 * <b> 该处理器是无状态的</b>
 * </p>
 * 主要负责解析消息流，更新对应service管理的数据model
 * 
 * @author zhangxiying
 *
 */
public abstract class AbsCmdMsgHandler {

	/**
	 * 根据实际的连接对象以及实际连接对象关联的名字字符串，从bean中提取出命令的字符串，目前是json
	 * 
	 * @param absCmd
	 *            接口抽象的命令字
	 * @param cmdStr
	 *            和服务器对接的命令字符串
	 * @param bean
	 *            包含命令参数的bean
	 * @return 命令参数字符串，目前是json格式
	 */
	public abstract String getSendMsdDetail(CmdInterface absCmd, String cmdStr,
			Object bean) throws JSONException;

	/**
	 * 处理命令字的响应
	 * 
	 * @param cmd
	 *            接口抽象的命令字
	 * @param results
	 *            响应的结果对象集
	 * @return
	 */
	public abstract CmdResultInfo handleCmdRsp(CmdInterface cmd,
			List<CmdResultInfo> results);

	/**
	 * 处理邮箱消息
	 * 
	 * @param mailMsgId
	 *            邮箱消息id
	 * @param MailBoxMsgBean
	 *            邮箱消息每个msg的信息
	 * @return 该邮箱消息处理后的结果
	 */
	public CmdResultInfo handleMailBoxRsp(int mailMsgId, MailBoxMsgBean msg) {
		return null;
	};

	/**
	 * @param mailMsgId
	 *            邮箱消息id
	 * @param MailBoxMsgBean
	 *            邮箱消息每个msg的信息
	 * @return 该邮箱消息处理后的结果
	 */
	public CmdResultInfo handleMailBoxRsp(String mailMsgId, MailBoxMsgBean msg) {
		return null;
	};

	/**
	 * 获取要订阅的邮箱的数据 子类可以覆写该方法
	 * 
	 * @return 订阅字段的集合
	 */
	public List<String> getSubscribeMailData() {
		return null;
	};

	/**
	 * 根据各个模块关系的邮箱订阅的字段进行相应的处理 子类可以覆写该方法
	 * 
	 * @param json
	 *            邮箱消息中对应的'state'字段的json
	 */
	public void handleSubscribeMailData(JSONObject stateJson) {

	}
}

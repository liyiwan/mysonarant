package com.yizi.iwuse.general.service.handler;

import java.util.List;

import org.json.JSONException;

import com.yizi.iwuse.framework.handler.AbsCmdMsgHandler;
import com.yizi.iwuse.framework.model.CmdResultInfo;
import com.yizi.iwuse.framework.service.MsgInterface.CmdInterface;

public class GeneralHandler extends AbsCmdMsgHandler {

	@Override
	public String getSendMsdDetail(CmdInterface absCmd, String cmdStr,
			Object bean) throws JSONException {
		switch(absCmd){
			
		}
		
		return null;
	}

	@Override
	public CmdResultInfo handleCmdRsp(CmdInterface cmd,
			List<CmdResultInfo> results) {
		return null;
	}

}

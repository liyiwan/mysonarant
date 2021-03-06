package com.yizi.iwuse.user.service.handler;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.yizi.iwuse.constants.PublicConst;
import com.yizi.iwuse.framework.handler.AbsCmdMsgHandler;
import com.yizi.iwuse.framework.model.CmdResultInfo;
import com.yizi.iwuse.framework.service.MsgInterface.CmdInterface;

public class UserMsgHandler extends AbsCmdMsgHandler {

	@Override
	public String getSendMsdDetail(CmdInterface absCmd, String cmdStr,Object bean) throws JSONException {
		JSONObject json = new JSONObject();

        switch (absCmd)
        {
	        case CUSTOMER_DetailInfo:
	        	//json.put(name, value);
	        	break;
        }
        
        return json.toString();
	}

	@Override
	public CmdResultInfo handleCmdRsp(CmdInterface cmd,List<CmdResultInfo> results) {
		CmdResultInfo info = results.get(0);
        if (info.faultNo != PublicConst.RSP_OK_CODE)
        {
            return info;
        }
        JSONObject resInfo = info.resInfo;
        switch (cmd)
        {
            case CUSTOMER_DetailInfo: // 无参数
            {
            	
            }
        }
        
        return info;
	}

}

package com.yizi.iwuse.wxapi;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.BaseActivity;
import com.yizi.iwuse.common.utils.ILog;
import com.yizi.iwuse.constants.UserConst;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/****
 * 微信授权登录
 * 
 * @author zhangxiying
 *
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private static final String TAG = "WXEntryActivity";
	
	private IWXAPI weichatApi;
	private BaseResp response;
	private HttpUtils http  = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ILog.i(TAG, "onCreate");
		http = new HttpUtils();
		//handleIntent(getIntent());
		weichatApi = WXAPIFactory.createWXAPI(this, UserConst.OAUTH_WEICHAT_APPID, false);
		weichatApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ILog.i(TAG, "onNewIntent");
        setIntent(intent);
        weichatApi.handleIntent(intent, this);
		finish();
    }

    /****
     * 处理微信官方返回的结果信息
     * 
     * @param intent
     */
    private void handleIntent(Intent intent) {
        SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
        switch(resp.errCode){
        	case BaseResp.ErrCode.ERR_OK:
        		//用户授权成功，从微信端获取用户唯一标识信息
        		String oauth_weichat_url_step1 = UserConst.OAUTH_WEICHAT_URL_STEP1
        				+"appid="+UserConst.OAUTH_WEICHAT_APPID+"&secret="+UserConst.OAUTH_WEICHAT_APPID+"&code="+resp.code
        				+"&grant_type=authorization_code";
        		http.send(HttpMethod.GET, oauth_weichat_url_step1, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException exception, String info) {
						 Toast.makeText(AppContext.instance().globalContext, 
								 AppContext.instance().globalContext.getString(R.string.prompt_oauth_weichat_exception), Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject resJson = new JSONObject(responseInfo.result);
							ILog.i(TAG, "handleIntent,onSuccess:"+resJson);
						} catch (JSONException e) {
							ILog.e(TAG, e);
						}
					}
				});
        	break;
        }
    }

    /***
     * 微信发送请求到第三方应用时，会回调到该方法
     * 
     */
	@Override
	public void onReq(BaseReq arg0) {
		finish();
	}

	/***
	 * 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	 * 
	 */
	@Override
	public void onResp(BaseResp response) {
		String result = "";
		if (response != null) {
			this.response = response;
		}
		switch(response.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result ="发送成功";
				Toast.makeText(this, result, Toast.LENGTH_LONG).show();
				String code = ((SendAuth.Resp) response).code;
				//用户授权成功，从微信端获取用户唯一标识信息
        		String oauth_weichat_url_step1 = UserConst.OAUTH_WEICHAT_URL_STEP1
        				+"appid="+UserConst.OAUTH_WEICHAT_APPID+"&secret="+UserConst.OAUTH_WEICHAT_SECRET+"&code="+code
        				+"&grant_type=authorization_code";
        		http.send(HttpMethod.GET, oauth_weichat_url_step1, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException exception, String info) {
						 Toast.makeText(AppContext.instance().globalContext, 
								 AppContext.instance().globalContext.getString(R.string.prompt_oauth_weichat_exception), Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							
							JSONObject resJson = new JSONObject(responseInfo.result);
							getWeichatUserInfo(resJson);
						} catch (JSONException e) {
							ILog.e(TAG, e);
						}
					}
        			
				});
				finish();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = "发送取消";
				Toast.makeText(this, result, Toast.LENGTH_LONG).show();
				finish();
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = "发送被拒绝";
				Toast.makeText(this, result, Toast.LENGTH_LONG).show();
				finish();
				break;
			default:
				result = "发送返回";
				Toast.makeText(this, result, Toast.LENGTH_LONG).show();
				finish();
				break;
		}
	}
	/***
	 * 获取用户信息
	 * 
	 * @param resJson
	 */
	private void getWeichatUserInfo(JSONObject resJson){
		ILog.i(TAG, "get user infomation!");
		String openId = resJson.optString("openid");
		String access_token = resJson.optString("access_token");
		String oauth_weichat_url_step2 = UserConst.OAUTH_WEICHAT_URL_STEP2
				+"access_token="+access_token+"&openid="+openId;
		http.send(HttpMethod.GET, oauth_weichat_url_step2, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException exception, String info) {
				 Toast.makeText(AppContext.instance().globalContext, 
						 AppContext.instance().globalContext.getString(R.string.prompt_oauth_weichat_exception), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
					AppContext.instance().userService.
							onLoginUpdateUserInfo(UserConst.LOGINTYPE_WEICHAT, responseInfo.result);
			}
			
		});
	}
	
}

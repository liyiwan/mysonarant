package com.yizi.iwuse.user;




import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.AppParams;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.BaseActivity;
import com.yizi.iwuse.common.utils.AccessTokenKeeper;
import com.yizi.iwuse.common.utils.ILog;
import com.yizi.iwuse.constants.UserConst;
import com.yizi.iwuse.user.model.ErrorInfo;
import com.yizi.iwuse.user.service.events.UserEvents;

import de.greenrobot.event.EventBus;

/****
 * 用户登录Activity
 * 
 * @author zhangxiying
 *
 */
@SuppressLint("NewApi")
public class LoginActivity extends BaseActivity {

	private static final String TAG = "LoginActivity";

	/** 用户ID/手机号 **/
	@ViewInject(R.id.edt_userid)
	private EditText edt_userid;
	/** 密码 **/
	@ViewInject(R.id.edt_password)
	private EditText edt_password;
	/** 登录 **/
	@ViewInject(R.id.btn_login)
	private Button btn_login;
	/** 忘记密码 **/
	@ViewInject(R.id.txt_forgetuserinfo)
	private TextView txt_forgetuserinfo;
	/** 微信授权登录 **/
	@ViewInject(R.id.img_oauth_weichat)
	private ImageView img_oauth_weichat;
	/** QQ授权登录 **/
	@ViewInject(R.id.img_oauth_qq)
	private ImageView img_oauth_qq;
	/** 支付宝授权登录 **/
	@ViewInject(R.id.img_oauth_paybao)
	private ImageView img_oauth_paybao;
	/** 微博授权登录 **/
	@ViewInject(R.id.img_oauth_weibo)
	private ImageView img_oauth_weibo;
	/**返回**/
	@ViewInject(R.id.img_userlogin_back)
	private ImageView img_back;
	/**登录切换按钮**/
	@ViewInject(R.id.btn_switch_login)
	private Button btn_switch_login;
	/**注册切换按钮**/
	@ViewInject(R.id.btn_switch_regist)
	private Button btn_switch_regist;
	/**登录层**/
	@ViewInject(R.id.lay_switch_login)
	private LinearLayout lay_switch_login;
	/**注册层**/
	@ViewInject(R.id.lay_switch_regist)
    private LinearLayout lay_switch_regist;
	/**注册用户名**/
	@ViewInject(R.id.edt_regist_userid)
	private EditText edt_regist_userid;
	/**注册密码**/
	@ViewInject(R.id.edt_regist_password)
	private EditText edt_regist_password;
	@ViewInject(R.id.btn_regist_connfirm)
	private Button btn_regist_connfirm;
	@ViewInject(R.id.btn_login_test)
	private Button btn_login_test;
	
	/** 友盟授权登录集成 **/
//	private UMSocialService mController = null;
	
	/**登录方式**/
	private int login_type = UserConst.LOGINTYPE_WEICHAT;

	/**微信授权登录*/
	public static IWXAPI weichatApi;
	private String weixinCode;
	private final static int LOGIN_WHAT_INIT = 1;
	private static String get_access_token = "";
	
	/**QQ授权登录**/
	public static QQAuth mQQAuth;
	private static Tencent mTencent;
	private UserInfo mInfo;
	/**新浪微博**/
	private AuthInfo mAuthInfo = null;
	private SsoHandler mSsoHandler = null;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_usercenter_login);
		mContext = this;
		/** 注册xUtils UI框架 **/
		ViewUtils.inject(this);
		/** 注册EventBus **/
		EventBus.getDefault().register(this);
		//微信授权
		weichatApi = WXAPIFactory.createWXAPI(LoginActivity.this, UserConst.OAUTH_WEICHAT_APPID, true);
		weichatApi.registerApp(UserConst.OAUTH_WEICHAT_APPID);
		//QQ授权
		//mQQAuth = QQAuth.createInstance(UserConst.OAUTH_QQ_APPID, mContext);
		mTencent = Tencent.createInstance(UserConst.OAUTH_QQ_APPID, mContext);
		
		mAuthInfo = new AuthInfo(this, UserConst.OAUTH_WEIBO_APPID, UserConst.OAUTH_WEIBO_REDIRECT_URL, UserConst.OAUTH_WEIBO_SCOPE);
		//mController = UMServiceFactory.getUMSocialService(UserConst.LOGIN_YOUMENG);
		//registLoginHandler();
	}

	/****
	 * 登录事件 通过xUtils绑定View事件
	 * 
	 * @param view
	 */
	@OnClick({ R.id.btn_switch_login,R.id.btn_switch_regist, R.id.btn_login, R.id.txt_forgetuserinfo,
			R.id.img_oauth_weichat, R.id.img_oauth_qq, R.id.img_oauth_paybao,R.id.btn_login_test,
			R.id.img_oauth_weibo,R.id.img_userlogin_back,R.id.btn_regist_connfirm })
	public void onLoginClickListener(View view) {
		switch (view.getId()) {
			case R.id.btn_switch_login:
				lay_switch_login.setVisibility(View.VISIBLE);
				lay_switch_regist.setVisibility(View.GONE);
				break;
			case R.id.btn_switch_regist:
				lay_switch_login.setVisibility(View.GONE);
				lay_switch_regist.setVisibility(View.VISIBLE);
				break;
			case R.id.btn_login:
				break;
			case R.id.btn_login_test:
				AppParams.isLogin = true;
				Toast.makeText(mContext, mContext.getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
				finish();
				break;
			case R.id.txt_forgetuserinfo:
				break;
			case R.id.img_oauth_weichat:
				login_type = UserConst.LOGINTYPE_WEICHAT;
				onLoginWithWeichat();
				//onThridLogin(SHARE_MEDIA.WEIXIN);
				break;
			case R.id.img_oauth_qq:
				login_type = UserConst.LOGINTYPE_QQ;
				//onThridLogin(SHARE_MEDIA.QQ);
				onLoginWithQQ();
				break;
			case R.id.img_oauth_paybao:
				login_type = UserConst.LOGINTYPE_PAYBAO;
				break;
			case R.id.img_oauth_weibo:
				login_type = UserConst.LOGINTYPE_WEIBO;
				//onThridLogin(SHARE_MEDIA.SINA);
				onLoginWithWeibo();
				break;
			case R.id.btn_regist_connfirm:
				break;
			case R.id.img_userlogin_back:
				finish();
				break;
			default:
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (login_type) {
			case UserConst.LOGINTYPE_WEIBO: {
				/** 使用SSO授权必须添加如下代码 */
//				UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
//						requestCode);
//				if (ssoHandler != null) {
//					ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//				}
				if (mSsoHandler != null) {
			        mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
			    }
				break;
			}
			case UserConst.LOGINTYPE_QQ: {
				mTencent.onActivityResult(requestCode, resultCode, data);
				break;
			}
		}
	}

	/***
	 * 发起微信授权登录
	 * 
	 */
	private void onLoginWithWeichat(){
		ILog.i(TAG, "onLoginWithWeichat()");
	    final SendAuth.Req req = new SendAuth.Req();
	    req.scope = "snsapi_userinfo";
	    req.state = UserConst.OAUTH_WEICHAT_STATE;
	    weichatApi.sendReq(req);
	}
	
	/***
	 * 发起QQ登录
	 * 
	 */
	private void onLoginWithQQ(){
		if (!mTencent.isSessionValid()) {
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					ILog.i(TAG, "login doComplete");
					updateUserInfo();
				}
			};
			//mQQAuth.login(this, "all", listener);
			mTencent.login(this, "all", listener);
		} else {
			//mQQAuth.logout(this);
		}
	}
	/***
	 * 获取QQ用户信息
	 * 
	 */
	private void updateUserInfo() {
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError e) {
				}

				@Override
				public void onComplete(final Object response) {
					ILog.i(TAG, "get user info sucess!");
					AppContext.instance().userService.onLoginUpdateUserInfo(login_type, response);
				}

				@Override
				public void onCancel() {
				}
				
			};
			mInfo = new UserInfo(this, mTencent.getQQToken());
			mInfo.getUserInfo(listener);

		} else {
			 ILog.i(TAG, "get userinfo failed");
		}
	}
	
	/***
	 * 发起新浪微博授权
	 */
	public void onLoginWithWeibo(){
		mSsoHandler = new SsoHandler(this, mAuthInfo);
		mSsoHandler. authorize(new AuthListener());
	}
	
	
	/****
	 * 注册授权登录的监听
	 * 
	 */
	private void registLoginHandler() {
//		// 微信
//		UMWXHandler wxHandler = new UMWXHandler(LoginActivity.this,
//				UserConst.OAUTH_WEICHAT_APPID, UserConst.OAUTH_WEICHAT_SECRET);
//		//wxHandler.mShareContent  = UserConst.OAUTH_WEICHAT_SCOPE;
//		wxHandler.addToSocialSDK();
//		// QQ
//		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(LoginActivity.this,
//				UserConst.OAUTH_QQ_APPID, UserConst.OAUTH_QQ_APPKEY);
//		qqSsoHandler.addToSocialSDK();
//		// 新浪微博
//		SinaSsoHandler sinaSsoHandler = new SinaSsoHandler();
//
//		mController.getConfig().setSsoHandler(sinaSsoHandler);
	}

	/**
	 * 授权第三方登录。如果授权成功，则获取用户信息
	 * 微信、QQ、微博
	 * 
	 * @param platform
	 */
//	private void onThridLogin(final SHARE_MEDIA platform) {
//		mController.doOauthVerify(LoginActivity.this, platform,
//			new UMAuthListener() {
//
//				@Override
//				public void onStart(SHARE_MEDIA platform) {
//					Toast.makeText(LoginActivity.this, "授权开始",Toast.LENGTH_SHORT).show();
//				}
//
//				@Override
//				public void onError(SocializeException e, SHARE_MEDIA platform) {
//					Toast.makeText(LoginActivity.this, AppContext.instance().globalContext.getString(R.string.oauth_promt_failed),Toast.LENGTH_SHORT).show();
//				}
//
//				@Override
//				public void onComplete(Bundle value, SHARE_MEDIA platform) {
//					Toast.makeText(LoginActivity.this, "授权完成",Toast.LENGTH_SHORT).show();
//					//标记为已登录状态
//					AppParams.isLogin = true;
//					// 获取uid
//					String uid = value.getString("uid");
//					if (!TextUtils.isEmpty(uid)) {
//						// uid不为空，获取用户信息
//						handleUserInfo(platform);
//					} else {
//						
//						Toast.makeText(LoginActivity.this, AppContext.instance().globalContext.getString(R.string.oauth_promt_failed),Toast.LENGTH_SHORT).show();
//					}
//				}
//
//				@Override
//				public void onCancel(SHARE_MEDIA platform) {
//					Toast.makeText(LoginActivity.this, AppContext.instance().globalContext.getString(R.string.oauth_promt_cancel),
//							Toast.LENGTH_SHORT).show();
//				}
//			});
//	}

	/**
	 * 获取用户信息
	 * 
	 * @param platform
	 */
//	private void handleUserInfo(final SHARE_MEDIA platform) {
//		mController.getPlatformInfo(LoginActivity.this, platform,
//			new UMDataListener() {
//
//				@Override
//				public void onStart() {
//
//				}
//
//				@Override
//				public void onComplete(int status, Map<String, Object> info) {
//					if (info != null) {
//						// 根据平台类型，解析用户信息
//						AppContext.instance().userService.onLoginUpdateUserInfo(platform, info); 
//					}
//				}
//			});
//	}

	/****
	 * 用户监后台事件
	 * 
	 * @param userEvent
	 */
	public void onEventMainThread(UserEvents userEvent) {
		switch (userEvent.eventtype) {
		case UserConst.ENVENTTYPE_LOGIN:
			Toast.makeText(mContext, mContext.getString(R.string.login), Toast.LENGTH_LONG).show();
			finish();
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		ILog.i(TAG, "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		ILog.i(TAG, "onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void removeAllView() {

	}
	
	public static boolean ready(Context context) {
		if (mTencent == null) {
			return false;
		}
		boolean ready = mTencent.isSessionValid()
				&& mTencent.getQQToken().getOpenId() != null;
		if (!ready)
			Toast.makeText(context, "login and get openId first, please!",
					Toast.LENGTH_SHORT).show();
		return ready;
	}
	
	
	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			Toast.makeText(mContext, "Login success", Toast.LENGTH_LONG).show();
			doComplete((JSONObject) response);
		}
		
		protected void doComplete(JSONObject values) {
			ILog.i(TAG, "doComplete");
		}

		@Override
		public void onError(UiError e) {
			Toast.makeText(mContext, "Login failed", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(mContext, "Login cancel", Toast.LENGTH_LONG).show();
		}
		
	}
	private Oauth2AccessToken mAccessToken;
	
	class AuthListener  implements WeiboAuthListener {
	    
	    @Override
	    public void onComplete(Bundle values) {
	    	ILog.i(TAG, "sina weibo auth sucess!");
	        // 从 Bundle 中解析 Token
	        mAccessToken = Oauth2AccessToken.parseAccessToken(values);
	        if (mAccessToken.isSessionValid()) {
	            // 保存 Token 到 SharedPreferences
	            AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
	        	new Thread(){
					@Override
					public void run() {
						UsersAPI mUsersAPI = new UsersAPI(LoginActivity.this, UserConst.OAUTH_WEIBO_APPID, mAccessToken);
			        	mUsersAPI.show(Long.parseLong(mAccessToken.getUid()), weiboUserInfoListener);
					}
	        	}.start();
	        	
	        } else {
	        // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
	            String code = values.getString("code", "");
	        }
	    }

	    @Override
	    public void onCancel() {
	    }

	    @Override
	    public void onWeiboException(WeiboException e) {
	    }
	}
	/**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener weiboUserInfoListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
        	ILog.i(TAG, "RequestListener.onComplete");
            if (!TextUtils.isEmpty(response)) {
                ILog.i(TAG, response);
                // 调用 UserService
                AppContext.instance().userService.onLoginUpdateUserInfo(login_type, response);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ILog.e(TAG, "get weibo user info Exception:"+e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(LoginActivity.this, info.toString(), Toast.LENGTH_LONG).show();
        }
    };
	
}

package com.yizi.iwuse.constants;

/****
 * 用户常量
 * 
 * @author zhangxiying
 *
 */
public class UserConst {
	
	/**用户事件类型-登录**/
	public static final int ENVENTTYPE_LOGIN = 0x500001;
	
	public static final int LOGINTYPE_WEICHAT = 0x9001;
	public static final int LOGINTYPE_QQ = 0x9002;
	public static final int LOGINTYPE_PAYBAO = 0x9003;
	public static final int LOGINTYPE_WEIBO = 0x9004;
	
	/***用户事件类型-钱包***/
	public static final int ENVENTTYPE_WALLET = 0x500002;
	
	/***用户事件类型-购物车***/
	public static final int ENVENTTYPE_SHOPPING_CAR = 0x500003;
	
	public static final String LOGIN_YOUMENG = "com.umeng.login";
	
	/**~~~~~~~~~~~~~~授权登录~~~~~~~~~~~~~~~**/
	//微信
	 //应用标识
	public static final String OAUTH_WEICHAT_APPID = "wx056395f0737456ce";//"wxd930ea5d5a258f4f";//
	//应用密钥
	public static final String OAUTH_WEICHAT_SECRET = "2f75a8635ceebf108bade5599ddeaeef";//"375a728a398ae4c73d4edf1d833f80bc";//
	 //授权作用域
	public static final String OAUTH_WEICHAT_SCOPE = "all";
	//保持请求和回调的状态
	public static final String OAUTH_WEICHAT_STATE = "weichat_iwuse_0x9891";
	/**微信授权登录成功，获取数据**/
	//第一步URL 获取code
	public static final String OAUTH_WEICHAT_URL_STEP1 = "https://api.weixin.qq.com/sns/oauth2/access_token?";
													//appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code"
	//第二步URL 获取用户信息
	public static String OAUTH_WEICHAT_URL_STEP2="https://api.weixin.qq.com/sns/userinfo?";
			//"access_token=ACCESS_TOKEN&openid=OPENID";
	//QQ
	 //应用标识
	public static final String OAUTH_QQ_APPID = "1104654550";
	
	public static final String OAUTH_QQ_APPKEY = "bBt8igECdhIH2aGU";
	
	public static final String OAUTH_QQ_SCOPE = "get_user_info,add_t";
	
	//新浪微博
	public static final String OAUTH_WEIBO_APPID = "723854602";
	public static final String OAUTH_WEIBO_SECRET = "4681fd71ab31140082c9bc522025682f";
	public static final String OAUTH_WEIBO_REDIRECT_URL = "http://www.yizi.com.cn";
	public static final String OAUTH_WEIBO_SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
	
	
}

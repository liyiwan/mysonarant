package com.yizi.iwuse.user.model;

public class ThridUserInfo {
	public int loginType;
	/**接口调用凭证**/
    public String access_token;
    /**access_token接口调用凭证超时时间，单位（秒）**/
    public int expires_in;
    /**用户刷新access_token**/
    public String refresh_token;
    /**授权用户唯一标识**/
    public String openid;
    /**用户授权的作用域，使用逗号（,）分隔**/
    public String scope;
    /**只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段**/
    public String unionId;
    /**头像地址***/
    public String headUrl;
    /**昵称**/
    public String nickName;
    
}

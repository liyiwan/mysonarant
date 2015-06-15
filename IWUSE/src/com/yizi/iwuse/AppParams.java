package com.yizi.iwuse;

import java.util.Map;

import com.yizi.iwuse.common.utils.ILog;

import android.content.Context;
import android.content.SharedPreferences;

/***
 * 本应用程序用到的全局的数据的保存类
 * 
 * @author zhangxiying
 *
 */
public class AppParams {
	protected static final String TAG = "AppParams";

	/** 用户名的文件存取KEY */
	public static final String PARAM_USER_KEY = "userName";

	/** 密码的文件存取KEY */
	public static final String PARAM_PWD_KEY = "userPassword";

	/** IP地址的文件存取KEY */
	public static final String PARAM_IP_KEY = "ipAddress";

	/** 端口的文件存取KEY */
	public static final String PARAM_PORT_KEY = "ipPort";

	/** 协议的文件存取KEY */
	public static final String PARAM_PROL_KEY = "protocol";

	/** WIFISSID的文件存取 */
	public static final String PARAM_WIFI_SSID_KEY = "wifiSSID";

	/** 默认语言的文件存取KEY */
	public static final String PARAM_LANGUAGE_KEY = "language";

	/** 应用程序上下文 */
	private Context mContext;
	
	/**用户是否已登录**/
	public static volatile boolean isLogin = false;
	/** sp 句柄 */
	private SharedPreferences settings;

	public AppParams(Context context) {
		mContext = context;
		update();
	}

	/**
	 * 重新从文件中读取一下配置信息
	 */
	public void update() {
		settings = mContext.getSharedPreferences("paramsxml",
				Context.MODE_PRIVATE);
	}

	/**
	 * 初始化保存在文件中的数据
	 * 
	 * @return 返回连接设置的类
	 */
	public void initFileData() {
		Map<String, ?> paramMap = settings.getAll();

		ILog.e(TAG, "paramMap size:" + paramMap.size());

		if (paramMap.size() > 0) {
			// TODO something
		}
	}

	/**
	 * 是否从sp里面扔除对应的key
	 * 
	 * @param key
	 *            要扔除的key
	 */
	public void removeParam(String key) {
		settings.edit().remove(key).commit();
	}

	/**
	 * 设置参数key的值文件中
	 * 
	 * @param key
	 *            保存的键
	 * @param value
	 *            需要保存的值
	 */
	public void setParam(String key, String value) {
		// paramMap.put(key, value);
		settings.edit().putString(key, value).commit();
	}

	/**
	 * 设置参数key的值文件中
	 * 
	 * @param key
	 *            保存的键
	 * @param value
	 *            需要保存的值
	 */
	public void setParam(String key, int value) {
		// paramMap.put(key, value);
		settings.edit().putInt(key, value).commit();
	}

	/**
	 * 设置参数key的值文件中
	 * 
	 * @param key
	 *            保存的键
	 * @param value
	 *            需要保存的值
	 */
	public void setParam(String key, float value) {
		// paramMap.put(key, value);
		settings.edit().putFloat(key, value).commit();
	}

	/**
	 * 设置参数key的值文件中
	 * 
	 * @param key
	 *            保存的键
	 * @param value
	 *            需要保存的值
	 */
	public void setParam(String key, boolean value) {
		settings.edit().putBoolean(key, value).commit();
	}

	/**
	 * 获取配置文件中保持的键值
	 * 
	 * @param key
	 * @return
	 */
	public String getParamStringByKey(String key) {
		return settings.getString(key, "");
	}

	/**
	 * 获取配置文件中保持的键值
	 * 
	 * @param key
	 * @return
	 */
	public Integer getParamIntByKey(String key) {
		return settings.getInt(key, 0);
	}

	/**
	 * 获取配置文件中保持的键值,带默认值
	 * 
	 * @param key
	 * @return
	 */
	public Integer getParamIntByKey(String key, int defaultValue) {
		return settings.getInt(key, defaultValue);
	}

	/**
	 * 获取配置文件中保持的键值
	 * 
	 * @param key
	 * @return
	 */
	public boolean getParamBooleanByKey(String key) {
		return settings.getBoolean(key, false);
	}

	/**
	 * 获取配置文件中保持的键值
	 * 
	 * @param key
	 * @return
	 */
	public float getParamFloatByKey(String key) {
		return settings.getFloat(key, 0);
	}

}

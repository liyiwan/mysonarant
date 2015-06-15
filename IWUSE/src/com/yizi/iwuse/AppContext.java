package com.yizi.iwuse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.yizi.iwuse.common.base.ICoreService;
import com.yizi.iwuse.common.base.ViewFactory;
import com.yizi.iwuse.common.utils.ILog;
import com.yizi.iwuse.filter.service.FilterService;
import com.yizi.iwuse.framework.database.DataBaseManager;
import com.yizi.iwuse.framework.service.impl.HttpCommMgr;
import com.yizi.iwuse.general.service.GeneralService;
import com.yizi.iwuse.order.service.OrderService;
import com.yizi.iwuse.product.service.ProductService;
import com.yizi.iwuse.user.service.UserService;

/***
 * 整个应用的全局上下文
 * 
 * @author zhangxiying
 *
 */
public class AppContext {
	/** The logging tag used by this class with */
	protected static final String TAG = "AppContext";
	/** 全局上下文 **/
	private static volatile AppContext instance = null;

	/** 全局的连接管理器 */
	public HttpCommMgr connMgr;

	/** iwuse 版本号通过packetInfo得到赋值 */
	public String iwuseVer;

	/** 带入http头信息中向主机发送的版本号 */

	/** hid的版本日期 */
	public final static String iwuseReleaseDate = "  Release 1.0.0.0 2015.5.8";
	
	/**
	 * ===================================== Warning：全局控制区 end
	 * =====================================
	 **/

	/** 用户信息服务 **/
	public UserService userService;
	/** 订单Service **/
	public OrderService orderService;
	/** 产品Service **/
	public ProductService productService;
	/** 综合Service ***/
	public GeneralService generalService;
	/** 筛选Service ***/
	public FilterService filterService;

	/** 所有service的集合 */
	public List<ICoreService> serviceList = new ArrayList<ICoreService>();

	/** 是否需要升级iwuse app */
	public boolean isUpdate = false;

	/** 开始升级iwuse的标准，用于保证只进行一次文件取操作。 */
	public boolean isBeginIwuseUpadate = false;

	/** 升级地址的 URL */
	public String updateUrl = "/IWuse.apk";

	/** 当前升级包的版本 */
	public String updateVersion = "";

	/** 应用程序的全局上下文 */
	public Context globalContext;

//	/** volley框架类 ，请求句柄 **/
//	public RequestQueue requestQueue;
	/** 应用程序的全局参数 */
	public AppParams appParams;

	/** 是否完全初始化 */
	public boolean isInited = false;

	/** 是否该类已经实例化完成 */
	public static boolean isInstanceed = false;

	/** 屏的像素宽和高 */
	public int[] disPlay = { 1280, 800 };

	/** 是否当前为开发者模式 */
	public static final boolean isDevelopMode = false;

	/** 是否当前为开发者模式 */
	public static final boolean isTestMode = true;

	/** 当前活动的activity */
	public Activity curActivity = null;

	/**
	 * 获取AppContext的单例
	 * 
	 * @return AppContext的实例
	 */
	public static AppContext instance() {
		// 如果为空或是没有实例化完成，则等待
		if (instance == null || !isInstanceed) {
			synchronized (AppContext.class) {
				if (instance == null) {
					instance = new AppContext();
					instance.init();
					isInstanceed = true;
				}
			}
		}

		return instance;
	}

	private void init() {
		// 注册event层的类
		registService();
		// 初始化连接管理器
		connMgr = new HttpCommMgr();
		// for 自动化测试
		if (isTestMode) {
			
		}
	}

	/**
	 * 注册service层的各个类
	 */
	private void registService() {
		// 用户管理类
		userService = new UserService();
		serviceList.add(userService);

		// 订单管理
		orderService = new OrderService();
		serviceList.add(orderService);

		// 商品管理
		productService = new ProductService();
		serviceList.add(productService);

		// 综合服务
		generalService = new GeneralService();
		serviceList.add(generalService);
		
		// 筛选服务
		filterService = new FilterService();
		serviceList.add(filterService);
	}

	/**
	 * 注册全局UI handler,需要在UI线程中进行
	 */
	public synchronized void initUiRes(Context context) {
		if (isInited) {
			return;
		}

		ILog.v(TAG, "initUiRes ...");
		// 记录一个全局上下文
		globalContext = context;
//		// 初始化请求volley队列
//		requestQueue = Volley.newRequestQueue(globalContext);
		//获取屏幕的分辨率
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		disPlay[0] = dm.widthPixels;
		disPlay[1] = dm.heightPixels ;
		// 初始化数据库
		DataBaseManager.getInstance(globalContext).initDataBase();
		// 初始化参数文件
		appParams = new AppParams(globalContext);
		appParams.initFileData();

		try {
			iwuseVer = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			iwuseVer = "unkonw";
			ILog.e(TAG, e);
		}

		isInited = true;
	}

	/**
	 * 初始化屏幕的像素参数
	 * 
	 * @param activity
	 */
	public void initDisplay(Activity activity) {
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		disPlay[0] = metric.widthPixels; // 屏幕宽度（像素）
		disPlay[1] = metric.heightPixels; // 屏幕高度（像素）
	}

	/**
	 * 初始化每个event需要进行的一些状态初始化
	 * 
	 * 暂时都返回true,不能因为event数据初始化异常造成用户不能登录
	 * 
	 * @return 全部初始化成功，返回true，否则返回false
	 */
	public boolean initServiceState() {

		for (ICoreService service : serviceList) {
			if (!service.initState()) {
				ILog.e(TAG, "init service [" + service.toString()
						+ "]'s state failed.");
				// return false;
			}
		}
		ILog.d(TAG, "init service's state.");
		return true;
	}

	/**
	 * 销毁该全局处理器
	 */
	public static void destory() {
		ILog.i(TAG, "destory appcontent......");
		if (null != instance) {
			instance = null;
		}
		isInstanceed = false;

		// for 自动化测试
		if (isTestMode) {

		}
		// 销毁view 工厂
		ViewFactory.destory();
	}

	/**
	 * 获取当前的配置区域
	 * 
	 * @return
	 */
	public Locale getCurrentLocale() {
		if (globalContext != null) {
			return globalContext.getResources().getConfiguration().locale;
		}
		return Locale.getDefault();
	}
}

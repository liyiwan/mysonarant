package com.yizi.iwuse.user.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.sina.weibo.sdk.openapi.models.User;
import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.AppParams;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.ICoreService;
import com.yizi.iwuse.common.utils.ILog;
import com.yizi.iwuse.constants.UserConst;
import com.yizi.iwuse.framework.database.DataBaseManager;
import com.yizi.iwuse.framework.model.CmdResultInfo;
import com.yizi.iwuse.framework.service.CmdSendAdapter;
import com.yizi.iwuse.framework.service.MsgInterface.CmdInterface;
import com.yizi.iwuse.user.database.UserDataManager;
import com.yizi.iwuse.user.model.AddressInfo;
import com.yizi.iwuse.user.model.CityModel;
import com.yizi.iwuse.user.model.DistrictModel;
import com.yizi.iwuse.user.model.ProvinceModel;
import com.yizi.iwuse.user.model.ShoppingItem;
import com.yizi.iwuse.user.model.ThridUserInfo;
import com.yizi.iwuse.user.model.WalletItem;
import com.yizi.iwuse.user.service.events.UserEvents;

import de.greenrobot.event.EventBus;

public class UserService implements ICoreService {
	
	private static final String TAG = "UserService";
	private ArrayList<WalletItem> walletArray;
	private ArrayList<ShoppingItem> shoppingArray;
	
	@Override
	public boolean initState() {
		return true;
	}

	/****
	 *  获取用户信息
	 * 
	 * @return
	 */
	public CmdResultInfo getCustomerInfo(){
		// EventBus.getDefault().post(null);
		return CmdSendAdapter.sendCmd(CmdInterface.CUSTOMER_DetailInfo, null);
	}
	
	/****
	 * 登录成功后，更新用户信息
	 * 
	 */
	public void onLoginUpdateUserInfo(int loginType,Object responseData) {
		ThridUserInfo userInfo = new ThridUserInfo();
		JSONObject rspJson = null;
		ILog.i(TAG, "onLoginUpdateUserInfo");
		switch(loginType){
			case UserConst.LOGINTYPE_WEICHAT:
				try {
					rspJson = new JSONObject(responseData.toString());
					userInfo.nickName = rspJson.getString("nickname");
					userInfo.headUrl = rspJson.getString("headimgurl");
				} catch (JSONException e1) {
					ILog.e(TAG, e1);
				}
				break;
			case UserConst.LOGINTYPE_QQ:
				try {
				    rspJson = new JSONObject(responseData.toString());
					userInfo.nickName = rspJson.getString("nickname");
					userInfo.headUrl = rspJson.getString("figureurl_qq_2");
				} catch (JSONException e) {
				   ILog.e(TAG, e);
				}
				break;
			case UserConst.LOGINTYPE_PAYBAO:
				break;
			case UserConst.LOGINTYPE_WEIBO:
				 User user = User.parse(responseData.toString());
				 userInfo.nickName = user.name;
				 userInfo.headUrl = user.profile_image_url; 
				break;
			default:break;
		}
		// 修改用户全局登录状态为已登录
		AppParams.isLogin = true;
		
		UserEvents loginEvent = new UserEvents();
		loginEvent.eventtype = UserConst.ENVENTTYPE_LOGIN;
		loginEvent.customObj = userInfo;
		
		EventBus.getDefault().post(loginEvent);
	}
	
	/***
	 * 注销用户，更新用户信息
	 * 
	 */
	public void onLogoutUpdateUserInfo(){
		
	}
	
	public void shoppingDoNetwork(){
		UserDataManager userDatabase = new UserDataManager();
		String userId = "abc";
		shoppingArray = new ArrayList<ShoppingItem>();
		ShoppingItem shoppingItem1 = new ShoppingItem(1,"imgurl","网络数据","产品属性信息",2888,1,userId);
		shoppingArray.add(shoppingItem1);
		userDatabase.saveShoppingNoChange(shoppingItem1);
		ShoppingItem shoppingItem2 = new ShoppingItem(2,"imgurl","网络数据","产品属性信息",2888,2,userId);
		shoppingArray.add(shoppingItem2);
		userDatabase.saveShoppingNoChange(shoppingItem2);
		ShoppingItem shoppingItem3 = new ShoppingItem(3,"imgurl","网络数据","产品属性信息",2888,1,userId);
		shoppingArray.add(shoppingItem3);
		userDatabase.saveShoppingNoChange(shoppingItem3);
		UserEvents userEvents = new UserEvents();
		userEvents.eventtype = UserConst.ENVENTTYPE_SHOPPING_CAR;
		userEvents.customObj = shoppingArray;
		EventBus.getDefault().post(userEvents);
	}
	
	public void doNetwork(){
		walletArray = new ArrayList<WalletItem>();
		WalletItem walletItem = new WalletItem(888,"满10000可用","2016-10-16","43454235",1);
		walletArray.add(walletItem);
		WalletItem walletItem1 = new WalletItem(888,"满10000可用","2016-10-16","43454235",0);
		walletArray.add(walletItem1);
		WalletItem walletItem2 = new WalletItem(888,"满10000可用","2016-10-16","43454235",1);
		walletArray.add(walletItem2);
		WalletItem walletItem3 = new WalletItem(888,"满10000可用","2016-10-16","43454235",1);
		walletArray.add(walletItem3);
		WalletItem walletItem4 = new WalletItem(888,"满10000可用","2016-10-16","43454235",1);
		walletArray.add(walletItem4);
		UserEvents userEvents = new UserEvents();
		userEvents.eventtype = UserConst.ENVENTTYPE_WALLET;
		userEvents.customObj = walletArray;
		EventBus.getDefault().post(userEvents);
	}
	
	/***
	 * 初始化地址基础数据
	 * 
	 */
	public void initAddressBaseData() {
		List<String> sqlList = new ArrayList<String>();
		//获取数据源
		DbUtils dbUtils = DataBaseManager
				.getInstance(AppContext.instance().globalContext).dbUtils;
		SQLiteDatabase mSqlLiteDatabase = null;
		try {
			//1.当前是否有地址信息
			Selector mSelector = Selector.from(AddressInfo.class);
			//获取所有的省及直辖市
			WhereBuilder mWhere = WhereBuilder.b("level", "==", 1);
			mSelector.where(mWhere);
			mSelector.orderBy("id", true);
			long count = dbUtils.count(mSelector);
			ILog.i(TAG, "addrList size"+count);
			if(count<1){
				mSqlLiteDatabase = dbUtils.getDatabase();
				//将sql语句读入内存
				InputStream in = AppContext.instance().globalContext.getAssets()
						.open("address.sql");
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(in, "utf-8"));
				String sqlStr = null;
				while ((sqlStr = bufferedReader.readLine()) != null) {
					if (!TextUtils.isEmpty(sqlStr)) {
						ILog.i(TAG, sqlStr);
						sqlList.add(sqlStr);
					}
				}
				bufferedReader.close();
				in.close();
				//开启事务，准备将sql插入表
				mSqlLiteDatabase.beginTransaction();
				Iterator<String> ite = sqlList.iterator();
				while (ite.hasNext()) {
					mSqlLiteDatabase.execSQL(ite.next());
				}
				//标识当前插入事务成功
				mSqlLiteDatabase.setTransactionSuccessful();
			}
		} catch (SQLException e) {
			ILog.e(TAG, e);
		} catch (IOException e) {
			ILog.e(TAG, e);
		} catch (DbException e) {
			ILog.e(TAG, e);
		}finally{
			//提交事务
			if(mSqlLiteDatabase!=null){
				mSqlLiteDatabase.endTransaction();
			}
		}
	}
	
	/***
	 * 获取本地库中地址基础数据
	 * 
	 * @return
	 */
	public List<ProvinceModel> getBaseAddressList(){
		List<ProvinceModel> addrList = new ArrayList<ProvinceModel>();
		List<CityModel> cityList = null;
		List<DistrictModel> districtList = null;
		DbUtils dbUtils = DataBaseManager
				.getInstance(AppContext.instance().globalContext).dbUtils;
		Selector mSelector = Selector.from(AddressInfo.class);
		//获取所有的省及直辖市
		WhereBuilder mWhere = WhereBuilder.b("parentId", "==", "0");
		mWhere.and("level", "==", 1);
		
		mSelector.where(mWhere);
		//mSelector.orderBy("id", true);
		//mSelector.limit(7);
		AddressInfo proAddInfo = null;
		ProvinceModel provinceModel = null;
		
		try {
			List<AddressInfo> provinList =  dbUtils.findAll(mSelector);
			Iterator<AddressInfo> provinIte = provinList.iterator();
			while(provinIte.hasNext()){
				provinceModel = new ProvinceModel();
				proAddInfo = provinIte.next();
				provinceModel.setName(proAddInfo.name);
				//获取所有的市
				mSelector =  Selector.from(AddressInfo.class);
			    mWhere = WhereBuilder.b("parentId", "==", proAddInfo.code);
			    mWhere.and("level", "==", 2);
			    mSelector.where(mWhere);
				//mSelector.orderBy("id", true);
				List<AddressInfo> citys = dbUtils.findAll(mSelector);
				Iterator<AddressInfo> cityIte = citys.iterator();
				AddressInfo cityInfo = null;
				CityModel cityModel = null;
				cityList = new ArrayList<CityModel>();
				while(cityIte.hasNext()){
					cityModel = new CityModel();
					cityInfo = cityIte.next();
					cityModel.setName(cityInfo.name);
					// 获取所有 的 区
					mSelector =  Selector.from(AddressInfo.class);
					mWhere = WhereBuilder.b("parentId", "==", cityInfo.code);
					mWhere.and("level", "==", 3);
				    mSelector.where(mWhere);
					//mSelector.orderBy("id", true);
					List<AddressInfo> areas = dbUtils.findAll(mSelector);
					Iterator<AddressInfo> areaIte = areas.iterator();
					AddressInfo areaInfo = null;
					DistrictModel areaModel = null;
					districtList = new ArrayList<DistrictModel>();
					while(areaIte.hasNext()){
						areaInfo = areaIte.next();
						areaModel = new DistrictModel();
						areaModel.setName(areaInfo.name);
						areaModel.setZipcode(areaInfo.code);
						districtList.add(areaModel);
					}
					cityModel.setDistrictList(districtList);
					
							
					cityList.add(cityModel);
				}
				provinceModel.setCityList(cityList);
				addrList.add(provinceModel);
			}
		} catch (DbException e) {
			ILog.e(TAG, e);
		}
		
		
		
		return addrList;
	}
	
}

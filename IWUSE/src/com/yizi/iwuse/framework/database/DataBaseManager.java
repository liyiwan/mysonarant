package com.yizi.iwuse.framework.database;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DaoConfig;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.lidroid.xutils.exception.DbException;
import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.common.utils.ILog;
import com.yizi.iwuse.user.model.AddressInfo;

import android.content.Context;

/***
 * 数据库管理
 * 
 * @author zhangxiying
 *
 */
public class DataBaseManager {
	private static final String TAG = "DataBaseManager";
	
	private static DataBaseManager mDataBaseManager = null;
	private Context mContext = null;
	/**数据库名称**/
	private static final String DATABASE_NAME="iwuse";
	/**当前数据版本**/
	private static final int DATABASE_VERSION = 1;
	public DbUtils dbUtils;
	
	private DataBaseManager(Context mContext){
		this.mContext = mContext;
	}
	
	/***
	 * 获取数据库实例
	 * @param mContext
	 * @return
	 */
	public static DataBaseManager getInstance(Context context){
		if(mDataBaseManager==null){
		 synchronized (DataBaseManager.class) {
				mDataBaseManager = new DataBaseManager(context);
			}
		}
		 return mDataBaseManager;
	}
	
	/***
	 * 创建数据库
	 */
	public void initDataBase(){
		ILog.i(TAG, "initDataBase");
		//配置数据库参数
		DaoConfig mDaoConfig = new DaoConfig(mContext);
		mDaoConfig.setDbName(DATABASE_NAME);
		mDaoConfig.setDbVersion(DATABASE_VERSION);
		//设置数据库更新监听
		mDaoConfig.setDbUpgradeListener(dbUpgradeListener);
		//创建数据库
		dbUtils = DbUtils.create(mDaoConfig);
		if(AppContext.isTestMode){
			dbUtils.configDebug(true);
		}
		
		try {
			dbUtils.createTableIfNotExist(AddressInfo.class);
		} catch (DbException e) {
			ILog.e(TAG, "create table exception");
		}
		
		ILog.i(TAG, "initDataBase success!");
	}
	
	/***
	 * 数据库更新监听
	 * 
	 */
	private DbUpgradeListener dbUpgradeListener = new DbUpgradeListener(){

		@Override
		public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
			ILog.i(TAG, "upgrade database. oldVersion is "+oldVersion +" and newVersion is "+newVersion);
		}
	};
	
	
	
}

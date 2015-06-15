package com.yizi.iwuse.common.base;

import com.yizi.iwuse.common.utils.ILog;

import de.greenrobot.event.EventBus;
import android.content.Context;
import android.view.View;

/****
 * UI单元view的基础类，其包裹android的view，并实现
 * 
 * @author zhangxiying
 *
 */
public abstract class BaseView {

	public Context mContext;

	public BaseView(Context mContext) {
		this.mContext = mContext;
	}
	

	/**
	 * 更新上下文
	 * 
	 * @param mContext
	 *            新的上下文
	 */
	public void updateContext(Context context) {
		mContext = context;
	}

	/**
	 * 获得android中view对象
	 * 
	 * @param context
	 * @return View
	 */
	public abstract View getLayoutView();

	/**
	 * 销毁view，进行view销毁前的一些资源的清理，比如添加的监听器
	 */
	public void destory() {
		ILog.d("BaseView", "destory()");
		// EventBus.getDefault().unregister(mContext);
	}

	/**
	 * 更新数据或布局
	 */
	private void updateLayoutView() {
	}

	private void updateLayoutView(int type) {
	}

}

package com.yizi.iwuse.common.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.yizi.iwuse.common.utils.ILog;
import com.yizi.iwuse.common.utils.IWuseUtil;
import com.yizi.iwuse.constants.ViewName;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/****
 * View 的全局管理器，管理各个activity中用到个view
 * 
 * @author zhangxiying
 *
 */
public final class ViewFactory {
	/** The logging tag used by this class with ILog. */
	protected static final String TAG = "ViewFactory";

	/* 存放不同单元的BaseView对象 */
	private HashMap<String, BaseView> views = new HashMap<String, BaseView>();

	/* 最近获取的view */
	private String lastestViewName;

	/* 获取baseView时的操作码 */
	public enum OPERATION_CODE {
		/** 需要更新上下文 */
		NEED_UPDATE_CONTEXT,
		/** 不需要更新上下文 */
		DON_NOT_NEED_UPDATE_CONTEXT,
	};

	private ViewFactory() {
		ILog.d(TAG, "init the ViewFactory......");
	}

	private static volatile ViewFactory instance = null;

	/**
	 * 获取ViewFactory的单例
	 * 
	 * @return ViewFactory的实例
	 */
	public static ViewFactory getInstance() {
		if (instance == null) {
			synchronized (ViewFactory.class) {
				if (instance == null) {
					instance = new ViewFactory();
				}
			}
		}

		return instance;
	}

	/**
	 * 根据操作码来确定获取相应view时的处理方法
	 * 
	 * @param context
	 *            Context
	 * @param viewName
	 *            需要获取的View名字
	 * @param operationCode
	 *            操作码
	 * @return BaseView
	 */
	public BaseView getBaseView(Context context, String viewName,
			OPERATION_CODE operationCode) {
		// 先根据常量判断名称，输入的viewName为空或者不存在这个常量，返回空值
		if (viewName == null || "".equals(viewName.trim())) {
			return null;
		}

		// 获取出来
		BaseView bview = views.get(viewName);
		// 判断是否已包含view，如果没有则创建，并添加的views中
		if (bview == null) {
			bview = buildView(context, viewName);
			if (bview != null) {
				views.put(viewName, bview);
			}
		}

		if (OPERATION_CODE.NEED_UPDATE_CONTEXT == operationCode
				&& null != bview) {
			if (null != context) {
				// 更新上下文属性
				bview.updateContext(context);
			}
		}
		return bview;
	}

	/**
	 * 管理UI单元baseview
	 * 
	 * @param context
	 *            Context
	 * @param viewName
	 *            需要获取的View名字
	 * @return BaseView
	 */
	public BaseView getBaseView(Context context, String viewName) {
		return getBaseView(context, viewName,
				OPERATION_CODE.NEED_UPDATE_CONTEXT);
	}

	/**
	 * 获取指定名称的view，该view已经确认是从view的树形结构上脱离了
	 * 
	 * @param context
	 * @param viewName
	 * @param isForAdd
	 *            获取该view的目的是否为了添加到新的viewGroup中
	 * @return
	 */
	public BaseView getBaseView(Context context, String viewName,
			boolean isRemoveFromParent) {
		BaseView bview = getBaseView(context, viewName);
		// 如果获取该view是为了添加到其他父view上，则要先检查一下该view是否已经放入其他父view
		// 要先清除父View
		if (isRemoveFromParent) {
			// 将view的原有树形的父节点去除掉
			if (!detachViewFromParent(bview.getLayoutView())) {
				// 如果移除异常,从缓存中删除，重新创建
				removeView(viewName);
				bview = getBaseView(context, viewName);
			}
		}

		return bview;
	}

	/**
	 * 通过viewName移除ui单元baseview
	 * 
	 * @param viewName
	 *            需要移除的View名字
	 */
	public void removeView(String viewName) {
		BaseView baseView = views.get(viewName);
		if (null != baseView) {
			baseView.destory();
			views.remove(viewName);

			// 遍历view，释放资源引用,此处不能使用改方法，否则会造成同一个activity的多界面直接切换时，公共界面哦丢失
			// IWuseUtil.unbindDrawables(baseView.getLayoutView());
		}
	}

	/**
	 * 在系统退出时回收资源用
	 */
	public void destroyManager() {
		Iterator<Entry<String, BaseView>> itor = views.entrySet().iterator();
		while (itor.hasNext()) {
			Map.Entry<String, BaseView> entry = (Map.Entry<String, BaseView>) itor
					.next();
			IWuseUtil.unbindDrawables(entry.getValue().getLayoutView());
		}

		views.clear();
		instance = null;
	}

	/**
	 * 切换语言
	 */
	public void destroyManagerForChangeLag() {
		Iterator<Entry<String, BaseView>> itor = views.entrySet().iterator();
		while (itor.hasNext()) {
			Map.Entry<String, BaseView> entry = (Map.Entry<String, BaseView>) itor
					.next();
			entry.getValue().destory();
			// HIDUtil.unbindDrawables(entry.getValue().getLayoutView());
		}

		views.clear();
		instance = null;
	}

	/**
	 * 判断名称为viewNmae的View是否已经存在
	 * 
	 * @param viewName
	 *            查询的View名字
	 * @return View是否已经存在
	 */
	public boolean isViewExist(String viewName) {
		if (views.get(viewName) == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 通过ui单元名词生产对应的ui单元对象
	 * 
	 * @param context
	 *            Context
	 * @param viewName
	 *            需要生成的View名字
	 * @return 需要生成的View
	 */
	public static BaseView buildView(Context context, String viewName) {
		if (context == null || viewName == null || "".equals(viewName.trim())) {
			return null;
		}

		BaseView baseView = null;
		// if (ViewName.RB_ADDR_VIEW.equals(viewName))
		// {
		// baseView = new AddressBookView(context);
		// }
		// else if (ViewName.RB_FAV_VIEW.equals(viewName))
		// {
		// baseView = new FavoriteView(context);
		// }

		return baseView;
	}

	/**
	 * 获取当前HID所装载的ViewName
	 * 
	 * @return
	 */
	public String getCurrentViewName() {
		// 查询views中存在的类对象

		// 获取类名字符串

		return lastestViewName;
	}

	/**
	 * 将当前的要返回的view从其关联的父节点中去除，以便能够加入到其他的viewGroup中去
	 * 
	 * @param child
	 *            要获取的view
	 */
	public boolean detachViewFromParent(View child) {
		if (null == child) {
			return true;
		} else {
			View view = child.getRootView();
			if (null == view) {
				return true;
			}

			if (view instanceof ViewGroup) {
				if (removeChildView((ViewGroup) view, child)) {
					ILog.d(TAG, "remove view's parent success.");
				} else {
					ILog.d(TAG, "the view hasn't any parent.:" + child);
				}
			}

			// 如果还能获取到chlid的父节点，证明出了异常,进行强制处理
			if (null != child.getParent()) {
				ILog.e(TAG,
						"the view's parent is exist!!!,but should be not exist.:"
								+ child);
				return false;
			}
		}

		return true;
	}

	private boolean removeChildView(ViewGroup vg, View child) {
		for (int i = 0; i < vg.getChildCount(); i++) {
			if (vg.getChildAt(i) == child) {
				vg.removeView(child);
				return true;
			}
			if (vg.getChildAt(i) instanceof ViewGroup) {
				if (removeChildView((ViewGroup) vg.getChildAt(i), child)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 销毁该单例，让系统回收
	 */
	public static void destory() {
		ILog.d(TAG, "destory the ViewFactory......");
		instance = null;
	}
}

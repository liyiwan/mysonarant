package com.yizi.iwuse.user.view;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.AppParams;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.utils.ILog;
import com.yizi.iwuse.common.utils.IWuseUtil;
import com.yizi.iwuse.constants.UserConst;
import com.yizi.iwuse.general.MainHomeActivity;
import com.yizi.iwuse.user.AboutActivity;
import com.yizi.iwuse.user.CallServiceActivity;
import com.yizi.iwuse.general.MainHomeActivity;
import com.yizi.iwuse.order.OrderListActivity;
import com.yizi.iwuse.user.AddresManageActivity;
import com.yizi.iwuse.user.LoginActivity;
import com.yizi.iwuse.user.SettingActivity;
import com.yizi.iwuse.user.ShoppingCartActivity;
import com.yizi.iwuse.user.SubmissionActivity;
import com.yizi.iwuse.user.UserActivity;
import com.yizi.iwuse.user.UserCollectionActivity;
import com.yizi.iwuse.user.WalletActivity;
import com.yizi.iwuse.user.model.ThridUserInfo;
import com.yizi.iwuse.user.service.events.UserEvents;

import de.greenrobot.event.EventBus;

/****
 * 用户管理中心
 * 
 * @author zhangxiying
 *
 */
public class UserFragment extends Fragment {

	private static final String TAG = "UserFragment";
	/***关闭**/
	@ViewInject(R.id.tv_close)
	private ImageView tv_close;
	/***用户信息，头像，昵称，积分**/
	@ViewInject(R.id.lay_userinfo)
	private RelativeLayout lay_userinfo;
	/**用户头像***/
	@ViewInject(R.id.img_usercenter_headimg)
	private ImageView img_usercenter_headimg;
	/**用户昵称**/
	@ViewInject(R.id.txt_usercenter_username)
	private TextView txt_usercenter_username;
	/**积分**/
	@ViewInject(R.id.text_usercenter_totalintegral)
	private TextView  text_usercenter_totalintegral;
	/**收藏**/
	@ViewInject(R.id.lay_usercollection)
	private LinearLayout lay_usercollection;
	/**购物车**/
	@ViewInject(R.id.lay_shopcart)
	private  LinearLayout lay_shopcart;
	/**钱包**/
	@ViewInject(R.id.lay_iwallet)
	private LinearLayout lay_iwallet;
	/**我的订单**/
	@ViewInject(R.id.txt_usercenter_iorderlist)
	private TextView txt_usercenter_iorderlist;
	/**地址管理**/
	@ViewInject(R.id.txt_usercenter_address)
	private TextView txt_usercenter_address;
	/**客服**/
	@ViewInject(R.id.txt_usercenter_callservice)
	private TextView txt_usercenter_callservice;
	/**上传稿件**/
	@ViewInject(R.id.txt_usercenter_isubmission)
	private TextView txt_usercenter_isubmission;
	/**设置**/
	@ViewInject(R.id.txt_usercenter_setting)
	private TextView txt_usercenter_setting;
	/**关于**/
	@ViewInject(R.id.txt_usercenter_about)
	private TextView txt_usercenter_about;
	
	BitmapUtils bitmapUtils = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_usercenter, null);
		ILog.i(TAG, "init onCreateView ");
		ViewUtils.inject(UserFragment.this, view);
		bitmapUtils = new BitmapUtils(getActivity());
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		tv_close = (ImageView)view.findViewById(R.id.tv_close);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tv_close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainHomeActivity) getActivity()).closeUserCenter();
			}
		});
	}

	/****
	 * 与用户登录强相关的操作按钮事件
	 * 
	 * @param view
	 */
	@OnClick({R.id.img_usercenter_headimg,R.id.lay_usercollection,R.id.lay_iwallet,
		R.id.txt_usercenter_iorderlist,R.id.txt_usercenter_callservice,R.id.txt_usercenter_isubmission,
		R.id.txt_usercenter_address})
	public void onUserCenterClick(View view){
		// 防止快速点击
		if(IWuseUtil.isFastDoubleClick()){
			return ;
		}
		ILog.i(TAG, "check login ");
		//先校验用户是否登录
		if(!AppParams.isLogin){
			ILog.i(TAG, " user have not login !");
			Intent mIntent = new Intent(AppContext.instance().globalContext,LoginActivity.class);
			this.startActivity(mIntent);
			return ;
		}
		Intent mIntent = null;
		
		switch(view.getId()){
			//用户信息
			case R.id.img_usercenter_headimg:
				mIntent = new Intent(AppContext.instance().globalContext, UserActivity.class);
				startActivity(mIntent);
				break;
			//我的收藏
			case R.id.lay_usercollection:
				mIntent = new Intent(getActivity(), UserCollectionActivity.class);
				startActivityForResult(mIntent, MainHomeActivity.RESULT_EMPTY_REQUEST);
				break;
			
			//我的钱包
			case R.id.lay_iwallet:
				mIntent = new Intent(AppContext.instance().globalContext, WalletActivity.class);
				startActivityForResult(mIntent, MainHomeActivity.RESULT_EMPTY_REQUEST);
				break;
			//我的订单
			case R.id.txt_usercenter_iorderlist:
				mIntent = new Intent(AppContext.instance().globalContext, OrderListActivity.class);
				startActivity(mIntent);
				break;
			case R.id.txt_usercenter_address:
				mIntent = new Intent(AppContext.instance().globalContext, AddresManageActivity.class);
				startActivity(mIntent);
				break;
			//物色客服
			case R.id.txt_usercenter_callservice:
				mIntent = new Intent(AppContext.instance().globalContext, CallServiceActivity.class);
				startActivity(mIntent);
				break;
			//上传投稿
			case R.id.txt_usercenter_isubmission:
				mIntent = new Intent(AppContext.instance().globalContext, SubmissionActivity.class);
				startActivity(mIntent);
				break;
			default :break;
		}
	}
	
	/***
	 * 跟系统设置强相关按钮点击事件
	 * 
	 * @param view
	 */
	@OnClick({R.id.lay_shopcart,R.id.txt_usercenter_setting,R.id.txt_usercenter_about})
	public void handlSettingsClick(View view){
		// 防止快速点击
		if(IWuseUtil.isFastDoubleClick()){
			return ;
		}
		Intent mIntent = null;
		switch(view.getId()){
			case R.id.txt_usercenter_setting:
				mIntent = new Intent(AppContext.instance().globalContext, SettingActivity.class);
				startActivity(mIntent);
				break;
				//我的购物车
			case R.id.lay_shopcart:
				mIntent = new Intent(AppContext.instance().globalContext, ShoppingCartActivity.class);
				startActivity(mIntent);
				break;
			case R.id.txt_usercenter_about:
				mIntent = new Intent(AppContext.instance().globalContext, AboutActivity.class);
				startActivity(mIntent);
				break;
			default :break;
		}
	}
	
	
	/****
	 * 用户监后台事件
	 * 
	 * @param userEvent
	 */
	public void onEvent(UserEvents userEvent) {
		switch (userEvent.eventtype) {
			case UserConst.ENVENTTYPE_LOGIN:
				//Toast.makeText(mContext, mContext.getString(R.string.login), Toast.LENGTH_LONG).show();
				ThridUserInfo userInfo = (ThridUserInfo)userEvent.customObj;
				ILog.i(TAG, "userInfo.nickName==="+userInfo.headUrl);
				txt_usercenter_username.setText(userInfo.nickName);
				bitmapUtils.display(img_usercenter_headimg, userInfo.headUrl);
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageEnd(TAG); 
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == MainHomeActivity.RESULT_EMPTY_REQUEST && resultCode == Activity.RESULT_OK){
			FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
			transaction.hide(this);
			transaction.commitAllowingStateLoss();
		}
	}
	
}

package com.yizi.iwuse.user;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnFocusChange;
import com.umeng.analytics.MobclickAgent;
import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.BaseActivity;
import com.yizi.iwuse.constants.GeneralConst;
import com.yizi.iwuse.constants.UserConst;
import com.yizi.iwuse.general.service.events.AddressEditEvent;
import com.yizi.iwuse.user.service.events.UserEvents;
import com.yizi.iwuse.user.view.AddrSelectPopupWindow;

import de.greenrobot.event.EventBus;

public class AddressEditActivity extends BaseActivity {

	@ViewInject(R.id.img_back)
	private ImageView img_back;
	@ViewInject(R.id.txt_address_contactname)
	private EditText txt_address_contactname;
	@ViewInject(R.id.txt_address_contactphone)
	private EditText txt_address_contactphone;
	@ViewInject(R.id.txt_address_zipcode)
	private EditText txt_address_zipcode;
	@ViewInject(R.id.txt_address_mainaddr)
	private TextView txt_address_mainaddr;
	@ViewInject(R.id.txt_address_subaddr)
	private EditText txt_address_subaddr;
	@ViewInject(R.id.btn_address_confirm)
	private Button btn_address_confirm;
	
	private View mView = null;
	
	private AddrSelectPopupWindow mAddrSelectPopupWindow = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mView = LayoutInflater.from(this).inflate(R.layout.layout_addresedit, null);
		this.setContentView(mView);
		/** 注册xUtils UI框架 **/
		ViewUtils.inject(this);
		/** 注册EventBus **/
		EventBus.getDefault().register(this);
	}

	
	/****
	 * 点击事件
	 * 
	 * @param view
	 */
	@OnClick({R.id.img_back,R.id.txt_address_mainaddr,
		R.id.btn_address_confirm})
	public void handeClickListener(View view){
		switch(view.getId()){
			case R.id.img_back:
				 finish();
				break;
			case R.id.txt_address_mainaddr:
				mAddrSelectPopupWindow = AddrSelectPopupWindow.getInstance(AddressEditActivity.this);
				mAddrSelectPopupWindow.showAtLocation(mView.findViewById(R.id.lay_addredit), Gravity.BOTTOM, 0, 0);	
				break;
			case R.id.btn_address_confirm:
				break;
			default :break;
		}
	}
	
//	@OnFocusChange({R.id.txt_address_mainaddr})
//	public void handlFocusListener(View view, boolean hasFocus){
//		switch(view.getId()){
//			case R.id.txt_address_mainaddr:
//				InputMethodManager imm = (InputMethodManager) AddressEditActivity.this 
//				.getSystemService(Context.INPUT_METHOD_SERVICE); 
//				imm.hideSoftInputFromWindow(txt_address_mainaddr.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
////				if(hasFocus){
//					mAddrSelectPopupWindow = AddrSelectPopupWindow.getInstance(AddressEditActivity.this);
//					mAddrSelectPopupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);	
////				}else{
////					if(mAddrSelectPopupWindow!=null && mAddrSelectPopupWindow.isShowing()){
////						mAddrSelectPopupWindow.dismiss();
////					}
////				}
//				break;
//		}
//	}
	

	/****
	 * 用户监后台事件
	 * 
	 * @param userEvent
	 */
	public void onEventMainThread(AddressEditEvent addressEvent) {
		switch (addressEvent.eventType) {
		case GeneralConst.EVENTTYPE_EDITADDR:
			txt_address_mainaddr.setText(addressEvent.address);
			break;
		}
	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	
	
	@Override
	public void removeAllView() {
			
	}

}

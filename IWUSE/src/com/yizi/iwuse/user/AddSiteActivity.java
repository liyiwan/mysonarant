package com.yizi.iwuse.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.BaseActivity;
import com.yizi.iwuse.common.utils.IWuseUtil;
import com.yizi.iwuse.constants.GeneralConst;
import com.yizi.iwuse.general.service.events.AddressEditEvent;
import com.yizi.iwuse.order.SubmitOrderActivity;
import com.yizi.iwuse.order.model.OrderSubItem;
import com.yizi.iwuse.user.view.AddrSelectPopupWindow;

import de.greenrobot.event.EventBus;

/**		用户没有登录，进入添加地址页面
 * 		或者用户有登录，但没有地址信息，则进入添加地址页面
 * @author hehaodong
 *
 */
public class AddSiteActivity extends BaseActivity {

	/***返回按钮***/
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	/***标题***/
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.btn_right)
	private Button btn_right;
	@ViewInject(R.id.tv_address_mainaddr)
	private TextView tv_address_mainaddr;
	private AddrSelectPopupWindow mAddrSelectPopupWindow = null;
	@ViewInject(R.id.et_consignee)
	private EditText et_consignee;
	@ViewInject(R.id.et_phone_num)
	private EditText et_phone_num;
	@ViewInject(R.id.et_security_code)
	private EditText et_security_code;
	@ViewInject(R.id.et_address_zipcode)
	private EditText et_address_zipcode;
	@ViewInject(R.id.et_address_subaddr)
	private EditText et_address_subaddr;
	private OrderSubItem orderSubItem;
	
	@Override
	public void removeAllView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		mView = LayoutInflater.from(this).inflate(R.layout.layout_addsite, null);
		setContentView(R.layout.layout_addsite);
		/** 注册xUtils UI框架 **/
		ViewUtils.inject(this);
		/** 注册EventBus **/
		EventBus.getDefault().register(this);
		initView();
	}
	
	private void initView(){
		int sumPrice = getIntent().getIntExtra("sumPrice", 0);
		orderSubItem = new OrderSubItem();
		orderSubItem.moneyCount = sumPrice;
		tv_title.setText(R.string.add_site);
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setText(R.string.add);
	}
	@OnClick({R.id.btn_back,R.id.tv_address_mainaddr,R.id.btn_right})
	public void handeClickListener(View view){
		switch(view.getId()){
			case R.id.btn_back:
				finish();
				break;
			case R.id.tv_address_mainaddr:
				mAddrSelectPopupWindow = AddrSelectPopupWindow.getInstance(AddSiteActivity.this);
				mAddrSelectPopupWindow.showAtLocation(findViewById(R.id.lay_addredit), Gravity.BOTTOM, 0, 0);	
				break;
			case R.id.btn_right:
				if(IWuseUtil.editTextIsNull(et_consignee)
						|| IWuseUtil.editTextIsNull(et_phone_num)
						|| IWuseUtil.editTextIsNull(et_security_code)
						|| IWuseUtil.editTextIsNull(et_address_zipcode)
						|| IWuseUtil.editTextIsNull(et_address_subaddr)){
					Toast.makeText(AddSiteActivity.this, R.string.input_all_please, Toast.LENGTH_LONG).show();
					return;
				}
				String address_mainaddr = tv_address_mainaddr.getText().toString().trim();
				if("".equals(address_mainaddr)){
					Toast.makeText(AddSiteActivity.this, R.string.input_all_please, Toast.LENGTH_LONG).show();
					return;
				}
				String consignee = et_consignee.getText().toString().trim();
				String phone_num = et_phone_num.getText().toString().trim();
				String security_code = et_security_code.getText().toString().trim();
				String address_zipcode = et_address_zipcode.getText().toString().trim();
				String address_subaddr = et_address_subaddr.getText().toString().trim();
				
				orderSubItem.userName = consignee;
				orderSubItem.phoneNum = phone_num;
				orderSubItem.userSite = address_mainaddr + address_subaddr;
				Intent intent = new Intent(AddSiteActivity.this,SubmitOrderActivity.class);
				intent.putExtra("OrderItem", orderSubItem);
				startActivity(intent);
				break;
		}
	}
	
	/****
	 * 用户监后台事件
	 * 
	 * @param userEvent
	 */
	public void onEventMainThread(AddressEditEvent addressEvent) {
		switch (addressEvent.eventType) {
		case GeneralConst.EVENTTYPE_EDITADDR:
			tv_address_mainaddr.setText(addressEvent.address);
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}

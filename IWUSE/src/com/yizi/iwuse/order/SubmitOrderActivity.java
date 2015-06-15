package com.yizi.iwuse.order;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.BaseActivity;
import com.yizi.iwuse.order.model.OrderSubItem;

/**		提交订单
 * @author hehaodong
 *
 */
public class SubmitOrderActivity extends BaseActivity {

	/***返回按钮***/
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	/***标题***/
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	/***订单对象***/
	private OrderSubItem orderSubItem;
	/***用户姓名***/
	@ViewInject(R.id.tv_name)
	private TextView tv_name;
	/***用户电话***/
	@ViewInject(R.id.tv_phone_num)
	private TextView tv_phone_num;
	/***用户地址***/
	@ViewInject(R.id.tv_user_site)
	private TextView tv_user_site;
	/***商品金额***/
	@ViewInject(R.id.tv_product_count)
	private TextView tv_product_count;
	/***待支付金额***/
	@ViewInject(R.id.tv_should_pay)
	private TextView tv_should_pay;
	/***提交订单按钮***/
	@ViewInject(R.id.btn_submit)
	private Button btn_submit;
	
	@Override
	public void removeAllView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_submit_order);
		/** 注册xUtils UI框架 **/
		ViewUtils.inject(this);
		initView();
		
	}
	
	private void initView(){
		tv_title.setText(R.string.end_count);
		orderSubItem = (OrderSubItem)getIntent().getSerializableExtra("OrderItem");
		if(orderSubItem!=null){
			tv_name.setText(orderSubItem.userName);
			tv_phone_num.setText(orderSubItem.phoneNum);
			tv_user_site.setText(orderSubItem.userSite);
			tv_product_count.setText(String.valueOf(orderSubItem.moneyCount));
			String shouldPay = String.format(getString(R.string.should_pay_count), orderSubItem.moneyCount);
			tv_should_pay.setText(shouldPay);
		}
	}
	
	/**		事件监听
	 * @param view
	 */
	@OnClick({R.id.btn_back})
	public void handeClickListener(View view){
		switch(view.getId()){
			case R.id.btn_back:
				finish();
				break;
		}
	}

}

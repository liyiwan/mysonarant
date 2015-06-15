package com.yizi.iwuse.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.TransactionTooLargeException;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.BaseActivity;
import com.yizi.iwuse.constants.OrderConst;
import com.yizi.iwuse.order.adapter.OrderDetailAdapter;
import com.yizi.iwuse.order.model.OrderMainInfo;
import com.yizi.iwuse.order.service.events.OrderEvents;
import com.yizi.iwuse.order.view.OrderStatusView;

import de.greenrobot.event.EventBus;

@SuppressLint("NewApi")
public class OrderDetailActivity extends BaseActivity {

	/**返回*/
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	/**订单编号**/
	@ViewInject(R.id.txt_orderid)
	private TextView txt_orderid;
	/**下单时间**/
	@ViewInject(R.id.txt_ordertime)
	private TextView txt_ordertime;
	/**订单状态**/
	@ViewInject(R.id.img_order_status)
	private RelativeLayout img_order_status;
	/**订单总金额**/
	@ViewInject(R.id.txt_orderamount)
	private TextView txt_orderamount;
	/**产品列表**/
	@ViewInject(R.id.list_productdetail)
	private ListView list_productdetail;
	/**付款**/
	@ViewInject(R.id.btn_orderpay)
	private Button btn_orderpay;
	/**取消订单**/
	@ViewInject(R.id.btn_ordercancel)
	private Button btn_ordercancel;
	/**删除订单**/
	@ViewInject(R.id.btn_orderdelete)
	private Button btn_orderdelete;
	/**查看物流**/
	@ViewInject(R.id.btn_orderlogistics)
	private Button btn_orderlogistics;
	/**退款**/
	@ViewInject(R.id.btn_orderrefund)
	private Button btn_orderrefund;
	
	
	
	private OrderDetailAdapter orderDetailAdapter = null;
	private OrderMainInfo orderMain = null;
	private String orderId = "";
	private OrderStatusView orderStatusView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 orderMain = (OrderMainInfo)getIntent().getSerializableExtra("orderMain");
		this.setContentView(R.layout.layout_orderdetail);
		/** 注册xUtils UI框架 **/
		ViewUtils.inject(this);
		/** 注册EventBus **/
		EventBus.getDefault().register(this);
		this.initData();
	}
	
	public void initData(){
		 tv_title.setText(R.string.order_detail);
		 AppContext.instance().orderService.getOrderDetail(orderMain.orderId+"");
	}
	

	/****
	 * 点击事件
	 * 
	 * @param view
	 */
	@OnClick({R.id.btn_back,R.id.btn_orderpay,R.id.btn_ordercancel,R.id.btn_orderlogistics,R.id.btn_orderdelete,R.id.btn_orderrefund})
	public void handeClickListener(View view){
		Intent mIntent = null;
		switch(view.getId()){
			case R.id.btn_back:
				 finish();
				break;
			case R.id.btn_orderpay:
				mIntent = new Intent(OrderDetailActivity.this,SubmitOrderActivity.class);
				mIntent.putExtra("order", orderMain);
				this.startActivity(mIntent);
				break;
			case R.id.btn_orderlogistics:
				mIntent = new Intent(OrderDetailActivity.this,LookTransportActivity.class);
				mIntent.putExtra("order", orderMain);
				this.startActivity(mIntent);
				break;
			case R.id.btn_ordercancel:
				break;
			case R.id.btn_orderdelete:
				break;
			case R.id.btn_orderrefund:
				break;
			default:break;
		}
	}
	
	/****
	 * 用户监后台事件
	 * 
	 * @param userEvent
	 */
	public void onEventMainThread(OrderEvents orderEvent) {
		switch (orderEvent.eventType) {
		//获取订单列表
		case OrderConst.EVENTTYPE_ORDERDETAIL:
			if(orderEvent.customerObj!=null){
				OrderMainInfo orderMainInfo = (OrderMainInfo)orderEvent.customerObj;
				txt_orderid.setText(orderMainInfo.orderId+"");
				txt_ordertime.setText(orderMainInfo.orderTime+"");
				
				Display display = img_order_status.getDisplay();
				orderStatusView = new OrderStatusView(OrderDetailActivity.this);
				orderStatusView.setInfo(display, orderMainInfo.orderStatusCode);
				img_order_status.addView(orderStatusView);
				
				txt_orderamount.setText(orderMainInfo.amount+"");
				
				orderDetailAdapter = new OrderDetailAdapter(OrderDetailActivity.this, orderMainInfo.orderSubList);
				list_productdetail.setAdapter(orderDetailAdapter);
				orderDetailAdapter.notifyDataSetChanged();
			}
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

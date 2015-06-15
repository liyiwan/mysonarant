package com.yizi.iwuse.order;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.BaseActivity;
import com.yizi.iwuse.constants.OrderConst;
import com.yizi.iwuse.order.adapter.OrderListAdapter;
import com.yizi.iwuse.order.service.events.OrderEvents;

import de.greenrobot.event.EventBus;

public class OrderListActivity extends BaseActivity {

	/**返回*/
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	
	@ViewInject(R.id.list_orderlist)
	private ListView list_orderlist;
	
	private OrderListAdapter orderListAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_orderlist);
		/** 注册xUtils UI框架 **/
		ViewUtils.inject(this);
		/** 注册EventBus **/
		EventBus.getDefault().register(this);
		this.initData();
	}
	
	public void initData(){
		 tv_title.setText(R.string.usercenter_iorderlist);
		 AppContext.instance().orderService.getOrderList();
	}
	

	/****
	 * 点击事件
	 * 
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
	
	/****
	 * 用户监后台事件
	 * 
	 * @param userEvent
	 */
	public void onEventMainThread(OrderEvents orderEvent) {
		switch (orderEvent.eventType) {
		//获取订单列表
		case OrderConst.EVENTTYPE_ORDERLIST:
			if(orderEvent.orderList!=null && !orderEvent.orderList.isEmpty()){
				orderListAdapter = new OrderListAdapter(OrderListActivity.this, orderEvent.orderList);
				list_orderlist.setAdapter(orderListAdapter);
				orderListAdapter.notifyDataSetChanged();
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

package com.yizi.iwuse.order;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.BaseActivity;
import com.yizi.iwuse.constants.OrderConst;
import com.yizi.iwuse.constants.UserConst;
import com.yizi.iwuse.order.model.OrderMainInfo;
import com.yizi.iwuse.order.model.TransportItem;
import com.yizi.iwuse.order.service.OrderService;
import com.yizi.iwuse.order.service.events.OrderEvents;
import com.yizi.iwuse.user.model.ShoppingItem;
import com.yizi.iwuse.user.service.events.UserEvents;

import de.greenrobot.event.EventBus;

/**		查看物流
 * @author hehaodong
 *
 */
public class LookTransportActivity extends BaseActivity {

	/***返回按钮***/
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	/***标题***/
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	private OrderMainInfo orderInfo;
	@ViewInject(R.id.tv_orderid)
	private TextView tv_orderid;
	@ViewInject(R.id.tv_orderamount)
	private TextView tv_orderamount;
	@ViewInject(R.id.tv_orderstatus)
	private TextView tv_orderstatus;
	@ViewInject(R.id.tv_ordertime)
	private TextView tv_ordertime;
	private ArrayList<TransportItem> transportArray;
	private TransportAdapter transportAdapter;
	@ViewInject(R.id.lv_transport)
	private ListView lv_transport;
		
	@Override
	public void removeAllView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_looktransport);
		ViewUtils.inject(this);
		/** 注册EventBus **/
		EventBus.getDefault().register(this);
		initView();
	}
	
	private void initView(){
		orderInfo = (OrderMainInfo)getIntent().getSerializableExtra("order");
		tv_title.setText(R.string.order_logistics);
		tv_orderid.setText(String.valueOf(orderInfo.orderId));
		tv_orderamount.setText(String.valueOf(orderInfo.amount));
		tv_orderstatus.setText(orderInfo.orderStatus);
		tv_ordertime.setText(orderInfo.orderTime);
		transportArray = new ArrayList<TransportItem>();
		transportAdapter = new TransportAdapter();
		lv_transport.setAdapter(transportAdapter);
		OrderService orderService = AppContext.instance().orderService;
		orderService.lookTransport();
	}
	
	private class TransportAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return transportArray==null?0:transportArray.size();
        }

        @Override
        public Object getItem(int i) {
            return transportArray.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

		@Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            TransportItem transportItem = transportArray.get(position); 
            if(view == null){
            	viewHolder = new ViewHolder();
            	view = LayoutInflater.from(LookTransportActivity.this).inflate(R.layout.transport_item, null);
            	viewHolder.ll_bottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
            	viewHolder.tv_transport_site = (TextView) view.findViewById(R.id.tv_transport_site);
            	viewHolder.tv_transport_time = (TextView) view.findViewById(R.id.tv_transport_time);
            	view.setTag(viewHolder);
            }else{
            	viewHolder = (ViewHolder)view.getTag();
            }
            
            viewHolder.tv_transport_site.setText(transportItem.transportSite);
            viewHolder.tv_transport_time.setText(transportItem.transportTime);
            if(position == (transportArray.size() - 1)){
            	viewHolder.ll_bottom.setVisibility(View.GONE);
            }else{
            	viewHolder.ll_bottom.setVisibility(View.VISIBLE);
            }
            return view;
        }

        private class ViewHolder {
        	private LinearLayout ll_bottom;
        	private TextView tv_transport_site;
        	private TextView tv_transport_time;
        }
        
    }
	
	@OnClick({R.id.btn_back})
	public void handeClickListener(View view){
		switch(view.getId()){
		case R.id.btn_back:
			finish();
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	/****
	 * 用户监后台事件
	 * 
	 * @param userEvent
	 */
	public void onEventMainThread(OrderEvents orderEvent) {
		switch (orderEvent.eventType) {
		case OrderConst.EVENTTYPE_LOOK_TRANSPORT:
			transportArray = (ArrayList<TransportItem>)orderEvent.customerObj;
			transportAdapter.notifyDataSetChanged();
			break;
		}
	}

}

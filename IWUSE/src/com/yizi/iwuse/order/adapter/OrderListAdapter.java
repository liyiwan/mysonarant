package com.yizi.iwuse.order.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.yizi.iwuse.R;
import com.yizi.iwuse.order.LookTransportActivity;
import com.yizi.iwuse.order.OrderDetailActivity;
import com.yizi.iwuse.order.SubmitOrderActivity;
import com.yizi.iwuse.order.model.OrderMainInfo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/***
 * 订单列表适配器
 * 
 * @author zhangxiying
 *
 */
public class OrderListAdapter extends BaseAdapter {
	private static final String TAG = "OrderListAdapter";
	
	private Context mContext;
	
	private List<OrderMainInfo> orderList;
	BitmapUtils bitmapUtils = null;
	
	public OrderListAdapter(Context context,List<OrderMainInfo> orderList){
		this.mContext = context;
		this.orderList = orderList;
		bitmapUtils = new BitmapUtils(mContext);
	}
	
	@Override
	public int getCount() {
		return orderList.size();
	}

	@Override
	public Object getItem(int position) {
		return orderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OrderItemHolder orderItem = null;
		//初始化界面
		if(convertView==null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.orderlist_item, null);
			orderItem = new OrderItemHolder();
			orderItem.txt_orderid =(TextView)convertView.findViewById(R.id.txt_orderid);
			orderItem.img_order_thumb = (ImageView) convertView.findViewById(R.id.img_order_thumb);
			orderItem.txt_orderamount = (TextView)convertView.findViewById(R.id.txt_orderamount);
			orderItem.txt_orderstatus = (TextView) convertView.findViewById(R.id.txt_orderstatus);
			orderItem.txt_ordertime = (TextView)convertView.findViewById(R.id.txt_ordertime);
			orderItem.img_orderdetail = (ImageView)convertView.findViewById(R.id.img_orderdetail);
			orderItem.btn_ordercancel = (Button)convertView.findViewById(R.id.btn_ordercancel);
			orderItem.btn_orderpay = (Button)convertView.findViewById(R.id.btn_orderpay);
			orderItem.btn_orderdelete = (Button)convertView.findViewById(R.id.btn_orderdelete);
			orderItem.btn_orderlogistics = (Button)convertView.findViewById(R.id.btn_orderlogistics);
			//将View做缓存
			convertView.setTag(orderItem);
		}else{
			orderItem = (OrderItemHolder) convertView.getTag();
		}
		
		final OrderMainInfo orderMainInfo = orderList.get(position);
		
		bitmapUtils.display(orderItem.img_order_thumb, orderMainInfo.orderThrumUri);
		orderItem.txt_orderid.setText(orderMainInfo.orderId+"");
		
		orderItem.txt_orderamount.setText(orderMainInfo.amount+"");
		orderItem.txt_ordertime.setText(orderMainInfo.orderTime+"");
		orderItem.txt_orderstatus.setText(orderMainInfo.orderStatus+"");
		
		orderItem.img_orderdetail.setTag(orderMainInfo);
		
		orderItem.img_orderdetail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OrderMainInfo orderMain = (OrderMainInfo)v.getTag();
				Intent mIntent = new Intent(mContext,OrderDetailActivity.class);
				mIntent.putExtra("orderMain", orderMain);
				mContext.startActivity(mIntent);
			}
		});
		
		orderItem.btn_orderlogistics.setTag(orderMainInfo);
		orderItem.btn_orderlogistics.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//OrderMainInfo orderMain = (OrderMainInfo)v.getTag();
				Intent mIntent = new Intent(mContext,LookTransportActivity.class);
				mIntent.putExtra("order", orderMainInfo);
				mContext.startActivity(mIntent);
			}
		});
		
		orderItem.btn_orderpay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(mContext,SubmitOrderActivity.class);
				mIntent.putExtra("order", orderMainInfo);
				mContext.startActivity(mIntent);
			}
		});
		
		return convertView;
	}
	
	
	
	/***
	 * 订单列表中元素对象
	 * 
	 * @author zhangxiying
	 *
	 */
	static class OrderItemHolder{
		public TextView txt_orderid;
		public ImageView img_order_thumb;
		public TextView txt_orderamount;
		public TextView txt_orderstatus;
		public TextView txt_ordertime;
		public ImageView img_orderdetail;
		public Button btn_ordercancel;
		public Button btn_orderpay;
		public Button btn_orderdelete;
		public Button btn_orderlogistics;
	}

}

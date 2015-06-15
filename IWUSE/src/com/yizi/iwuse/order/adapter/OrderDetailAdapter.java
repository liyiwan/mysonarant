package com.yizi.iwuse.order.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.yizi.iwuse.R;
import com.yizi.iwuse.order.model.OrderSubInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/***
 * 订单商品详情适配器
 * 
 * @author zhangxiying
 *
 */
public class OrderDetailAdapter extends BaseAdapter {

	private Context mContext;
	private List<OrderSubInfo> orderSubList;
	private BitmapUtils bitmapUtils = null;
	
	public OrderDetailAdapter(Context mContext,List<OrderSubInfo> orderSubList){
		super();
		this.mContext = mContext;
		this.orderSubList = orderSubList;
		bitmapUtils = new BitmapUtils(mContext);
	}
	
	@Override
	public int getCount() {
		return orderSubList.size();
	}

	@Override
	public Object getItem(int position) {
		return orderSubList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OrderDetailHolder orderDetailHolder = null;
		//初始化界面
		if(convertView==null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.orderdetail_item, null);
			orderDetailHolder = new OrderDetailHolder();
			orderDetailHolder.img_product_uri =(ImageView)convertView.findViewById(R.id.img_product_uri);
			orderDetailHolder.txt_product_name = (TextView) convertView.findViewById(R.id.txt_product_name);
			orderDetailHolder.txt_product_desc = (TextView)convertView.findViewById(R.id.txt_product_desc);
			orderDetailHolder.txt_productamount = (TextView) convertView.findViewById(R.id.txt_productamount);
			orderDetailHolder.txt_product_quantity = (TextView)convertView.findViewById(R.id.txt_product_quantity);
			orderDetailHolder.btn_color = (Button)convertView.findViewById(R.id.btn_color);
			orderDetailHolder.btn_material = (Button)convertView.findViewById(R.id.btn_material);
			orderDetailHolder.btn_size = (Button)convertView.findViewById(R.id.btn_size);
			//将View做缓存
			convertView.setTag(orderDetailHolder);
		}else{
			orderDetailHolder = (OrderDetailHolder) convertView.getTag();
		}
		
		OrderSubInfo orderSubInfo = orderSubList.get(position);
		
		bitmapUtils.display(orderDetailHolder.img_product_uri, orderSubInfo.productUri);
		orderDetailHolder.txt_product_name.setText(orderSubInfo.productName+"");
		orderDetailHolder.txt_product_desc.setText(orderSubInfo.productDesc+"");
		orderDetailHolder.txt_productamount.setText(orderSubInfo.productAmount+"");
		orderDetailHolder.txt_product_quantity.setText(orderSubInfo.quantity+"");
		if(orderSubInfo.color==null||"".equals(orderSubInfo.color)){
			orderDetailHolder.btn_color.setVisibility(View.GONE);
		}else{
			orderDetailHolder.btn_color.setText(orderSubInfo.color);
			orderDetailHolder.btn_color.setVisibility(View.VISIBLE);
		}
		if(orderSubInfo.material==null ||"".equals(orderSubInfo.material)){
			orderDetailHolder.btn_material.setVisibility(View.GONE);
		}else{
			orderDetailHolder.btn_material.setText(orderSubInfo.material);
			orderDetailHolder.btn_material.setVisibility(View.VISIBLE);
		}
		
		if(orderSubInfo.size==null||"".equals(orderSubInfo.size)){
			orderDetailHolder.btn_size.setVisibility(View.GONE);
		}else{
			orderDetailHolder.btn_size.setText(orderSubInfo.size);
			orderDetailHolder.btn_size.setVisibility(View.VISIBLE);
		}
		
		orderDetailHolder.img_product_uri.setTag(orderSubInfo);
		
		return convertView;
	}
	
	static class OrderDetailHolder{
		/**产品图片**/
		public ImageView img_product_uri;
		/**产品名称**/
		public TextView txt_product_name;
		/**产品描述**/
		public TextView txt_product_desc;
		/**颜色**/
		public Button btn_color;
		/**材质**/
		public Button btn_material;
		/**尺寸**/
		public Button btn_size;
		/**价格**/
		public TextView txt_productamount;
		/**数量**/
		public TextView txt_product_quantity;
		
	}

}

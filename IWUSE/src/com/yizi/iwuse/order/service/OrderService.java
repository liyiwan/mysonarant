package com.yizi.iwuse.order.service;

import java.util.ArrayList;
import java.util.List;

import com.yizi.iwuse.common.base.ICoreService;
import com.yizi.iwuse.constants.OrderConst;
import com.yizi.iwuse.framework.model.CmdResultInfo;
import com.yizi.iwuse.framework.service.CmdSendAdapter;
import com.yizi.iwuse.framework.service.MsgInterface.CmdInterface;
import com.yizi.iwuse.order.model.OrderMainInfo;
import com.yizi.iwuse.order.model.OrderSubInfo;
import com.yizi.iwuse.order.model.TransportItem;
import com.yizi.iwuse.order.service.events.OrderEvents;

import de.greenrobot.event.EventBus;

/***
 * 订单服务类
 * 
 * @author zhangxiying
 *
 */
public class OrderService implements ICoreService{

	@Override
	public boolean initState() {
		return true;
	}
	
	
	/****
	 * ex. 获取订单信息
	 * 
	 * @return
	 */
	public CmdResultInfo getProductInfo(){
	    
		// EventBus.getDefault().post(null);
		return CmdSendAdapter.sendCmd(CmdInterface.ORDER_OrderInfo, null);
	}
	/***
	 * 获取我的订单列表
	 * 
	 */
	public void getOrderList(){
		new Thread(){

			@Override
			public void run() {
				//CmdSendAdapter.sendCmd(CmdInterface.ORDER_OrderInfo, null);
				List<OrderMainInfo>  orderList = new ArrayList<OrderMainInfo>();
				OrderMainInfo orderMainInfo = new OrderMainInfo();
				orderMainInfo.orderId = 989213;
				orderMainInfo.amount = 899;
				orderMainInfo.orderStatus = "待付款";
				orderMainInfo.orderTime = "2015-06-08 17:04";
				orderMainInfo.orderThrumUri = "http://images.bdhome.cn/bdhomeimage/advertisingitem/21474836521/21474836521.jpg";
				
				orderList.add(orderMainInfo);
				
				OrderEvents orderEvents = new OrderEvents();
				orderEvents.eventType = OrderConst.EVENTTYPE_ORDERLIST;
				orderEvents.orderList  = orderList;
				EventBus.getDefault().post(orderEvents);
			}
		}.start();
	}
	/***
	 * 获取订单详情
	 * 
	 * @param orderid
	 */
	public void getOrderDetail(String orderid){
		new Thread(){

			@Override
			public void run() {
				//CmdSendAdapter.sendCmd(CmdInterface.ORDER_getorderinfo, orderid);
				OrderMainInfo orderMainInfo = new OrderMainInfo();
				orderMainInfo.orderId = 989213;
				orderMainInfo.amount = 7889;
				orderMainInfo.orderStatusCode = "10";
				orderMainInfo.orderStatus = "待付款";
				orderMainInfo.orderTime = "2015-06-08 17:04";
				orderMainInfo.orderThrumUri = "http://images.bdhome.cn/bdhomeimage/advertisingitem/21474836521/21474836521.jpg";
				
				OrderSubInfo orderSubInfo = new OrderSubInfo();
				
				orderSubInfo.productName = "进口画芯";
				orderSubInfo.productDesc = "印刷画 美国原版进口画芯 W755*H755mm";
				orderSubInfo.productUri = "http://images.bdhome.cn/bdhomeimage/advertisingitem/21474836521/21474836521.jpg";
				orderSubInfo.color = "";
				orderSubInfo.material = "木质、金属边框";
				orderSubInfo.size = "755*755mm";
				orderSubInfo.productPrice = 913D;
				orderSubInfo.productAmount = 913D;
				orderSubInfo.quantity = 1;
				
				orderMainInfo.orderSubList.add(orderSubInfo);
				
				orderSubInfo = new OrderSubInfo();
				orderSubInfo.productName = "休闲椅";
				orderSubInfo.productDesc = "休闲椅 布艺软包+不锈钢脚 W840*D810*H730 mm";
				orderSubInfo.productUri = "http://images.bdhome.cn/bdhomeimage/advertisingitem/21474836521/21474836521.jpg";
				orderSubInfo.color = "红";
				orderSubInfo.material = "布艺软包+不锈钢脚";
				orderSubInfo.size = "840*810*730 mm";
				orderSubInfo.productPrice = 6351D;
				orderSubInfo.productAmount = 6351D;
				orderSubInfo.quantity = 1;
				orderMainInfo.orderSubList.add(orderSubInfo);	
				
				OrderEvents orderEvent = new OrderEvents();
				orderEvent.eventType = OrderConst.EVENTTYPE_ORDERDETAIL;
				orderEvent.customerObj = orderMainInfo;
				EventBus.getDefault().post(orderEvent);
			}
		}.start();
	}
	
	public void lookTransport(){
		ArrayList<TransportItem> transportArray = new ArrayList<TransportItem>();
		TransportItem transportItem1 = new TransportItem("货品已经到达广州市海珠区琶堤","2015-05-20 11:30");
		transportArray.add(transportItem1);
		TransportItem transportItem2 = new TransportItem("货品已经到达广州货仓","2015-05-19 17:30");
		transportArray.add(transportItem2);
		TransportItem transportItem3 = new TransportItem("货品已经到达深圳货仓","2015-05-18 05:05");
		transportArray.add(transportItem3);
		TransportItem transportItem4 = new TransportItem("货品已经到达深圳福田办事点","2015-05-17 17:30");
		transportArray.add(transportItem4);
		TransportItem transportItem5 = new TransportItem("货品已出仓","2015-05-17 12:30");
		transportArray.add(transportItem5);
		OrderEvents orderEvent = new OrderEvents();
		orderEvent.eventType = OrderConst.EVENTTYPE_LOOK_TRANSPORT;
		orderEvent.customerObj = transportArray;
		EventBus.getDefault().post(orderEvent);
	}
	

}

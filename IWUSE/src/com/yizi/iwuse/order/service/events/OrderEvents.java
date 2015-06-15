package com.yizi.iwuse.order.service.events;

import java.util.List;

import com.yizi.iwuse.order.model.OrderMainInfo;

public class OrderEvents {
   /**事件类型**/
   public int eventType;
   public List<OrderMainInfo> orderList = null;
   /**传递参数**/
   public Object customerObj;
}

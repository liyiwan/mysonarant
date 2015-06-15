package com.yizi.iwuse.order.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderMainInfo implements Serializable{
	private static final long serialVersionUID = -6062903155883175932L;
	public int orderId;
	public double amount;
	public String orderStatusCode;
	public String orderStatus;
	public String orderTime;
	public String orderThrumUri;
	public List<OrderSubInfo> orderSubList = new ArrayList<OrderSubInfo>();
}

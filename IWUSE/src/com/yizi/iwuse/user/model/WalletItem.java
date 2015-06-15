package com.yizi.iwuse.user.model;

public class WalletItem {

	public int price;
	public String condition;
	public String endTime;
	public String ticketId;
	/***钱包购物券状态，0、已经过期  1、未过期***/
	public int state = 1;
	
	public WalletItem(int price,String condition,String endTime,String ticketId,int state){
		this.price = price;
		this.condition = condition;
		this.endTime = endTime;
		this.ticketId = ticketId;
		this.state = state;
	}
	
}

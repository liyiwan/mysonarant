package com.yizi.iwuse.user.model;

import com.lidroid.xutils.db.annotation.NoAutoIncrement;

public class ShoppingItem {
	@NoAutoIncrement
	public int id;
	public String productUrl;
	public String productName;
	public String productProperty;
	public int productPrice;
	public int productNum = 1;
	public String userId;
	public boolean isSelect = false;
	
	public ShoppingItem(){
		
	}
	
	public ShoppingItem(int id,String productUrl,String productName,String productProperty,int productPrice,int productNum,String userId){
		this.id = id;
		this.productUrl = productUrl;
		this.productName = productName;
		this.productProperty = productProperty;
		this.productPrice = productPrice;
		this.productNum = productNum;
		this.userId = userId;
	}
}

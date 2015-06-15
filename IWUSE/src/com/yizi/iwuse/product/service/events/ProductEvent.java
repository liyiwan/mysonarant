package com.yizi.iwuse.product.service.events;

import java.util.ArrayList;

import com.yizi.iwuse.product.model.ProductItem;

/**		单品事件
 * @author hehaodong
 *
 */
public class ProductEvent {
	/***单品数据***/
	private ArrayList<ProductItem> productArray;
	
	public ProductEvent(ArrayList<ProductItem> productArray){
		this.productArray = productArray;
	}

	public ArrayList<ProductItem> getProductArray() {
		return productArray;
	}

	public void setProductArray(ArrayList<ProductItem> productArray) {
		this.productArray = productArray;
	}
	
}

package com.yizi.iwuse.filter.service.events;

import java.util.ArrayList;

import com.yizi.iwuse.filter.model.ProductFilterItem;

/**		产品过滤事件
 * @author hehaodong
 *
 */
public class ProductFilterEvent {

	private ArrayList<ProductFilterItem> filterArray;

	public ArrayList<ProductFilterItem> getFilterArray() {
		return filterArray;
	}

	public void setFilterArray(ArrayList<ProductFilterItem> filterArray) {
		this.filterArray = filterArray;
	}
	
	
}

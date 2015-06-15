package com.yizi.iwuse.filter.model;

import java.util.ArrayList;

/**		产品过滤item
 * @author hehaodong
 *
 */
public class ProductFilterItem {

	public ArrayList<FilterSubItem> filterArray;
	public String filterName;

	
	public ProductFilterItem(String filterName,ArrayList<FilterSubItem> filterArray){
		this.filterName = filterName;
		this.filterArray = filterArray;
	}

}

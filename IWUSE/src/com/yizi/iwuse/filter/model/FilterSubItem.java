package com.yizi.iwuse.filter.model;

public class FilterSubItem {

	public String filterSubName;
	public boolean isSelect = false;
	public String filterParentName;
	
	public FilterSubItem(String filterSubName,String filterParentName){
		this.filterSubName = filterSubName;
		this.filterParentName = filterParentName;
	}
}

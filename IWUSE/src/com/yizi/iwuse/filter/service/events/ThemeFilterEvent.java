package com.yizi.iwuse.filter.service.events;

import java.util.ArrayList;

import com.yizi.iwuse.filter.model.WuseFilterItem;


public class ThemeFilterEvent {

	private ArrayList<WuseFilterItem> filterArray;

	public ArrayList<WuseFilterItem> getFilterArray() {
		return filterArray;
	}

	public void setFilterArray(ArrayList<WuseFilterItem> filterArray) {
		this.filterArray = filterArray;
	}
	
	
}

package com.yizi.iwuse.filter.service;

import java.util.ArrayList;

import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.ICoreService;
import com.yizi.iwuse.filter.model.FilterSubItem;
import com.yizi.iwuse.filter.model.ProductFilterItem;
import com.yizi.iwuse.filter.model.WuseFilterItem;
import com.yizi.iwuse.filter.service.events.ProductFilterEvent;
import com.yizi.iwuse.filter.service.events.ThemeFilterEvent;
import com.yizi.iwuse.product.service.events.ThemeEvent;

import de.greenrobot.event.EventBus;

public class FilterService implements ICoreService {

	/***主题过滤数据***/
	private ArrayList<WuseFilterItem> themeArray;
	/***单品过滤数据***/
	private ArrayList<ProductFilterItem> filterArray;
	
	@Override
	public boolean initState() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void getNetData() {
		filterArray = new ArrayList<ProductFilterItem>();
		ArrayList<FilterSubItem> filterArray1 = new ArrayList<FilterSubItem>();
		filterArray1.add(new FilterSubItem("厨房","房间"));
		filterArray1.add(new FilterSubItem("卫浴间","房间"));
		filterArray1.add(new FilterSubItem("卧房","房间"));
		filterArray1.add(new FilterSubItem("大厅","房间"));
		filterArray1.add(new FilterSubItem("户外","房间"));
		ProductFilterItem filter1 = new ProductFilterItem("房间",filterArray1);
		filterArray.add(filter1);
		ArrayList<FilterSubItem> filterArray2 = new ArrayList<FilterSubItem>();
		filterArray2.add(new FilterSubItem("美欧城市","风格"));
		filterArray2.add(new FilterSubItem("北欧简约","风格"));
		filterArray2.add(new FilterSubItem("日式淡雅","风格"));
		filterArray2.add(new FilterSubItem("东南亚度假","风格"));
		filterArray2.add(new FilterSubItem("华丽宫廷","风格"));
		filterArray2.add(new FilterSubItem("奢华现代","风格"));
		filterArray2.add(new FilterSubItem("美欧城市","风格"));
		filterArray2.add(new FilterSubItem("北欧简约","风格"));
		filterArray2.add(new FilterSubItem("日式淡雅","风格"));
		filterArray2.add(new FilterSubItem("东南亚度假","风格"));
		filterArray2.add(new FilterSubItem("华丽宫廷","风格"));
		filterArray2.add(new FilterSubItem("奢华现代","风格"));
		filterArray2.add(new FilterSubItem("美欧城市","风格"));
		filterArray2.add(new FilterSubItem("北欧简约","风格"));
		filterArray2.add(new FilterSubItem("日式淡雅","风格"));
		filterArray2.add(new FilterSubItem("东南亚度假","风格"));
		filterArray2.add(new FilterSubItem("华丽宫廷","风格"));
		filterArray2.add(new FilterSubItem("奢华现代","风格"));
		ProductFilterItem filter2 = new ProductFilterItem("风格",filterArray2);
		filterArray.add(filter2);
		ArrayList<FilterSubItem> filterArray3 = new ArrayList<FilterSubItem>();
		filterArray3.add(new FilterSubItem("沙发","类别"));
		filterArray3.add(new FilterSubItem("桌子","类别"));
		filterArray3.add(new FilterSubItem("椅子","类别"));
		filterArray3.add(new FilterSubItem("床","类别"));
		filterArray3.add(new FilterSubItem("柜子","类别"));
		filterArray3.add(new FilterSubItem("衣柜","类别"));
		filterArray3.add(new FilterSubItem("茶几","类别"));
		filterArray3.add(new FilterSubItem("灯饰","类别"));
		filterArray3.add(new FilterSubItem("饰品","类别"));
		ProductFilterItem filter3 = new ProductFilterItem("类别",filterArray3);
		filterArray.add(filter3);
		
	}
	
	public void doNetWork() {
		getNetData();
		ProductFilterEvent productFilterEvent = new ProductFilterEvent();
		productFilterEvent.setFilterArray(filterArray);
		EventBus.getDefault().post(productFilterEvent);
	}
	
	public void getThemeFilterData() {
		themeArray = new ArrayList<WuseFilterItem>();
		themeArray.add(new WuseFilterItem("All is iwuse"));
		themeArray.add(new WuseFilterItem("Life is iwuse"));
		themeArray.add(new WuseFilterItem("Video as iwuse"));
		themeArray.add(new WuseFilterItem("User as iwuse"));
	}
	
	public void doThemeFilterWork() {
		getThemeFilterData();
		ThemeFilterEvent themeFilterEvent = new ThemeFilterEvent();
		themeFilterEvent.setFilterArray(themeArray);
		EventBus.getDefault().post(themeFilterEvent);
	}

}

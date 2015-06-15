package com.yizi.iwuse.general.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/***
 * ViewPager 适配器
 * 
 * @author zhangxiying
 *
 */
public class ViewPagerAdapter extends PagerAdapter {

	private List<View> mViewList= null; 
	private Context mContext = null;
	
	public ViewPagerAdapter(Context context){
		mContext = context;
	}

	public ViewPagerAdapter(Context context,List<View> viewList){
		mContext = context;
		mViewList = viewList;
	}
	/***
	 * 删除页卡
	 * 
	 */
	@Override
    public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mViewList.get(position));
    }

	/***
	 * 实例化页卡
	 * 
	 */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    	//添加页卡
    	container.addView(mViewList.get(position), 0);     
        return mViewList.get(position); 
    }
	
	@Override
	public int getCount() {
		if(mViewList!=null && !mViewList.isEmpty()){
			return mViewList.size();
		}else{
			return 0;
		}
	}
	
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view==obj;
	}

}

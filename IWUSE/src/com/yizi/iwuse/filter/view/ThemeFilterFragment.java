package com.yizi.iwuse.filter.view;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextPaint;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.R;
import com.yizi.iwuse.filter.model.WuseFilterItem;
import com.yizi.iwuse.filter.service.FilterService;
import com.yizi.iwuse.filter.service.events.ThemeFilterEvent;
import com.yizi.iwuse.general.MainHomeActivity;
import com.yizi.iwuse.general.view.MainHomeFragment;

import de.greenrobot.event.EventBus;

/**		筛选主题页面
 * @author hehaodong
 *
 */
public class ThemeFilterFragment extends Fragment implements OnTouchListener {

	private ListView lv_filter;
	private ArrayList<WuseFilterItem> filterArray;
	private FilterAdapter filterAdapter;
	private FragmentManager mFragmentManager;
	private GestureDetector mGestureDetector;
	private LinearLayout ll_bottom;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frg_filter, null);
		
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		lv_filter = (ListView)view.findViewById(R.id.lv_filter);
		ll_bottom = (LinearLayout)view.findViewById(R.id.ll_bottom);
		filterArray = new ArrayList<WuseFilterItem>();
		filterAdapter = new FilterAdapter();
		lv_filter.setAdapter(filterAdapter);
		mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // if (Math.abs(e1.getRawX() - e2.getRawX()) > 250) {
                // // System.out.println("水平方向移动距离过大");
                // return true;
                // }
                if (Math.abs(velocityY) < 100) {
                    // System.out.println("手指移动的太慢了");
                    return true;
                }

                // 手势向下 down
                if ((e2.getRawY() - e1.getRawY()) > 200) {
                    return true;
                }
                // 手势向上 up
                if ((e1.getRawY() - e2.getRawY()) > 200) {
                	FragmentTransaction transaction = mFragmentManager.beginTransaction();
            		transaction.setCustomAnimations(R.anim.slide_up_in, R.anim.slide_up_out);
            		MainHomeActivity.isFilterOpen = false;
            		transaction.hide(ThemeFilterFragment.this);
            		transaction.commitAllowingStateLoss();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
		ll_bottom.setOnTouchListener(this);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mFragmentManager = getActivity().getSupportFragmentManager();
		EventBus.getDefault().register(this);
		
		FilterService server = AppContext.instance().filterService;
		server.doThemeFilterWork();
	}
	
	private class FilterAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return filterArray.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return filterArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			WuseFilterItem filterItem = filterArray.get(position);
			if(convertView == null){
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.theme_filter_item, null);
				viewHolder.tv_select = (TextView)convertView.findViewById(R.id.tv_select);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder)convertView.getTag();
			}
			
			viewHolder.tv_select.setTag(filterItem);
			viewHolder.tv_select.setOnClickListener(new TextViewClick());
			viewHolder.tv_select.setText(filterItem.filterName);
			if(filterItem.isSelect){
				viewHolder.tv_select.setSelected(true);
				TextPaint textPaint = viewHolder.tv_select.getPaint(); 
				textPaint.setFakeBoldText(true);
			}else{
				viewHolder.tv_select.setSelected(false);
				TextPaint textPaint = viewHolder.tv_select.getPaint(); 
				textPaint.setFakeBoldText(false);
			}
			return convertView;
		}
		
		private class ViewHolder {
			public TextView tv_select;
		}
		
		private class TextViewClick implements OnClickListener{

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				for(WuseFilterItem wuseFilterItem : filterArray){
					wuseFilterItem.isSelect = false;
				}
				WuseFilterItem wuseFilterItem = (WuseFilterItem)view.getTag();
				wuseFilterItem.isSelect = true;
				filterAdapter.notifyDataSetChanged();
				/*FragmentTransaction transaction = mFragmentManager.beginTransaction();
				transaction.setCustomAnimations(R.anim.push_top_in, R.anim.push_top_out);
				transaction.hide(ThemeFilterFragment.this);
				transaction.commitAllowingStateLoss();
				MainHomeActivity.isFilterOpen = false;*/
			}
			
		}
	}
	
	public void onEventMainThread(ThemeFilterEvent event) {
		filterArray = event.getFilterArray();
		filterAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
	    return mGestureDetector.onTouchEvent(event);
	}

}

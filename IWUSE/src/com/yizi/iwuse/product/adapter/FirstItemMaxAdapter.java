package com.yizi.iwuse.product.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizi.iwuse.R;
import com.yizi.iwuse.common.widget.ThemeVideoWidget;
import com.yizi.iwuse.common.widget.VideoThread;
import com.yizi.iwuse.product.model.ThemeItem;

public class FirstItemMaxAdapter extends BaseAdapter {

	/***列表项最大高度***/
	public int maxHeight = 0;
	/***列表项标准高度***/
	public int firstHeight = 0;
	/***是否是第一次加载listview***/
	private boolean isFisrt = true;
	/***主题数据***/
	public ArrayList<ThemeItem> themeArray;
	/***是否有在播放视频***/
	public boolean isVideoShow = false;
	private Activity context;
	public FirstItemMaxAdapter() {
		super();
		// TODO Auto-generated constructor stub
	}
	public FirstItemMaxAdapter(Activity context) {
		super();
		this.context = context;
	}

	public FirstItemMaxAdapter(ArrayList<ThemeItem> themeArray, Activity context) {
		super();
		this.themeArray = themeArray;
		this.context = context;
	}
	@Override
	public int getCount() {
		return (themeArray == null)?0:themeArray.size();
	}

	@Override
	public Object getItem(int i) {
		return themeArray.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		
		ViewHolder viewHolder = null;
		ThemeItem themeItem = themeArray.get(position);
		
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.first_item_max_item, null);
			viewHolder = new ViewHolder();
			viewHolder.surface = (SurfaceView)view.findViewById(R.id.vdovi_videotools);
			viewHolder.cover = (ImageView) view.findViewById(R.id.cover);
			viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			viewHolder.tv_kind = (TextView) view.findViewById(R.id.tv_kind);
			viewHolder.tv_property = (TextView) view.findViewById(R.id.tv_property);
			viewHolder.fl_insert_large = (FrameLayout) view.findViewById(R.id.fl_insert_large);
			viewHolder.tv_grey = (TextView) view.findViewById(R.id.tv_grey);
			view.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)view.getTag();
		}
		
		viewHolder.object = themeItem;
		viewHolder.tv_title.setText(themeItem.title);
		viewHolder.tv_kind.setText(themeItem.kind);
		viewHolder.tv_property.setText(themeItem.property);
		viewHolder.fl_insert_large.setLayoutParams(new FrameLayout.LayoutParams(
							FrameLayout.LayoutParams.MATCH_PARENT, maxHeight));
		if("视频".equals(themeItem.property) && isVideoShow){
			viewHolder.surface.setVisibility(View.VISIBLE);
			viewHolder.cover.setVisibility(View.GONE);
			if(viewHolder.videoView == null){
//				String vdoPath = "android.resource://"+context.getPackageName()+"/"+R.raw.demo3;
				viewHolder.videoView = new ThemeVideoWidget(context,viewHolder.surface, themeItem.videoUrl,maxHeight);
				isVideoShow = false;
			}
			
		}else{
			viewHolder.surface.setVisibility(View.GONE);
			viewHolder.cover.setVisibility(View.VISIBLE);
			viewHolder.cover.setScaleType(ImageView.ScaleType.CENTER_CROP);
			viewHolder.cover.setImageResource(themeItem.picUrl);
		}
		
		if(isFisrt){
			if (position == 0) {
				view.setLayoutParams(new AbsListView.LayoutParams(
						AbsListView.LayoutParams.MATCH_PARENT, maxHeight));
				viewHolder.tv_grey.getBackground().setAlpha(0);
				new VideoThread(viewHolder).start();
			} else {
				view.setLayoutParams(new AbsListView.LayoutParams(
						AbsListView.LayoutParams.MATCH_PARENT, firstHeight));
//				viewHolder.tv_grey.getBackground().setAlpha(127);
			}
			if(position == 3){
				isFisrt = false;
			}
		}
		return view;
	}
	
	public class ViewHolder {
		public ImageView cover;
		public SurfaceView surface;
		public TextView tv_title;
		public TextView tv_kind;
		public TextView tv_favor;
		public TextView tv_property;
		public View videoView;
		public LinearLayout ll_video;
		public FrameLayout fl_insert_large;
		public TextView tv_grey;
		public Object object;
		public ViewHolder() {
			super();
			// TODO Auto-generated constructor stub
		}
		
	}

}

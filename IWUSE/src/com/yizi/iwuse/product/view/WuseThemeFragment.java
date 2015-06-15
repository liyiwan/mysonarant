package com.yizi.iwuse.product.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.utils.IWuseUtil;
import com.yizi.iwuse.common.widget.ThemeVideoWidget;
import com.yizi.iwuse.common.widget.VideoThread;
import com.yizi.iwuse.common.widget.pulltorefresh.PullToRefreshLayout;
import com.yizi.iwuse.common.widget.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.yizi.iwuse.general.view.MainHomeFragment;
import com.yizi.iwuse.product.ProductDetailActivity;
import com.yizi.iwuse.product.adapter.FirstItemMaxAdapter;
import com.yizi.iwuse.product.adapter.FirstItemMaxAdapter.ViewHolder;
import com.yizi.iwuse.product.model.ThemeItem;
import com.yizi.iwuse.product.service.ProductService;
import com.yizi.iwuse.product.service.events.ThemeEvent;
import com.yizi.iwuse.product.service.events.VideoPlay;
import com.yizi.iwuse.product.service.events.VideoStop;

import de.greenrobot.event.EventBus;

/**
 * 物色页面
 * 
 * @author hehaodong
 *
 */
public class WuseThemeFragment extends Fragment implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener , OnRefreshListener  {

	private FirstItemMaxListView mListView;
	private FirstItemMaxAdapter mAdapter;
	/***下拉刷新，上拉加载更多控件**/
	private PullToRefreshLayout  mPullToRefreshLayout;
	/**是否需要发送请求去加载更多，用于上拉到底部自动加载**/
	private boolean mHasRequestedMore;
	/****用于模拟网络异步加载数据***/
	private Handler mHandler = new Handler();
	private ProductService server = AppContext.instance().productService;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater
				.inflate(R.layout.frg_wuse_theme, container, false);
		mListView = (FirstItemMaxListView) mView
				.findViewById(R.id.firstItemMaxListView);
		mPullToRefreshLayout = (PullToRefreshLayout) mView.findViewById(R.id.refresh_view);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		EventBus.getDefault().register(this);
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		/***屏幕高度***/
		int mScreenHeight = wm.getDefaultDisplay().getHeight();
		/***状态栏高度***/
		int statusHeight = IWuseUtil.getStatusBarHeight(getActivity());
		int maxHeight = (mScreenHeight - MainHomeFragment.titleHeight - statusHeight) / 5 * 3;//列表项最大高度
		int firstHeight = (mScreenHeight - MainHomeFragment.titleHeight - statusHeight) / 5;//列表项起始高度
		
		mAdapter = new FirstItemMaxAdapter(getActivity());
		mListView.setAdapter(mAdapter);
		mAdapter.maxHeight = maxHeight;
		mAdapter.firstHeight = firstHeight;
		mListView.setItemHeight(firstHeight);
		mListView.setItemMaxHeight(maxHeight);
		mListView.setOnItemClickListener(this);
		mListView.setOnScrollListener(this);
		/**添加上拉加载更多，下拉刷新监听***/
		mPullToRefreshLayout.setOnRefreshListener(this);
		/***设置可下拉刷新**/
		mListView.setCanPullDown(true);
		/***设置可上拉加载更多**/
		mListView.setCanPullUp(false);
//		server = AppContext.instance().productService;
		server.doNetWork();
		/**首次进入，自动刷新**/
//		mPullToRefreshLayout.autoRefresh();
	}
	
	public void onEventMainThread(ThemeEvent event) {
		if(null==mAdapter.themeArray || mAdapter.themeArray.isEmpty()){
			mAdapter.themeArray = event.getThemeArray();
		}else{
			mAdapter.themeArray.addAll(event.getThemeArray());
		}
//		Log.i("TAG", "onEventMainThread " + event.getThemeArray().size());
		mAdapter.notifyDataSetChanged();
		mHasRequestedMore = false;
	}
	
//	public void onEventMainThread(ViewHolder viewHolder) {
	public void onEventMainThread(VideoPlay videoPlay) {
		View view = mListView.getChildAt(0);
		if(view != null){
			ViewHolder viewHolder2 = (ViewHolder)view.getTag();
			if(viewHolder2 != null){
				ThemeItem themeItem = (ThemeItem)viewHolder2.object;
				if("视频".equals(themeItem.property)){
					if(!FirstItemMaxListView.isFingerPress){
						if(viewHolder2.videoView == null){
							mAdapter.isVideoShow = true;
							mAdapter.notifyDataSetChanged();
						}
					}
				}
			}
		}
	}

	
	public void onEventMainThread(VideoStop videoStop) {
		View view = mListView.getChildAt(0);
		if(view != null){
			ViewHolder videoHolder = (ViewHolder) view.getTag();
			if(videoHolder != null){
				if(videoHolder.videoView != null){
					ThemeVideoWidget videoWidget = (ThemeVideoWidget)videoHolder.videoView;
					if(videoWidget.getPlayer() != null){
//						videoWidget.getPlayer().stop();
						videoWidget.getPlayer().release();
						videoHolder.surface.setVisibility(View.GONE);
						videoHolder.cover.setVisibility(View.VISIBLE);
						videoWidget.setPlayer(null);
						videoHolder.videoView = null;
					}
				}
			}
		
		}
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		Log.i(this.getClass().getSimpleName(), "onResume");
		View view = mListView.getChildAt(0);
		if(view != null){
			ViewHolder viewHolder = (ViewHolder)view.getTag();
			ThemeItem themeItem = (ThemeItem)viewHolder.object;
			if("视频".equals(themeItem.property)){
				if(viewHolder.videoView == null){
					new VideoThread(viewHolder).start();
				}
			}
		}
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		Log.e(this.getClass().getSimpleName(), "onPause");
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		View view = mListView.getChildAt(0);
		if(view != null){
			ViewHolder viewHolder = (ViewHolder)view.getTag();
			ThemeItem themeItem = (ThemeItem)viewHolder.object;
			if("视频".equals(themeItem.property)){
				ThemeVideoWidget videoWidget = (ThemeVideoWidget)viewHolder.videoView;
				if(videoWidget != null){
					if(videoWidget.getPlayer() != null){
						videoWidget.getPlayer().release();
						viewHolder.surface.setVisibility(View.GONE);
						viewHolder.cover.setVisibility(View.VISIBLE);
						videoWidget.setPlayer(null);
						viewHolder.videoView = null;
					}
				}
			}
		}
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(),ProductDetailActivity.class);
		startActivity(intent);
	}

	
	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(null!=mAdapter.themeArray){
					mAdapter.themeArray.clear();
				}
				server.doNetWork();
				/**下拉刷新成功**/
				mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 1000);
	}
	/**
	 * 上拉加载更多
	 */
	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
	}
	
	  @Override
      public void onScrollStateChanged(final AbsListView view, final int scrollState) {
//          Log.d("TAG", "onScrollStateChanged:" + scrollState);
      }

	  /***上拉到底部自动加载下一页数据**/
      @Override
      public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
         /* Log.d("TAG", "onScroll firstVisibleItem:" + firstVisibleItem +
                  " visibleItemCount:" + visibleItemCount +
                  " totalItemCount:" + totalItemCount);*/
//           our handling
          if (!mHasRequestedMore) {
              int lastInScreen = firstVisibleItem + visibleItemCount;
              if (totalItemCount >0 && lastInScreen >= totalItemCount) {
//                  Log.d("TAG", "onScroll lastInScreen - so load more");
                  mHasRequestedMore = true;
//                  onLoadMoreItems();
                  server.doNetWork();
              }
          }
      }

}

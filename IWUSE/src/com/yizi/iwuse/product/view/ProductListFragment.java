package com.yizi.iwuse.product.view;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.widget.grid.StaggeredGridView;
import com.yizi.iwuse.common.widget.grid.util.DynamicHeightImageView;
import com.yizi.iwuse.common.widget.grid.util.DynamicHeightTextView;
import com.yizi.iwuse.common.widget.pulltorefresh.PullToRefreshLayout;
import com.yizi.iwuse.common.widget.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.yizi.iwuse.product.ProductDetailActivity;
import com.yizi.iwuse.product.adapter.ProductIAdapter;
import com.yizi.iwuse.product.model.ProductItem;
import com.yizi.iwuse.product.service.ProductService;
import com.yizi.iwuse.product.service.events.ProductEvent;

import de.greenrobot.event.EventBus;

/**		单品页面
 * @author hehaodong
 *
 */
public class ProductListFragment extends Fragment implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener , OnRefreshListener {

	/***下拉刷新，上拉加载更多控件**/
	private PullToRefreshLayout  mPullToRefreshLayout;
	/**是否需要发送请求去加载更多，用于上拉到底部自动加载**/
	private boolean mHasRequestedMore;
	/***瀑布流控件***/
	private StaggeredGridView gv_product;
	/***单品宽度***/
//	private int gridWidth = 0;
//	/***单品数据***/
//	private ArrayList<ProductItem> productArray;
	/**单品数据加载服务**/
	private ProductService server = AppContext.instance().productService;
	private ProductIAdapter adapter;
	/****用于模拟网络异步加载数据***/
	private Handler mHandler = new Handler();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.frg_productlist, container,false);
		return mView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		gv_product = (StaggeredGridView)view.findViewById(R.id.gv_product);
		mPullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		EventBus.getDefault().register(this);
		
		WindowManager wm = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);

//		int mScreenWidth = wm.getDefaultDisplay().getWidth();
//        gridWidth = mScreenWidth/2-20;
        
		adapter = new ProductIAdapter(getActivity());
		gv_product.setAdapter(adapter);
//		gv_product.setAdapter(new SampleAdapter(getActivity(), R.id.txt_line1));
		gv_product.setOnItemClickListener(this);
		gv_product.setOnScrollListener(this);
		
		/**添加上拉加载更多，下拉刷新监听***/
		mPullToRefreshLayout.setOnRefreshListener(this);
		/***设置可下拉刷新**/
		gv_product.setCanPullDown(true);
		/***设置可上拉加载更多**/
		gv_product.setCanPullUp(false);
		
//		ProductService server = AppContext.instance().productService;
		server.doProductNetWork();
	}
	

	public void onEventMainThread(ProductEvent event) {
		if(null==adapter.productArray || adapter.productArray.isEmpty()){
			adapter.productArray = event.getProductArray();
		}else{
			adapter.productArray.addAll(event.getProductArray());
		}
		adapter.notifyDataSetChanged();
		mHasRequestedMore = false;
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
				if(null!=adapter.productArray){
					adapter.productArray.clear();
				}
				server.doProductNetWork();
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
          // our handling
          if (!mHasRequestedMore) {
              int lastInScreen = firstVisibleItem + visibleItemCount;
              if (totalItemCount > 0 && lastInScreen >= totalItemCount) {
//                  Log.d("TAG", "onScroll lastInScreen - so load more");
                  mHasRequestedMore = true;
//                  onLoadMoreItems();
                  server.doProductNetWork();
              }
          }
      }
	
	
	
}

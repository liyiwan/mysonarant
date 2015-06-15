package com.yizi.iwuse.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.IActivity;
import com.yizi.iwuse.common.widget.EmptyFragment;
import com.yizi.iwuse.constants.UserConst;
import com.yizi.iwuse.filter.view.ProductFilterFragment;
import com.yizi.iwuse.filter.view.ThemeFilterFragment;
import com.yizi.iwuse.product.view.ProductListFragment;
import com.yizi.iwuse.product.view.WuseThemeFragment;
import com.yizi.iwuse.user.service.events.UserEvents;

import de.greenrobot.event.EventBus;

/***
 * 我的收藏
 * 
 * @author zhangxiying
 */
public class UserCollectionActivity extends FragmentActivity implements IActivity {

	@ViewInject(R.id.view_pager) private ViewPager mViewPager;
	/** 返回 */
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	/***标题***/
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	private FragmentManager mFragmentManager;
	/***当前页卡编号***/
	private int currIndex = 0;
	private TabPagerAdapter mPagerAdapter;
	/***主题选择按钮***/
	@ViewInject(R.id.tv_mainhome_iwusetheme) private TextView tv_mainhome_iwusetheme;
	/***单品选择按钮***/
	@ViewInject(R.id.tv_mainhome_productlist) private TextView tv_mainhome_productlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_usercollection);
		/** 注册xUtils UI框架 **/
		ViewUtils.inject(this);
		/** 注册EventBus **/
		EventBus.getDefault().register(this);
		initView();
	}
	
	private void initView(){
		tv_title.setText(getString(R.string.my_collect));
		mFragmentManager = getSupportFragmentManager();
		mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
		mViewPager.invalidate();
		mViewPager.setCurrentItem(currIndex);
		mPagerAdapter.notifyDataSetChanged();
		
	}

	/****
	 * 点击事件
	 * 
	 * @param view
	 */
	@OnClick({ R.id.btn_back,R.id.tv_mainhome_iwusetheme,R.id.tv_mainhome_productlist})
	public void handeClickListener(View view) {
		switch (view.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.tv_mainhome_iwusetheme:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.tv_mainhome_productlist:
			mViewPager.setCurrentItem(1);
			break;
		}
	}

	/****
	 * 用户监后台事件
	 * 
	 * @param userEvent
	 */
	public void onEventMainThread(UserEvents userEvent) {
		switch (userEvent.eventtype) {
		case UserConst.ENVENTTYPE_LOGIN:
			Toast.makeText(AppContext.instance().globalContext,
						AppContext.instance().globalContext.getString(R.string.login),
						Toast.LENGTH_LONG).show();
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void removeAllView() {

	}
	
	/**		主页切换适配器
	 * @author hehaodong
	 *
	 */
	private class TabPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {

		public TabPagerAdapter(FragmentManager fm) {
			super(fm);
			mViewPager.setOnPageChangeListener(this);
		}
		
		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = (WuseThemeFragment) Fragment.instantiate(UserCollectionActivity.this, WuseThemeFragment.class.getName());
				break;
			case 1:
				fragment = (EmptyFragment) Fragment.instantiate(UserCollectionActivity.this, EmptyFragment.class.getName());
				((EmptyFragment)fragment).setEmptyText(R.string.no_collection);
				break;
			}
			return fragment;
		}
		
		@Override
		public int getCount() {
			return 2;
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		
		}
		
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			
		}
		
		@Override
		public void onPageSelected(int position) {
			Animation animation = null;
//            int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
//            int two = one * 2;// 页卡1 -> 页卡3 偏移量
			switch (position) {
			case 0:
				tv_mainhome_iwusetheme.setSelected(true);
				tv_mainhome_productlist.setSelected(false);
				mViewPager.setCurrentItem(0);
				break;
			case 1:
				tv_mainhome_iwusetheme.setSelected(false);
				tv_mainhome_productlist.setSelected(true);
				mViewPager.setCurrentItem(1);
				break;
			}
			currIndex = position;
		}
	}

}

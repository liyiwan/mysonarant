package com.yizi.iwuse.user;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.BaseActivity;
import com.yizi.iwuse.common.base.IActivity;
import com.yizi.iwuse.common.utils.ILog;
import com.yizi.iwuse.common.widget.EmptyFragment;
import com.yizi.iwuse.constants.UserConst;
import com.yizi.iwuse.user.model.WalletItem;
import com.yizi.iwuse.user.service.UserService;
import com.yizi.iwuse.user.service.events.UserEvents;

import de.greenrobot.event.EventBus;

/***
 * 钱包
 * 
 * @author hehaodong
 */
public class WalletActivity extends FragmentActivity implements IActivity {
	private static final String TAG = "WalletActivity";
	/***返回按钮***/
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	/***标题***/
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.btn_right)
	private Button btn_right;
	@ViewInject(R.id.lv_wallet)
	private ListView lv_wallet;
	private WalletAdapter walletAdapter;
	private ArrayList<WalletItem> walletArray;
	@ViewInject(R.id.fl_empty)
	private FrameLayout fl_empty;
	private EmptyFragment fr_empty;
	private FragmentManager mFragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_wallet);
		ILog.i(TAG, "onCreate");
		/** 注册xUtils UI框架 **/
		ViewUtils.inject(this);
		/** 注册EventBus **/
		EventBus.getDefault().register(this);
		initView();
		
		UserService userService = AppContext.instance().userService;
		userService.doNetwork();
	}
	
	private void initView(){
		mFragmentManager = getSupportFragmentManager();
		fr_empty = new EmptyFragment();
		fr_empty.setEmptyText(R.string.empty_ticket);
		tv_title.setText(getString(R.string.my_wallet));
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setText(getString(R.string.add));
		walletAdapter = new WalletAdapter();
		lv_wallet.setAdapter(walletAdapter);
	}
	
	private class WalletAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return walletArray==null?0:walletArray.size();
        }

        @Override
        public Object getItem(int i) {
            return walletArray.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("NewApi")
		@Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            WalletItem walletItem = walletArray.get(position); 
            if(view == null){
            	viewHolder = new ViewHolder();
            	view = LayoutInflater.from(WalletActivity.this).inflate(R.layout.wallet_item, null);
            	viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
            	viewHolder.tv_condition = (TextView) view.findViewById(R.id.tv_condition);
            	viewHolder.tv_end_time = (TextView) view.findViewById(R.id.tv_end_time);
            	viewHolder.tv_ticket_id = (TextView) view.findViewById(R.id.tv_ticket_id);
            	viewHolder.ll_is_over = (LinearLayout) view.findViewById(R.id.ll_is_over);
            	viewHolder.ll_all = (LinearLayout) view.findViewById(R.id.ll_all);
            	view.setTag(viewHolder);
            }else{
            	viewHolder = (ViewHolder)view.getTag();
            }
            
            viewHolder.tv_price.setText(String.valueOf(walletItem.price));
            viewHolder.tv_condition.setText(walletItem.condition);
            String endTime = String.format(getResources().getString(R.string.end_time), walletItem.endTime);
            viewHolder.tv_end_time.setText(endTime);
            String ticketId = String.format(getResources().getString(R.string.ticket_id), walletItem.ticketId);
            viewHolder.tv_ticket_id.setText(ticketId);
            if(walletItem.state == 0){
            	viewHolder.ll_all.setAlpha((float) 0.5);
            	viewHolder.ll_is_over.setVisibility(View.VISIBLE);
            }else{
            	viewHolder.ll_all.setAlpha(1);
            	viewHolder.ll_is_over.setVisibility(View.GONE);
            }
            
            return view;
        }

        private class ViewHolder {
        	private TextView tv_price;
        	private TextView tv_condition;
        	private TextView tv_end_time;
        	private TextView tv_ticket_id;
        	private LinearLayout ll_is_over;
        	private LinearLayout ll_all;
        }
        
    }

	/****
	 * 点击事件
	 * 
	 * @param view
	 */
	@OnClick({R.id.btn_back,R.id.btn_right})
	public void handeClickListener(View view){
		switch(view.getId()){
			case R.id.btn_back:
				finish();
				break;
			case R.id.btn_right:
				Intent intent = new Intent(WalletActivity.this,AddTicketActivity.class);
				startActivity(intent);
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
			Toast.makeText(AppContext.instance().globalContext, AppContext.instance().globalContext.getString(R.string.login), Toast.LENGTH_LONG).show();
			break;
		case UserConst.ENVENTTYPE_WALLET:
			walletArray = (ArrayList<WalletItem>)userEvent.customObj;
//			walletArray = new ArrayList<WalletItem>();
			FragmentTransaction transaction = mFragmentManager.beginTransaction();
			if(walletArray.size() > 0){
				if(fr_empty != null){
					transaction.hide(fr_empty);
					lv_wallet.setVisibility(View.VISIBLE);
				}
				walletAdapter.notifyDataSetChanged();
			}else{
				lv_wallet.setVisibility(View.GONE);
				transaction.add(R.id.fl_empty,fr_empty);
				transaction.commitAllowingStateLoss();
			}
			break;
		}
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		ILog.i(TAG, "onResume");
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		ILog.i(TAG, "onPause");
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ILog.i(TAG, "onDestroy");
		EventBus.getDefault().unregister(this);
	}
	
	@Override
	public void removeAllView() {
		
	}


}

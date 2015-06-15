package com.yizi.iwuse.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.umeng.analytics.MobclickAgent;
import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.BaseActivity;
import com.yizi.iwuse.constants.UserConst;
import com.yizi.iwuse.user.service.events.UserEvents;

import de.greenrobot.event.EventBus;
/**
 * 
 * @author hunkhuang
 * 设置页面
 */
public class SettingActivity extends BaseActivity implements OnItemClickListener{

	
	/**返回*/
//	@ViewInject(R.id.btn_back)
//	private ImageView img_back;
	/**标题*/
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	/**列表*/
	@ViewInject(R.id.layout_setting_listview)
	private ListView mListView;
	
	private SettingItem[] items = new SettingItem[]{
			new SettingItem("清除缓存", new ItemCallBack() {
				
				@Override
				public void callBack() {
					Toast.makeText(getBaseContext(), "清除缓存", Toast.LENGTH_SHORT).show();
					
				}
			}),
			new SettingItem("给物色打分吧", new ItemCallBack() {
				
				@Override
				public void callBack() {
//					Toast.makeText(getBaseContext(), "给物色打分吧", Toast.LENGTH_SHORT).show();
					Uri uri = Uri.parse("market://details?id="+getPackageName());
					Intent intent = new Intent(Intent.ACTION_VIEW,uri);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);

				}
			}),
			new SettingItem("消息提醒", new ItemCallBack() {
				
				@Override
				public void callBack() {
					Toast.makeText(getBaseContext(), "消息提醒", Toast.LENGTH_SHORT).show();
					
				}
			},1),
			new SettingItem("关于物色", new ItemCallBack() {
				
				@Override
				public void callBack() {
//					Toast.makeText(getBaseContext(), "关于物色", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(SettingActivity.this, AboutActivity.class));
				}
			}),
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_setting);
		/** 注册xUtils UI框架 **/
		ViewUtils.inject(this);
		/** 注册EventBus **/
		EventBus.getDefault().register(this);
		
		setupView();
		
	}
	
	/**
	 * 设置控件属性
	 */
	private void setupView(){
		tv_title.setText("软件菜单");
		mListView.setAdapter(new SettingItemAdapter());
		mListView.setOnItemClickListener(this);
	}
	

	/****
	 * 点击事件
	 * 
	 * @param view
	 */
	@OnClick({R.id.btn_back})
	public void handeClickListener(View view){
		switch(view.getId()){
			case R.id.btn_back:
				 finish();
				break;
		}
	}
	
	/***
	 * 
	 * ListView的item点击事件
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		SettingItem item = items[position];
		item.callBack.callBack();
		
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
		}
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	@Override
	public void removeAllView() {
		
	}
	
	private class SettingItem{
		/***标题**/
		String title;
		/**点击事件**/
		ItemCallBack callBack;
		/**item类型**/
		int type;
		public SettingItem(String title, ItemCallBack callBack, int type) {
			super();
			this.title = title;
			this.setCallBack(callBack);
			this.type = type;
		}
		public SettingItem(String title, ItemCallBack callBack) {
			super();
			this.title = title;
			this.setCallBack(callBack);
		}
		
		private void setCallBack(ItemCallBack callBack){
			if(null==callBack){
				callBack = new ItemCallBack(){
					@Override
					public void callBack() {}};
			}
			this.callBack = callBack;
		}
		
	}
	/**
	 * 
	 * item点击后的回调事件
	 *
	 */
	private interface ItemCallBack{
		void callBack();
	}
	
	private class SettingItemAdapter extends BaseAdapter{
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			return ((SettingItem)getItem(position)).type;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
//			return super.getViewTypeCount();
			return 2;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return items[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return items[position].hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null ;
			switch (getItemViewType(position)) {
			case 0:
				if(null==convertView){
					convertView = getLayoutInflater().inflate(R.layout.setting_item0, null);
					holder = new ViewHolder() ;
					holder.title = (TextView) convertView.findViewById(R.id.setting_item_text);
					convertView.setTag(holder);
				}else{
					holder = (ViewHolder) convertView.getTag();
				}
				break;
			case 1:
				if(null==convertView){
					convertView = getLayoutInflater().inflate(R.layout.setting_item1, null);
					holder = new ViewHolder() ;
					holder.title = (TextView) convertView.findViewById(R.id.setting_item_text);
					holder.switchView = (Switch) convertView.findViewById(R.id.setting_item_text_switch);
					convertView.setTag(holder);
				}else{
					holder = (ViewHolder) convertView.getTag();
				}
				break;

			default:
				break;
			}
			SettingItem item = (SettingItem) getItem(position);
			holder.title.setText(item.title);
			return convertView;
		}
		
		private class ViewHolder{
			TextView title;
			Switch switchView ;
		}
		
	}
}

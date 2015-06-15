package com.yizi.iwuse.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.BaseActivity;
import com.yizi.iwuse.constants.UserConst;
import com.yizi.iwuse.user.service.events.UserEvents;

import de.greenrobot.event.EventBus;

public class AboutActivity extends BaseActivity {

	/**返回*/
//	@ViewInject(R.id.btn_back)
//	private ImageView btn_back;
	/**标题*/
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_about);
		/** 注册xUtils UI框架 **/
		ViewUtils.inject(this);
		/** 注册EventBus **/
		EventBus.getDefault().register(this);
		
		setupView();
	}
	
	/****
	 * 设置控件信息 
	 */
	private void setupView(){
		tv_title.setText("关于我们");
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

}

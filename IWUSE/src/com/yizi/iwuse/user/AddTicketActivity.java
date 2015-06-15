package com.yizi.iwuse.user;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.BaseActivity;
import com.yizi.iwuse.common.widget.CustomDialog;

public class AddTicketActivity extends BaseActivity{

	/***返回按钮***/
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	/***标题***/
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	/***确定按钮***/
	@ViewInject(R.id.btn_certain)
	private Button btn_certain;
	/***购物券输入框***/
	@ViewInject(R.id.et_input_num)
	private EditText et_input_num;
	
	@Override
	public void removeAllView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_addticket);
		ViewUtils.inject(this);
		initView();
	}
	
	private void initView(){
		tv_title.setText(R.string.add_ticket);
	}
	
	/****
	 * 点击事件
	 * 
	 * @param view
	 */
	@OnClick({R.id.btn_back,R.id.btn_certain})
	public void handeClickListener(View view){
		switch(view.getId()){
		case R.id.btn_back:
			 finish();
			break;
		case R.id.btn_certain:
			String ticketNum = et_input_num.getText().toString().trim();
			if("".equals(ticketNum)){
				Toast.makeText(AddTicketActivity.this, R.string.ticket_num_please, Toast.LENGTH_LONG).show();
				return;
			}
			showDialog();
			break;
		}
	}
	
	public void showDialog(){
		CustomDialog.Builder customDialog = new CustomDialog.Builder(this);
		customDialog.setTitle(R.string.add_success);
		customDialog.setMessage(R.string.add_desc);
		CustomDialog dialog = customDialog.create();
		dialog.show();
	}
}

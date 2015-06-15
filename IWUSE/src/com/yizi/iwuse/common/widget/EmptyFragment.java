package com.yizi.iwuse.common.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yizi.iwuse.R;
import com.yizi.iwuse.general.MainHomeActivity;

public class EmptyFragment extends Fragment {

	@ViewInject(R.id.btn_to_look)
	private Button btn_to_look;
	@ViewInject(R.id.tv_empty)
	private TextView tv_empty;
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty;
	private int tv_id;
	private int iv_id;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.frg_empty, null);
		/** 注册xUtils UI框架 **/
		ViewUtils.inject(this,mView);
		return mView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		tv_empty.setText(tv_id);
		iv_empty.setBackgroundResource(iv_id);
	}

	/****
	 * 点击事件
	 * 
	 * @param view
	 */
	@OnClick({R.id.btn_to_look})
	public void handeClickListener(View view){
		switch(view.getId()){
		case R.id.btn_to_look:
//			Intent intent = new Intent(getActivity(),MainHomeActivity.class);
//			startActivity(intent);
			getActivity().setResult(Activity.RESULT_OK);
			getActivity().finish();
			break;
		}
	}
	
	public void setEmptyText(int tv_id){
		this.tv_id = tv_id;
	}
	
	public void setEmptyImg(int iv_id){
		this.iv_id = iv_id;
	}
	
}

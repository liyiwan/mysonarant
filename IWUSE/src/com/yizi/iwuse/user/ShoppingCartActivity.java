package com.yizi.iwuse.user;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.AppParams;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.BaseActivity;
import com.yizi.iwuse.common.base.IActivity;
import com.yizi.iwuse.common.widget.CustomDialog;
import com.yizi.iwuse.common.widget.EmptyFragment;
import com.yizi.iwuse.constants.UserConst;
import com.yizi.iwuse.user.database.UserDataManager;
import com.yizi.iwuse.user.model.ShoppingItem;
import com.yizi.iwuse.user.service.UserService;
import com.yizi.iwuse.user.service.events.UserEvents;

import de.greenrobot.event.EventBus;

/****
 * 购物车
 * 
 * @author hehaodong
 *
 */
public class ShoppingCartActivity extends FragmentActivity implements IActivity {

	/***返回按钮***/
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	/***标题***/
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	/***购物车商品列表集合***/
	private ArrayList<ShoppingItem> shoppingArray;
	/***购物车列表适配器***/
	private ShoppingAdapter shoppingAdapter;
	/***购物车listview***/
	@ViewInject(R.id.listView)
	private ListView listView;
	/***选择的商品总价格显示控件***/
	@ViewInject(R.id.tv_sum_price)
	private TextView tv_sum_price;
	/***选择的商品总价格***/
	private int sumPrice = 0;
	/***全选按钮***/
	@ViewInject(R.id.tv_all_select)
	private TextView tv_all_select;
	/***全否按钮***/
	@ViewInject(R.id.tv_no_select)
	private TextView tv_no_select;
	/***是否点击了全选按钮，通过该阀值刷新视图***/
	private boolean is_all_select = false;
	private boolean is_all_no_select = false;
	/***通过该阀值让用户点击全选按钮时，不会触发check监听***/
//	private boolean is_check = true;
	/***使用购物券请登录提醒文字***/
	@ViewInject(R.id.ll_ticket_login)
	private LinearLayout ll_ticket_login;
	/***登录按钮***/
	@ViewInject(R.id.tv_login)
	private TextView tv_login;
	/***结算按钮***/
	@ViewInject(R.id.btn_submitorder)
	private Button btn_submitorder;
	/***用户数据库操作对象***/
	private UserDataManager userDatabase;
	/***页面为空layout***/
	@ViewInject(R.id.ll_no_empty)
	private LinearLayout ll_no_empty;
	/***自定义页面为空Fragment***/
	private EmptyFragment fr_empty;
	private FragmentManager mFragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_shoppingcart);
		
		/** 注册xUtils UI框架 **/
		ViewUtils.inject(this);
		/** 注册EventBus **/
		EventBus.getDefault().register(this);
		userDatabase = new UserDataManager();
		
		initView();
		//登录则进行网络操作，并把网络获得的数据保存到数据库
		if(AppParams.isLogin){
			userDatabase.updataShoppingBelong("abc");
			UserService userService = AppContext.instance().userService;
			userService.shoppingDoNetwork();
		}else{
			//未登录，则把游客的购物车数据从数据库取出并显示
			ArrayList<ShoppingItem> shopping = (ArrayList<ShoppingItem>)userDatabase.getShoppingArray("visitor");
			if(shopping != null){
				if(shopping.size() > 0){
					shoppingArray.addAll(shopping);
					shoppingAdapter.notifyDataSetChanged();
				}else{
					FragmentTransaction transaction = mFragmentManager.beginTransaction();
					ll_no_empty.setVisibility(View.GONE);
					transaction.add(R.id.fl_empty,fr_empty);
					transaction.commitAllowingStateLoss();
				}
			}else{
				FragmentTransaction transaction = mFragmentManager.beginTransaction();
				ll_no_empty.setVisibility(View.GONE);
				transaction.add(R.id.fl_empty,fr_empty);
				transaction.commitAllowingStateLoss();
			}
		}
	}

	private void initView(){
		mFragmentManager = getSupportFragmentManager();
		fr_empty = new EmptyFragment();
		fr_empty.setEmptyText(R.string.empty_shopping_car);
		tv_title.setText(R.string.my_shopping_car);
		shoppingArray = new ArrayList<ShoppingItem>();
		shoppingAdapter = new ShoppingAdapter();
		listView.setAdapter(shoppingAdapter);
		String sumPriceStr = String.format(getString(R.string.sum_price), sumPrice);
		tv_sum_price.setText(sumPriceStr);
		//没有登录则显示购物券使用要求登录提醒
		if(!AppParams.isLogin){
			ll_ticket_login.setVisibility(View.VISIBLE);
		}
	}
	
	private class ShoppingAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return shoppingArray==null?0:shoppingArray.size();
        }

        @Override
        public Object getItem(int i) {
            return shoppingArray.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("NewApi")
		@Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            ShoppingItem shoppingItem = shoppingArray.get(position); 
            if(view == null){
            	viewHolder = new ViewHolder();
            	view = LayoutInflater.from(ShoppingCartActivity.this).inflate(R.layout.shopping_item, null);
            	viewHolder.tv_select = (TextView) view.findViewById(R.id.tv_select);
            	viewHolder.iv_product_img = (ImageView) view.findViewById(R.id.iv_product_img);
            	viewHolder.tv_product_price = (TextView) view.findViewById(R.id.tv_product_price);
            	viewHolder.tv_product_name = (TextView) view.findViewById(R.id.tv_product_name);
            	viewHolder.tv_product_property = (TextView) view.findViewById(R.id.tv_product_property);
            	viewHolder.tv_num = (TextView) view.findViewById(R.id.tv_num);
            	viewHolder.btn_reduce = (Button) view.findViewById(R.id.btn_reduce);
            	viewHolder.btn_add = (Button) view.findViewById(R.id.btn_add);
            	viewHolder.btn_del = (Button) view.findViewById(R.id.btn_del);
            	view.setTag(viewHolder);
            }else{
            	viewHolder = (ViewHolder)view.getTag();
            }
            
            viewHolder.iv_product_img.setImageResource(R.drawable.image2);
            viewHolder.tv_product_name.setText(shoppingItem.productName);
            viewHolder.tv_num.setText(String.valueOf(shoppingItem.productNum));
            viewHolder.tv_product_price.setText(String.valueOf(shoppingItem.productPrice));
            viewHolder.tv_product_property.setText("产品属性");
            viewHolder.obj = shoppingItem;
            if(shoppingItem.isSelect){
            	viewHolder.tv_select.setSelected(true);
            }else{
            	viewHolder.tv_select.setSelected(false);
            }
            viewHolder.tv_select.setTag(viewHolder);
            viewHolder.tv_select.setOnClickListener(new SumPriceListener());
            viewHolder.btn_add.setTag(viewHolder);
            viewHolder.btn_add.setOnClickListener(new AddSumListener());
            viewHolder.btn_reduce.setTag(viewHolder);
            viewHolder.btn_reduce.setOnClickListener(new ReduceSumListener());
            viewHolder.btn_del.setTag(viewHolder);
            viewHolder.btn_del.setOnClickListener(new DelListener());
            /*if(is_all_select == true && shoppingArray.size() == (position + 1)){
            	int sumPrice = 0;
				for(int i=0;i < shoppingArray.size();i++){
					ShoppingItem shoppingItemAdd = shoppingArray.get(i);
					sumPrice = sumPrice + (shoppingItemAdd.productPrice * shoppingItemAdd.productNum);
				}
				String sumPriceStr = String.format(getString(R.string.sum_price), sumPrice);
				tv_sum_price.setText(sumPriceStr);
				ShoppingCartActivity.this.sumPrice = sumPrice;
            	is_all_select = false;
            }else if(is_all_no_select == true && shoppingArray.size() == (position + 1)){
            	sumPrice = 0;
				String sumPriceStr2 = String.format(getString(R.string.sum_price), sumPrice);
				tv_sum_price.setText(sumPriceStr2);
				is_all_no_select = false;
            }*/
            return view;
        }

        private class ViewHolder {
        	private TextView tv_select;
        	private ImageView iv_product_img;
        	private TextView tv_product_price;
        	private TextView tv_product_name;
        	private TextView tv_product_property;
        	private TextView tv_num;
        	private Button btn_reduce;
        	private Button btn_add;
        	private Button btn_del;
        	private Object obj;
        }
        
        private class DelListener implements OnClickListener{

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				ViewHolder viewHolder = (ViewHolder)view.getTag();
				if(viewHolder != null){
					ShoppingItem shoppingItem = (ShoppingItem)viewHolder.obj;
					showDelDialog(shoppingItem);
				}
			}
        	
        }
        
        private class SumPriceListener implements OnClickListener{
        	
        	/*@Override
        	public void onCheckedChanged(CompoundButton buttonView,
        			boolean isChecked) {
        		// TODO Auto-generated method stub
        		ViewHolder viewHolder = (ViewHolder)buttonView.getTag();
        		if(viewHolder != null){
        			int price = Integer.valueOf(viewHolder.tv_product_price.getText().toString().trim());
        			int number = Integer.valueOf(viewHolder.tv_num.getText().toString().trim());
        			if(isChecked){
        				sumPrice = sumPrice + (price * number);
        			}else{
        				sumPrice = sumPrice - (price * number);
        			}
        			String sumPriceStr = String.format(getString(R.string.sum_price), sumPrice);
        			tv_sum_price.setText(sumPriceStr);
        		}
        		
        	}*/

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				ViewHolder viewHolder = (ViewHolder)view.getTag();
				if(viewHolder != null){
        			int price = Integer.valueOf(viewHolder.tv_product_price.getText().toString().trim());
        			int number = Integer.valueOf(viewHolder.tv_num.getText().toString().trim());
        			if(!view.isSelected()){
        				view.setSelected(true);
        				sumPrice = sumPrice + (price * number);
        			}else{
        				view.setSelected(false);
        				sumPrice = sumPrice - (price * number);
        			}
        			String sumPriceStr = String.format(getString(R.string.sum_price), sumPrice);
        			tv_sum_price.setText(sumPriceStr);
        		}
			}
        	
        }
        
        private class AddSumListener implements OnClickListener{

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				ViewHolder viewHolder = (ViewHolder)view.getTag();
				if(viewHolder != null){
					int number = Integer.valueOf(viewHolder.tv_num.getText().toString().trim());
					number = number + 1;
					viewHolder.tv_num.setText(number + "");
					ShoppingItem shoppingItem = (ShoppingItem)viewHolder.obj;
					shoppingItem.productNum = number;
					if(viewHolder.tv_select.isSelected()){
						int price = Integer.valueOf(viewHolder.tv_product_price.getText().toString().trim());
						sumPrice = sumPrice + price;
						String sumPriceStr = String.format(getString(R.string.sum_price), sumPrice);
						tv_sum_price.setText(sumPriceStr);
					}
				}
			}
        	
        }
        
        private class ReduceSumListener implements OnClickListener{

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				ViewHolder viewHolder = (ViewHolder)view.getTag();
				if(viewHolder != null){
					int number = Integer.valueOf(viewHolder.tv_num.getText().toString().trim());
					if(number > 1){
						number = number - 1;
					}
					viewHolder.tv_num.setText(number + "");
					ShoppingItem shoppingItem = (ShoppingItem)viewHolder.obj;
					shoppingItem.productNum = number;
					if(viewHolder.tv_select.isSelected()){
						int price = Integer.valueOf(viewHolder.tv_product_price.getText().toString().trim());
						sumPrice = sumPrice - price;
						String sumPriceStr = String.format(getString(R.string.sum_price), sumPrice);
						tv_sum_price.setText(sumPriceStr);
					}
				}
			}
        	
        }
        
    }
	
	
	/****
	 * 点击事件
	 * 
	 * @param view
	 */
	@OnClick({R.id.btn_back,R.id.tv_all_select,R.id.tv_no_select,R.id.tv_login,R.id.btn_submitorder})
	public void handeClickListener(View view){
		switch(view.getId()){
			case R.id.btn_back:
				finish();
				break;
			case R.id.tv_all_select:
				for(ShoppingItem shoppingItem : shoppingArray){
					shoppingItem.isSelect = true;
				}
//				is_all_select = true;
				shoppingAdapter.notifyDataSetChanged();
				sumPrice = 0;
				for(int i=0;i < shoppingArray.size();i++){
					ShoppingItem shoppingItem = shoppingArray.get(i);
					sumPrice = sumPrice + (shoppingItem.productPrice * shoppingItem.productNum);
				}
				String sumPriceStr = String.format(getString(R.string.sum_price), sumPrice);
				tv_sum_price.setText(sumPriceStr);
				break;
			case R.id.tv_no_select:
				for(ShoppingItem shoppingItem : shoppingArray){
					shoppingItem.isSelect = false;
				}
//				is_all_no_select = true;
				shoppingAdapter.notifyDataSetChanged();
				sumPrice = 0;
				String sumPriceStr2 = String.format(getString(R.string.sum_price), sumPrice);
				tv_sum_price.setText(sumPriceStr2);
				break;
			case R.id.tv_login:
				Intent intent = new Intent(ShoppingCartActivity.this,LoginActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_submitorder:
				if(!AppParams.isLogin){
					showDialog();
				}else{
					Intent intent2 = new Intent(ShoppingCartActivity.this,AddSiteActivity.class);
					intent2.putExtra("sumPrice", sumPrice);
					startActivity(intent2);
				}
				break;
		}
	}
	
	public void showDialog(){
		CustomDialog.Builder customDialog = new CustomDialog.Builder(this);
		customDialog.setLinearLayout(R.layout.buy_login_dialog);
		customDialog.setMessage(R.string.no_login_show_dialog);
		customDialog.setPositiveButton(R.string.direct_buy, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(sumPrice == 0){
					Toast.makeText(ShoppingCartActivity.this,R.string.select_product_please, Toast.LENGTH_LONG).show();
				}else{
					Intent intent = new Intent(ShoppingCartActivity.this,AddSiteActivity.class);
					intent.putExtra("sumPrice", sumPrice);
					startActivity(intent);
				}
				if(dialog != null){
					dialog.dismiss();
				}
			}
		});
		customDialog.setNegativeButton(R.string.login,  new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShoppingCartActivity.this,LoginActivity.class);
				startActivity(intent);
				if(dialog != null){
					dialog.dismiss();
				}
			}
		});
		CustomDialog dialog = customDialog.create();
		dialog.show();
	}
	
	public void showDelDialog(final ShoppingItem shoppingItem){
		CustomDialog.Builder customDialog = new CustomDialog.Builder(this);
		customDialog.setMessage(R.string.confirm_del_shopping);
		customDialog.setPositiveButton(R.string.certain, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				userDatabase.delShopping(shoppingItem);
				ArrayList<ShoppingItem> shopping = null;
				if(AppParams.isLogin){
					shopping = (ArrayList<ShoppingItem>)userDatabase.getShoppingArray("abc");
				}else{
					shopping = (ArrayList<ShoppingItem>)userDatabase.getShoppingArray("visitor");
				}
				if(shopping != null){
					if(shopping.size() > 0){
						shoppingArray.removeAll(shoppingArray);
						shoppingArray.addAll(shopping);
						shoppingAdapter.notifyDataSetChanged();
					}else{
						FragmentTransaction transaction = mFragmentManager.beginTransaction();
						ll_no_empty.setVisibility(View.GONE);
						transaction.add(R.id.fl_empty,fr_empty);
						transaction.commitAllowingStateLoss();
					}
				}else{
					FragmentTransaction transaction = mFragmentManager.beginTransaction();
					ll_no_empty.setVisibility(View.GONE);
					transaction.add(R.id.fl_empty,fr_empty);
					transaction.commitAllowingStateLoss();
				}
				if(dialog != null){
					dialog.dismiss();
				}
			}
		});
		customDialog.setNegativeButton(R.string.cancel,  new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(dialog != null){
					dialog.dismiss();
				}
			}
		});
		CustomDialog dialog = customDialog.create();
		dialog.show();
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
		case UserConst.ENVENTTYPE_SHOPPING_CAR:
			ArrayList<ShoppingItem> shopping = (ArrayList<ShoppingItem>)userDatabase.getShoppingArray("abc");
			if(shopping != null){
				if(shopping.size() > 0){
					shoppingArray.addAll(shopping);
					shoppingAdapter.notifyDataSetChanged();
				}
			}
			/*ArrayList<ShoppingItem> shopping = (ArrayList<ShoppingItem>)userEvent.customObj;
			if(shopping != null){
				if(shopping.size() > 0){
					shoppingArray.addAll(shopping);
					shoppingAdapter.notifyDataSetChanged();
				}
			}*/
			break;
		}
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if(!AppParams.isLogin){
			ll_ticket_login.setVisibility(View.VISIBLE);
		}else{
			ll_ticket_login.setVisibility(View.GONE);
		}
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

}

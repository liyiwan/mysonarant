package com.yizi.iwuse.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yizi.iwuse.AppParams;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.base.BaseActivity;
import com.yizi.iwuse.common.widget.WebViewWidget;
import com.yizi.iwuse.user.AddSiteActivity;
import com.yizi.iwuse.user.ShoppingCartActivity;
import com.yizi.iwuse.user.database.UserDataManager;
import com.yizi.iwuse.user.model.ShoppingItem;

/**		产品详情页
 * @author hehaodong
 *
 */
public class ProductDetailActivity extends BaseActivity{

	@ViewInject(R.id.wv_product_detail) private WebViewWidget wv_product_detail;
	@ViewInject(R.id.ll_add_cart) private LinearLayout ll_add_cart;
	@ViewInject(R.id.btn_first_buy) private Button btn_first_buy;
	@ViewInject(R.id.cart_anim_icon) private ImageView cart_anim_icon;
	@ViewInject(R.id.btn_back) private Button btn_back;
	private Animation mAnimation;
	private UserDataManager userDatabase;
	/***购物车商品数量***/
	@ViewInject(R.id.tv_car_num) private TextView tv_car_num;
	
	@Override
	public void removeAllView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_productdetail);
		ViewUtils.inject(this);
		userDatabase = new UserDataManager();
		
		wv_product_detail.loadUrl("file:///android_asset/index.html");
		mAnimation = AnimationUtils.loadAnimation(this, R.anim.cart_anim);
		mAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				cart_anim_icon.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//未登录，游客购物车数量
		if(!AppParams.isLogin){
			int shoppingNum = userDatabase.getShoppingNum("visitor");
			if(shoppingNum != 0){
				tv_car_num.setVisibility(View.VISIBLE);
				tv_car_num.setText(String.valueOf(shoppingNum));
			}else{
				tv_car_num.setVisibility(View.GONE);
			}
		}else{
			int shoppingNum = userDatabase.getShoppingNum("abc");
			if(shoppingNum != 0){
				tv_car_num.setVisibility(View.VISIBLE);
				tv_car_num.setText(String.valueOf(shoppingNum));
			}else{
				tv_car_num.setVisibility(View.GONE);
			}
		}
		
	}

	/**		事件监听
	 * @param view
	 */
	@OnClick({R.id.btn_back,R.id.ll_add_cart,R.id.btn_first_buy})
	public void handeClickListener(View view){
		switch(view.getId()){
			case R.id.btn_back:
				finish();
				break;
			case R.id.ll_add_cart:
				cart_anim_icon.setVisibility(View.VISIBLE);
				cart_anim_icon.startAnimation(mAnimation);
				if(AppParams.isLogin){
					ShoppingItem shoppingItem1 = new ShoppingItem(4,"imgurl","网络数据","产品属性信息",2888,1,"abc");
					userDatabase.saveShopping(shoppingItem1);
					int shoppingNum = userDatabase.getShoppingNum("abc");
					if(shoppingNum != 0){
						tv_car_num.setVisibility(View.VISIBLE);
						tv_car_num.setText(String.valueOf(shoppingNum));
					}else{
						tv_car_num.setVisibility(View.GONE);
					}
				}else{
					ShoppingItem shoppingItem1 = new ShoppingItem(6,"imgurl","产品详细名称","产品属性信息",2888,1,"visitor");
					userDatabase.saveShopping(shoppingItem1);
					int shoppingNum = userDatabase.getShoppingNum("visitor");
					if(shoppingNum != 0){
						tv_car_num.setVisibility(View.VISIBLE);
						tv_car_num.setText(String.valueOf(shoppingNum));
					}else{
						tv_car_num.setVisibility(View.GONE);
					}
				}
				break;
			case R.id.btn_first_buy:
//				if(AppParams.isLogin){
//					
//				}else{
					Intent intent = new Intent(ProductDetailActivity.this,ShoppingCartActivity.class);
					startActivity(intent);
					/*Intent intent = new Intent(ProductDetailActivity.this,AddSiteActivity.class);
					intent.putExtra("sumPrice", 1000);
					startActivity(intent);*/
//				}
				break;
		}
	}


}

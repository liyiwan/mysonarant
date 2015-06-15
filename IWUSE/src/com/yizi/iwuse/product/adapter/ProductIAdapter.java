package com.yizi.iwuse.product.adapter;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yizi.iwuse.R;
import com.yizi.iwuse.common.widget.grid.util.DynamicHeightImageView;
import com.yizi.iwuse.product.model.ProductItem;

public class ProductIAdapter extends BaseAdapter {
	
	private  final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
	private final Random mRandom = new Random();
	/***单品数据***/
	public ArrayList<ProductItem> productArray;
	public Activity mActivity;
	
    public ProductIAdapter(Activity mActivity) {
		super();
		this.mActivity = mActivity;
	}

	@Override
    public int getCount() {
        return productArray==null?0:productArray.size();
    }

    @Override
    public Object getItem(int i) {
        return productArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
        	viewHolder = new ViewHolder();
        	view = LayoutInflater.from(mActivity).inflate(R.layout.single_img, null);
        	viewHolder.cover = (DynamicHeightImageView) view.findViewById(R.id.cover);
        	view.setTag(viewHolder);
        }else{
        	viewHolder = (ViewHolder)view.getTag();
        }
        double positionHeight = getPositionRatio(position);
//        view.setLayoutParams(new AbsListView.LayoutParams(gridWidth, gridWidth));
        viewHolder.cover.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        viewHolder.cover.setScaleType(ImageView.ScaleType.FIT_XY);
        viewHolder.cover.setImageResource(productArray.get(position).themeUrl);
        viewHolder.cover.setHeightRatio(positionHeight);
//        Log.e("height", positionHeight + "  " + position);
//        view.setOnClickListener(new ProductClick());
        return view;
    }

    class ViewHolder {
    	DynamicHeightImageView cover;
    }
    

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
//            Log.d("TAG", "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 1.5) + 1.0; // height will be 1.0 - 1.5 the width
    }
    
    /*private class ProductClick implements OnClickListener{

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(),ProductDetailActivity.class);
    		startActivity(intent);
		}
		
	}*/
}

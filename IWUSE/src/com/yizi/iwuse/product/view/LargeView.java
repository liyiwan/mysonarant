package com.yizi.iwuse.product.view;

import android.view.View;

public class LargeView {

	private View mTarget;

	public LargeView(View target) {
		mTarget = target;
	}

	public int getWidth() {
		return mTarget.getLayoutParams().width;
	}

	public void setWidth(int width) {
		mTarget.getLayoutParams().width = width;
		mTarget.requestLayout();
	}
	
	public int getHeight() {
		return mTarget.getLayoutParams().height;
	}

	public void setHeight(int height) {
		mTarget.getLayoutParams().height = height;
		mTarget.requestLayout();
	}
}

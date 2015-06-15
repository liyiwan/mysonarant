package com.yizi.iwuse.common.widget.pulltorefresh.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements Pullable
{

	// 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
		private boolean canPullDown = true;
		private boolean canPullUp = true;
	
	public PullableScrollView(Context context)
	{
		super(context);
	}

	public PullableScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
		if (canPullDown && getScrollY() == 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean canPullUp()
	{
		if(!canPullUp) return canPullUp ;
		if (canPullUp && (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight())))
			return true;
		else
			return false;
	}

	public void setCanPullDown(boolean canPullDown) {
		this.canPullDown = canPullDown;
	}

	public void setCanPullUp(boolean canPullUp) {
		this.canPullUp = canPullUp;
	}

	
	
}

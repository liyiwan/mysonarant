package com.yizi.iwuse.common.widget.pulltorefresh.pullableview;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class PullableTextView extends TextView implements Pullable
{
	// 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
		private boolean canPullDown = true;
		private boolean canPullUp = true;
	public PullableTextView(Context context)
	{
		super(context);
	}

	public PullableTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
		return canPullDown;
	}

	@Override
	public boolean canPullUp()
	{
		return canPullUp;
	}

	public void setCanPullDown(boolean canPullDown) {
		this.canPullDown = canPullDown;
	}

	public void setCanPullUp(boolean canPullUp) {
		this.canPullUp = canPullUp;
	}

}

package com.yizi.iwuse.common.widget.pulltorefresh.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class PullableExpandableListView extends ExpandableListView implements
		Pullable
{


	// 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
		private boolean canPullDown = true;
		private boolean canPullUp = true;
	public PullableExpandableListView(Context context)
	{
		super(context);
	}

	public PullableExpandableListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableExpandableListView(Context context, AttributeSet attrs,
			int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
		if(!canPullDown) return canPullDown ;
		if (getCount() == 0)
		{
			// 没有item的时候也可以下拉刷新
			return true;
		} else if (getFirstVisiblePosition() == 0
				&& getChildAt(0).getTop() >= 0)
		{
			// 滑到顶部了
			return true;
		} else
			return false;
	}

	@Override
	public boolean canPullUp()	
	{
		if(!canPullUp) return canPullUp ;
		if (getCount() == 0)
		{
			// 没有item的时候也可以上拉加载
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1))
		{
			// 滑到底部了
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt(
							getLastVisiblePosition()
									- getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
				return true;
		}
		return false;
	}
	
	public void setCanPullDown(boolean canPullDown) {
		this.canPullDown = canPullDown;
	}

	public void setCanPullUp(boolean canPullUp) {
		this.canPullUp = canPullUp;
	}


}

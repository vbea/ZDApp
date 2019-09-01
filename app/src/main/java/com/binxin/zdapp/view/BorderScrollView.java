package com.binxin.zdapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class BorderScrollView extends ScrollView
{
	private OnBorderListener onBorderListener;
	private View contentView;
	public BorderScrollView(Context context)
	{
		super(context);
	}
	public BorderScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	public BorderScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy)
	{
		super.onScrollChanged(x, y, oldx, oldy);
		doOnBorderListener();
	}
	
	public void setOnBorderListener(final OnBorderListener onBorderListener)
	{
		this.onBorderListener = onBorderListener;
		if (onBorderListener == null)
		{
			return;
		}
		if (contentView == null)
		{
			contentView = getChildAt(0);
		}
	}
	public static interface OnBorderListener
	{
		public void onBottom();
		public void onTop();
	}
	
	private void doOnBorderListener()
	{
		if (contentView != null && contentView.getMeasuredHeight() <= getScrollY() + getHeight())
		{
			if (onBorderListener != null)
			{
				onBorderListener.onBottom();
			}
		}
		else if (getScrollY() == 0)
		{
			if (onBorderListener != null)
			{
				onBorderListener.onTop();
			}
		}
	}
}

package com.binxin.zdapp.view.viewpager;

import com.binxin.zdapp.view.VerticalViewPager;

public interface VerIndicator extends VerticalViewPager.OnPageChangeListener
{
	//自定义VerticalViewPager接口
	void setViewPager(VerticalViewPager view);

    /**
     * Vbea ZDApp
     */
    void setViewPager(VerticalViewPager view, int initialPosition);

    void setCurrentItem(int item);

    void setOnPageChangeListener(VerticalViewPager.OnPageChangeListener listener);

    void notifyDataSetChanged();
}

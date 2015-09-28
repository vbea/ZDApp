package com.binxin.zdapp.classes;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.binxin.zdapp.R;
import com.binxin.zdapp.style.SystemBarTintManager;

public class MyThemesInfo
{
	private Activity active;
	public MyThemesInfo(Activity a)
	{
		this.active = a;
	}
	public void setThemeColor(int resid)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			//状态栏
			active.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//导航栏
			//content.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			SystemBarTintManager tintManager = new SystemBarTintManager(active);
			tintManager.setStatusBarTintEnabled(true);
			//此处可以重新指定状态栏颜色
			//tintManager.setStatusBarTintResource(resid);
			switch(resid)
			{
				case 0:
					tintManager.setStatusBarTintResource(R.color.title);
					return;
				case 1:
					tintManager.setStatusBarTintResource(R.color.title_old);
					return;
				case 2:
					tintManager.setStatusBarTintResource(R.color.theme_col1);
					return;
				case 3:
					tintManager.setStatusBarTintResource(R.color.theme_col2);
					return;
				case 4:
					tintManager.setStatusBarTintResource(R.color.theme_col3);
					return;
				case 5:
					tintManager.setStatusBarTintResource(R.color.theme_col4);
					return;
				case 6:
					tintManager.setStatusBarTintResource(R.color.theme_col5);
					return;
				case 7:
					tintManager.setStatusBarTintResource(R.color.theme_col6);
					return;
				case 8:
					tintManager.setStatusBarTintResource(R.color.theme_col7);
					return;
				case 9:
					tintManager.setStatusBarTintResource(R.color.theme_col8);
					return;
				case 10:
					tintManager.setStatusBarTintResource(R.color.theme_col9);
					return;
				case 11:
					tintManager.setStatusBarTintResource(R.color.black);
					return;
				case 12:
					tintManager.setStatusBarTintResource(R.color.theme_col11);
					return;
				case 13:
					tintManager.setStatusBarTintResource(R.color.theme_col12);
					return;
				case 14:
					tintManager.setStatusBarTintResource(R.color.theme_col13);
					return;
				case 15:
					tintManager.setStatusBarTintResource(R.color.theme_col14);
					return;
				case 16:
					tintManager.setStatusBarTintResource(R.color.theme_col15);
					return;
				case 17:
					tintManager.setStatusBarTintResource(R.color.theme_col16);
					return;
				case 18:
					tintManager.setStatusBarTintResource(R.color.theme_col17);
					return;
				case 19:
					tintManager.setStatusBarTintResource(R.color.theme_col18);
					return;
				default:
					tintManager.setStatusBarTintResource(R.color.title);
					return;
			}
		}
	}
	public void setThemes(LinearLayout lin,int code)
	{
		getThemes(lin,code);
		setThemeColor(code);
	}
	public void getThemes(LinearLayout lin,int code)
	{
		switch(code)
		{
			case 0:
				lin.setBackgroundResource(R.color.title);
				return;
			case 1:
				lin.setBackgroundResource(R.color.title_old);
				return;
			case 2:
				lin.setBackgroundResource(R.color.theme_col1);
				return;
			case 3:
				lin.setBackgroundResource(R.color.theme_col2);
				return;
			case 4:
				lin.setBackgroundResource(R.color.theme_col3);
				return;
			case 5:
				lin.setBackgroundResource(R.color.theme_col4);
				return;
			case 6:
				lin.setBackgroundResource(R.color.theme_col5);
				return;
			case 7:
				lin.setBackgroundResource(R.color.theme_col6);
				return;
			case 8:
				lin.setBackgroundResource(R.color.theme_col7);
				return;
			case 9:
				lin.setBackgroundResource(R.color.theme_col8);
				return;
			case 10:
				lin.setBackgroundResource(R.color.theme_col9);
				return;
			case 11:
				lin.setBackgroundResource(R.color.theme_col10);
				return;
			case 12:
				lin.setBackgroundResource(R.color.theme_col11);
				return;
			case 13:
				lin.setBackgroundResource(R.color.theme_col12);
				return;
			case 14:
				lin.setBackgroundResource(R.color.theme_col13);
				return;
			case 15:
				lin.setBackgroundResource(R.color.theme_col14);
				return;
			case 16:
				lin.setBackgroundResource(R.color.theme_col15);
				return;
			case 17:
				lin.setBackgroundResource(R.color.theme_col16);
				return;
			case 18:
				lin.setBackgroundResource(R.color.theme_col17);
				return;
			case 19:
				lin.setBackgroundResource(R.color.theme_col18);
				return;
			default:
				lin.setBackgroundResource(R.color.title);
				return;
		}
	}
	public void getRThemes(RelativeLayout lin,int code)
	{
		//lin.setBackgroundColor(Color.parseColor("#4F000000"));
		switch(code)
		{
			case 0:
				lin.setBackgroundResource(R.color.title);
				return;
			case 1:
				lin.setBackgroundResource(R.color.title_old);
				return;
			case 2:
				lin.setBackgroundResource(R.color.theme_col1);
				return;
			case 3:
				lin.setBackgroundResource(R.color.theme_col2);
				return;
			case 4:
				lin.setBackgroundResource(R.color.theme_col3);
				return;
			case 5:
				lin.setBackgroundResource(R.color.theme_col4);
				return;
			case 6:
				lin.setBackgroundResource(R.color.theme_col5);
				return;
			case 7:
				lin.setBackgroundResource(R.color.theme_col6);
				return;
			case 8:
				lin.setBackgroundResource(R.color.theme_col7);
				return;
			case 9:
				lin.setBackgroundResource(R.color.theme_col8);
				return;
			case 10:
				lin.setBackgroundResource(R.color.theme_col9);
				return;
			case 11:
				lin.setBackgroundResource(R.color.theme_col10);
				return;
			case 12:
				lin.setBackgroundResource(R.color.theme_col11);
				return;
			case 13:
				lin.setBackgroundResource(R.color.theme_col12);
				return;
			case 14:
				lin.setBackgroundResource(R.color.theme_col13);
				return;
			case 15:
				lin.setBackgroundResource(R.color.theme_col14);
				return;
			case 16:
				lin.setBackgroundResource(R.color.theme_col15);
				return;
			case 17:
				lin.setBackgroundResource(R.color.theme_col16);
				return;
			case 18:
				lin.setBackgroundResource(R.color.theme_col17);
				return;
			case 19:
				lin.setBackgroundResource(R.color.theme_col18);
				return;
			default:
				lin.setBackgroundResource(R.color.title);
				return;
		}
	}

	public void setImage(ImageView lin,int code)
	{
		//lin.setBackgroundColor(Color.parseColor("#4F000000"));
		switch(code)
		{
			case 0:
				lin.setImageResource(R.color.title);
				return;
			case 1:
				lin.setImageResource(R.color.title_old);
				return;
			case 2:
				lin.setImageResource(R.color.theme_col1);
				return;
			case 3:
				lin.setImageResource(R.color.theme_col2);
				return;
			case 4:
				lin.setImageResource(R.color.theme_col3);
				return;
			case 5:
				lin.setImageResource(R.color.theme_col4);
				return;
			case 6:
				lin.setImageResource(R.color.theme_col5);
				return;
			case 7:
				lin.setImageResource(R.color.theme_col6);
				return;
			case 8:
				lin.setImageResource(R.color.theme_col7);
				return;
			case 9:
				lin.setImageResource(R.color.theme_col8);
				return;
			case 10:
				lin.setImageResource(R.color.theme_col9);
				return;
			case 11:
				lin.setImageResource(R.color.theme_col10);
				return;
			case 12:
				lin.setImageResource(R.color.theme_col11);
				return;
			case 13:
				lin.setImageResource(R.color.theme_col12);
				return;
			case 14:
				lin.setImageResource(R.color.theme_col13);
				return;
			case 15:
				lin.setImageResource(R.color.theme_col14);
				return;
			case 16:
				lin.setImageResource(R.color.theme_col15);
				return;
			case 17:
				lin.setImageResource(R.color.theme_col16);
				return;
			case 18:
				lin.setImageResource(R.color.theme_col17);
				return;
			case 19:
				lin.setImageResource(R.color.theme_col18);
				return;
			default:
				lin.setImageResource(R.color.title);
				return;
		}
	}
}

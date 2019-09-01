package com.binxin.zdapp.classes;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;

import com.binxin.zdapp.R;
//import com.binxin.zdapp.MainApplication;
import com.binxin.zdapp.style.SystemBarTintManager;

public class MyThemes
{
	static int[] colorids = {
		R.color.title,
		R.color.title_old,
		R.color.theme_col1,
		R.color.theme_col2,
		R.color.theme_col3,
		R.color.theme_col4,
		R.color.theme_col5,
		R.color.theme_col6,
		R.color.theme_col7,
		R.color.theme_col8,
		R.color.theme_col9,
		R.color.theme_col10,
		R.color.theme_col11,
		R.color.theme_col12,
		R.color.theme_col13,
		R.color.theme_col14,
		R.color.theme_col15,
		R.color.theme_col16,
		R.color.theme_col17,
		R.color.theme_col18,
	};
	
	static int[] colorids2 = {
		R.color.title,
		R.color.title_old,
		R.color.theme_col1,
		R.color.theme_col2,
		R.color.theme_col3,
		R.color.theme_col4,
		R.color.theme_col5,
		R.color.theme_col6,
		R.color.theme_col7,
		R.color.theme_col8,
		R.color.theme_col9,
		R.color.theme_col10,
		R.color.theme_col11,
		R.color.theme_col12,
		R.color.theme_col13,
		R.color.theme_col14,
		R.color.theme_col15,
		R.color.theme_col16,
		R.color.theme_col17,
		R.color.black
	};
	public static void setThemeColor(Activity content)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			//状态栏
			content.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//导航栏
			//content.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			SystemBarTintManager tintManager = new SystemBarTintManager(content);
			tintManager.setStatusBarTintEnabled(true);
			//此处可以重新指定状态栏颜色
			//tintManager.setStatusBarTintResource(resid);
			tintManager.setStatusBarTintResource(colorids2[Common.APP_THEME_ID]);
			/*switch(Common.APP_THEME_ID)
			{
				case 0:
					tintManager.setStatusBarTintResource(colorids2[Common.APP_THEME_ID]);
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
			}*/
		}
	}
	public static void setThemes(Activity content,LinearLayout lin)
	{
		getThemes(lin);
		setThemeColor(content);
		//new MainApplication().getConfig(content);
	}
	public static void getThemes(View lin)
	{
		lin.setBackgroundResource(colorids[Common.APP_THEME_ID]);
	}
	
	public static void setImage(ImageView lin)
	{
		//lin.setBackgroundColor(Color.parseColor("#4F000000"));
		switch(Common.APP_THEME_ID)
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

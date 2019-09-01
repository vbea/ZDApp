package com.binxin.zdapp.activity;

import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.AdapterView;
import android.content.SharedPreferences;

import com.binxin.zdapp.R;
import com.binxin.zdapp.style.SystemBarTintManager;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.ThemeItem;
import com.binxin.zdapp.classes.ThemeAdapter;
import com.binxin.zdapp.classes.Common;

public class Set_theme extends Activity
{
	private List<ThemeItem> mItem = new ArrayList<ThemeItem>();
	private LinearLayout themeLayout;
	private ListView mListView;
	private ThemeAdapter adapter;
	private SharedPreferences spf;
	private SharedPreferences.Editor edt;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		setContentView(R.layout.theme);
		
		mListView = (ListView) findViewById(R.id.listTheme);
		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		themeLayout = (LinearLayout) findViewById(R.id.themeLayout);
		adapter = new ThemeAdapter(this);
		spf = getSharedPreferences("zdapp", MODE_PRIVATE);
		edt = spf.edit();

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
			{
				if (Common.APP_THEME_ID != p3)
					setThemes(p3);
			}
		});

		mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
			{
				if (Common.APP_THEME_ID != p3)
					setThemes(p3);
				else
					return false;
				return true;
			}
		});
		
		close.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});
	}
	
	private void getData()
	{
		int[] colorids = {
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
		String[] colornames = getResources().getStringArray(R.array.array_theme);
		if (colorids.length != colornames.length)
			return;
		for (int i=0;i<colorids.length;i++)
		{
			mItem.add(new ThemeItem(getResources().getColor(colorids[i]), colornames[i]));
		}
		adapter.setList(mItem);
		mListView.setAdapter(adapter);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		adapter.setWidth(themeLayout.getHeight());
		getData();
		//adapter.notifyDataSetChanged();
		super.onWindowFocusChanged(hasFocus);
	}
	
	
	//临时保存主题ID
	/*public void setThemeId(int tid)
	{
		if (mt_Code != tid)
		{
			mt_Code = tid;
			mTheme.setImage(act_img,mt_Code);
			mTheme.setThemes(mt_layout,mt_Code);
			if (mt_Code < 11)
				mt_Name.setText("颜色" + (mt_Code + 1));
			else if (mt_Code > 11)
				mt_Name.setText("颜色" + mt_Code);
			else
				mt_Name.setText("半透明");
		}
	}*/
	
	//保存主题设置
	public void setThemes(int code)
	{
		Common.APP_THEME_ID = code;
		MyThemes.setThemes(Set_theme.this, themeLayout);
		adapter.notifyDataSetChanged();
	}
	
	//初始化
	/*public void onStarted()
	{
		mTheme = new MyThemesInfo(this);
		SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		mt_oCode = sube.getInt("themeCode", 0);
		setThemeId(mt_oCode);
	}*/
	
	/*private void onChangeExited()
	{
		if (mt_Code != mt_oCode)
		{
			setTheme(android.R.style.Theme_DeviceDefault_Light);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示");
			builder.setMessage("设置已改变，是否保存？");
			builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int swich)
				{
					setThemes(mt_Code);
					onDestroy();
				}
			});
			builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int swich)
				{
					onDestroy();
				}
			});
			builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int swich)
				{
					dialog.cancel();
				}
			});
			builder.show();
		}
		else
		{
			onDestroy();
		}
	}*/

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		MyThemes.setThemes(this, themeLayout);
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		edt.putInt("themeCode", Common.APP_THEME_ID);
		edt.commit();
		//finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		super.onDestroy();
	}
	
}

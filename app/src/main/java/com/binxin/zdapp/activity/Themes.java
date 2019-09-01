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
import com.binxin.zdapp.classes.ThemeItem;
import com.binxin.zdapp.classes.ThemeAdapter;
import com.binxin.zdapp.classes.Common;
import com.binxin.zdapp.classes.MyThemes;

public class Themes extends Activity
{
	private List<ThemeItem> mItem = new ArrayList<ThemeItem>();
	private LinearLayout title;
	private ThemeAdapter adapter;
	private SharedPreferences spf;
	private SharedPreferences.Editor edt;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme);
		
		ListView mListView = (ListView) findViewById(R.id.listTheme);
		title = (LinearLayout) findViewById(R.id.themeLayout);
		ImageButton back = (ImageButton) findViewById(R.id.btn_back);
		spf = getSharedPreferences("java21", MODE_PRIVATE);
		edt = spf.edit();
		getData();
		adapter = new ThemeAdapter(this, mItem);
		mListView.setAdapter(adapter);
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
			{
				if (Common.APP_THEME_ID == p3)
					return;
				Common.APP_THEME_ID = p3;
				edt.putInt("theme", Common.APP_THEME_ID);
				edt.commit();
				MyThemes.setThemes(Themes.this, title);
				adapter.notifyDataSetChanged();
			}
		});
		
		mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
			{
				return Common.APP_THEME_ID != p3;
			}
		});
		
		back.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				onDestroy();
				finish();
			}
		});
	}
	
	private void getData()
	{
		int[] colorids = {
			/*R.color.title,
			R.color.title_2,
			R.color.title_3,
			R.color.title_4,
			R.color.title_5,
			R.color.title_6,
			R.color.title_7,
			R.color.title_8,
			R.color.title_9,
			R.color.title_10,
			R.color.title_11,
			R.color.title_12,
			R.color.title_13,
			R.color.title_14*/
		};
		String[] colornames = getResources().getStringArray(R.array.array_theme);
		if (colorids.length != colornames.length)
			return;
		for (int i=0;i<colorids.length;i++)
		{
			mItem.add(new ThemeItem(getResources().getColor(colorids[i]), colornames[i]));
		}
	}

	@Override
	protected void onResume()
	{
		MyThemes.setThemes(this, title);
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
}

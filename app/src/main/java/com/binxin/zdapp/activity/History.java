package com.binxin.zdapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Context;
import android.content.SharedPreferences;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class History extends Activity
{
	private LinearLayout titleLayout;
	/** Called when the activity is first created. */  
	@Override  
	public void onCreate(Bundle savedInstanceState) 
	{  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
	/*	Button bacc = (Button) findViewById(R.id.button06);
			
		bacc.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				finish();
			}
		});*/
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		MyThemes.setThemes(this, titleLayout);
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
}

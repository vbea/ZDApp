package com.binxin.zdapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.content.SharedPreferences;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class History_dev extends Activity
{
	private LinearLayout titleLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_dev);
		
		WebView web = (WebView) findViewById(R.id.hid_webView);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		
		WebSettings wset = web.getSettings();
		wset.setJavaScriptEnabled(true);
		web.loadUrl("file:///android_asset/web/history_dev.html");
		
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
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

package com.binxin.zdapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ImageButton;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class Syxy extends Activity
{
	private LinearLayout titleLayout;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		//setTheme(R.style.NTD);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.softsyxy);
		setFinishOnTouchOutside(true);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		//setTheme(R.style.NTD);
		/*WebView mView = (WebView) findViewById(R.id.webView2);
		WebSettings wset = mView.getSettings();
		wset.setJavaScriptEnabled(true);
		//打开asset文件夹目录
		mView.loadUrl("file:///android_asset/web/syxy.html");*/
		//打开本地SD卡内的文件
		//mView.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");
		//打开指定URL文件
		//mView.loadUrl("http://zde.yy.ai");
		close.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				//overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
	}

	public void setMyTheme()
	{
		android.content.SharedPreferences sube = getSharedPreferences("zdapp", android.content.Context.MODE_PRIVATE);
		int code = sube.getInt("themeCode", 0);
		MyThemes.getThemes(titleLayout, code);
	}
	
	@Override
	protected void onResume()
	{
		setMyTheme();
		super.onResume();
	}
	
}

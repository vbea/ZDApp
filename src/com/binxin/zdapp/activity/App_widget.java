package com.binxin.zdapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ImageButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.content.Intent;
import android.content.DialogInterface;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
/*import java.io.FileInputStream;
*import java.io.IOException;
*import java.io.InputStreamReader;*/

public class App_widget extends Activity 
{
	LinearLayout titleLayout;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_6_0);
				
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		WebView mView = (WebView) findViewById(R.id.webView3);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		WebSettings wset = mView.getSettings();
		wset.setJavaScriptEnabled(true);
		//打开asset文件夹目录
		mView.loadUrl("file:///android_asset/zdapp/widget.html");
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				finish();
			}
		});
	}
	public void setMyTheme()
	{
		android.content.SharedPreferences sube = getSharedPreferences("zdapp", android.content.Context.MODE_PRIVATE);
		int code = sube.getInt("themeCode", 0);
		MyThemes.setThemes(this, titleLayout, code);
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
		setMyTheme();
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
}

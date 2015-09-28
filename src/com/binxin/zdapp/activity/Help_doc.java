package com.binxin.zdapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.webkit.WebView;
import android.webkit.WebSettings;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class Help_doc extends Activity
{
	private LinearLayout titleLayout;
	@Override
	public void onCreate(Bundle savedIntanceState)
	{
		super.onCreate(savedIntanceState);
		setContentView(R.layout.helpdoc);
		
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		WebView mView = (WebView) findViewById(R.id.webView1);
		WebSettings wset = mView.getSettings();
		wset.setJavaScriptEnabled(true);
		//打开asset文件夹目录
		mView.loadUrl("file:///android_asset/web/help.html");
		//打开本地SD卡内的文件
		//mView.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");
		//打开指定URL文件
		//mView.loadUrl("http://zde.yy.ai");
		setMyTheme();
		close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
	}
	/*public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		// TODO Auto-generated method stub
		// return super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			new AlertDialog.Builder(this)
				.setIcon(R.drawable.zdapp)
				.setTitle("退出")
				.setMessage("退出正德应用？")
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog,int which)
					{
						// TODO Auto-generated method stub
						Intent startMain = new Intent(Intent.ACTION_MAIN);
						startMain.addCategory(Intent.CATEGORY_HOME);
						startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(startMain);
						//System.exit(0);
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create()
				.show();
			return false;
		}
		return false;
	}*/
	public void setMyTheme()
	{
		SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
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

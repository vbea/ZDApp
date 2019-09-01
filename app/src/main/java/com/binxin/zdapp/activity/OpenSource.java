package com.binxin.zdapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.webkit.WebView;
import android.webkit.WebSettings;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class OpenSource extends Activity
{
	private LinearLayout titleLayout;
	private SharedPreferences spf;
	private Editor edt;
	private CheckBox chk_gr;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.open_source);
		
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		chk_gr = (CheckBox) findViewById(R.id.chk_myosl);
		WebView mView = (WebView) findViewById(R.id.webCodeView);
		WebSettings wset = mView.getSettings();
		spf = getSharedPreferences("Codes", Context.MODE_PRIVATE);
		edt = spf.edit();
		wset.setJavaScriptEnabled(true);
		//打开asset文件夹目录
		mView.loadUrl("file:///android_asset/web/opensource.html");
		//打开本地SD卡内的文件
		//mView.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");
		//打开指定URL文件
		//mView.loadUrl("http://zde.yy.ai");
	
		close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		chk_gr.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				edt.putBoolean("downloadState",chk_gr.isChecked());
				if (!chk_gr.isChecked())
					edt.putBoolean("downloadCodeState",chk_gr.isChecked());
				edt.commit();
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
		chk_gr.setChecked(spf.getBoolean("downloadState",false));
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

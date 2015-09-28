package com.binxin.zdapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.DownloadListener;
import android.widget.ProgressBar;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.net.Uri;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class DownloadCode extends Activity
{
	private LinearLayout titleLayout;
	private ProgressBar webProgress;
	private WebView web;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.code_download);

		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		web = (WebView) findViewById(R.id.dow_webView);
		webProgress = (ProgressBar) findViewById(R.id.codeProgress);
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		WebSettings wset = web.getSettings();
		wset.setJavaScriptEnabled(true);
		web.loadUrl("file:///android_asset/web/download.html");
		//web.loadUrl("http://www.vbes.tk/vbea/zdapp/code/download.html");
		web.setWebViewClient(new WebViewClient());
		web.setDownloadListener(new MyWebViewDownLoadListener());
		
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		web.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				if (newProgress <= 50)
					webProgress.setSecondaryProgress(newProgress * 2);
				webProgress.setProgress(newProgress);
				webProgress.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
			}
		});
	}
	private class MyWebViewDownLoadListener implements DownloadListener
	{   
        @Override  
        public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype,long contentLength)
		{
			setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
			AlertDialog.Builder builder = new AlertDialog.Builder(DownloadCode.this);
			builder.setTitle("源码下载");
			builder.setMessage("正德应用提示：确定要下载该源码？\n(文件将保存在SD卡下的download文件夹中)");
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog,int swich)
				{
					Uri uri = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				}
			});
			builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog,int swich)
				{
					dialog.cancel();
				}
			});
			builder.create();
			builder.show();
        }   
    }
	
	public void setMyTheme()
	{
		SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		int code = sube.getInt("themeCode", 0);
		MyThemes.setThemes(this, titleLayout, code);
	}

	@Override
    //设置回退 
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法 
    public boolean onKeyDown(int keyCode, KeyEvent event)
	{ 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack())
		{ 
            web.goBack(); //goBack()表示返回WebView的上一页面 
            return true; 
        } 
        return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onPause()
	{
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
		super.onDestroy();
	} 
}

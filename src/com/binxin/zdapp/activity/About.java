package com.binxin.zdapp.activity;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.net.Uri;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.util.Log;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
//import com.binxin.zdapp.update.UpdateManager;
//import com.binxin.zdapp.update.ParseXmlService;
//import com.binxin.zdapp.update.UpdateDialog;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.*;

public class About extends Activity
{
	private Button btn_new,btn_web,btn_t,btn_hist,btn_abt,btn_syxy,btn_wel,btn_vbea,btn_score,btn_feed;
	private LinearLayout titleLayout,aboutLayout;
	private HashMap<String, String> mHashMap = null;
	private ProgressDialog psd;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);	
		//android.R.style.Theme_Material_Light
		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		btn_new = (Button) findViewById(R.id.abt_new);
		btn_web = (Button) findViewById(R.id.abt_web);
		btn_t   = (Button) findViewById(R.id.abt_t);
		btn_hist= (Button) findViewById(R.id.abt_hist);
		btn_abt = (Button) findViewById(R.id.abt_abt);
		btn_syxy= (Button) findViewById(R.id.abt_syxy);
		btn_vbea= (Button) findViewById(R.id.abt_vbea);
		btn_wel = (Button) findViewById(R.id.abt_welcome);
		//btn_feed = (Button) findViewById(R.id.abt_feedback);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		aboutLayout = (LinearLayout) findViewById(R.id.abthemeLayout);
		//psd = new ProgressDialog(this);
		setMyTheme();
		//返回
		close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		//检查更新
		btn_new.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if(isNetWork())
				{
					setTheme(android.R.style.Theme_DeviceDefault_Light);
					final Dialog dg = psd.show(About.this,null,"检查更新中…",false,true);
					dg.show();
					UmengUpdateAgent.setUpdateAutoPopup(false);
					UmengUpdateAgent.setUpdateListener(new UmengUpdateListener()
					{
						@Override
						public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo)
						{
							switch (updateStatus)
							{
								case UpdateStatus.Yes: // has update
									dg.cancel();
									UmengUpdateAgent.showUpdateDialog(About.this, updateInfo);
									break;
								case UpdateStatus.No: // has no update
									dg.cancel();
									Toast.makeText(getApplicationContext(), "已是最新版本", Toast.LENGTH_SHORT).show();
									break;
									/*case UpdateStatus.NoneWifi: // none wifi
									 Toast.makeText(getApplicationContext(), "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
									 break;*/
								case UpdateStatus.Timeout: // time out
									dg.cancel();
									Toast.makeText(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT).show();
									break;
							}
						}
					});
					UmengUpdateAgent.forceUpdate(About.this);
				}
				else
				{
					Toast.makeText(About.this,"请检查你的网络连接",Toast.LENGTH_SHORT).show();
				}
			}
		});
		//官方网站
		btn_web.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent= new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse("http://www.vbes.tk");
				intent.setData(content_url);
				startActivity(intent);
			}
		});
		//官方微博
		btn_t.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent= new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse("http://t.qq.com/zde-binxin");
				intent.setData(content_url);
				startActivity(intent);
			}
		});
		//历史版本
		btn_hist.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent= new Intent(About.this,History.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		//软件说明
		btn_abt.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent= new Intent(About.this,More.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		//使用协议
		btn_syxy.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent syxy = new Intent(About.this,Syxy.class);
				startActivity(syxy);
			}
		});
		//欢迎页
		btn_wel.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent welcome = new Intent(About.this,Welcome.class);
				startActivity(welcome);
				//About.this.finish();
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		//联系我们
		btn_vbea.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent vbea = new Intent(About.this,Vbea_about.class);
				startActivity(vbea);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		
	}
	private boolean isNetWork()
	{
		boolean net = false;
		ConnectivityManager conm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conm.getActiveNetworkInfo();
		if (info != null && info.isAvailable())
			net = true;
		return net;
	}
	
	public void setMyTheme()
	{
		SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		int code = sube.getInt("themeCode", 0);
		//MyThemes.getThemes(titleLayout, code);
		if (code != 11)
			MyThemes.setThemes(this,aboutLayout, code);
		else
			MyThemes.setThemes(this,aboutLayout, 0);
			//MyThemes.getThemes(titleLayout, 0);
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


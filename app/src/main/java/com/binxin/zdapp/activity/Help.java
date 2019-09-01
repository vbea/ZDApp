package com.binxin.zdapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.view.View;
import android.net.Uri;
import android.content.Intent;
import android.content.Context;
import android.content.ComponentName;
import android.content.SharedPreferences;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
import com.umeng.fb.FeedbackAgent;

public class Help extends Activity
{
	private LinearLayout titleLayout,downLayout;
	private FeedbackAgent agent;
	private int clickSum = 0;
	private SharedPreferences shp;
	/** Called when the activity is first created. */  
	@Override  
	public void onCreate(Bundle savedInstanceState) 
	{  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		Button btn_feedback = (Button) findViewById(R.id.hep_feedback);
		Button btn_score=(Button) findViewById(R.id.abt_appscore);
		Button btn_help = (Button) findViewById(R.id.hep_helpd);
		Button btn_code = (Button) findViewById(R.id.hep_fcode);
		Button btn_hdev = (Button) findViewById(R.id.hep_fhis);
		Button btn_down = (Button) findViewById(R.id.hep_down);
		Button btn_anq = (Button) findViewById(R.id.hep_anq);
		downLayout = (LinearLayout) findViewById(R.id.hep_downLayout);
		shp = getSharedPreferences("Codes", Context.MODE_PRIVATE);
		agent = new FeedbackAgent(this);
		agent.sync();
		
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		btn_feedback.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				agent.startFeedbackActivity();
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		//给软件评分
		btn_score.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					/*Intent intent = new Intent();
					intent.setClassName("com.tencent.android.qqdownloader", "com.qq.AppService.StartApp");
					intent.setAction(Intent.ACTION_MAIN);
					intent.addCategory("APP_MARKET");
					intent.setData(Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.binxin.zdapp&g_f=993187"));
					startActivity(intent);*/
					Uri uri = Uri.parse("market://details?id="+getPackageName());
					Intent intent = new Intent(Intent.ACTION_VIEW,uri);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
				catch (Exception e)
				{
					//Toast.makeText(getApplicationContext(), "你的手机未安装应用宝", Toast.LENGTH_SHORT).show();
					Uri uri = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.binxin.zdapp&g_f=993187");
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
			}
		});
		btn_help.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				startActivity(new Intent(Help.this, Help_doc.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		btn_hdev.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				startActivity(new Intent(Help.this,History_dev.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		btn_anq.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				startActivity(new Intent(Help.this,Vbea_anq.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		btn_down.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				SharedPreferences.Editor edt = shp.edit();
				boolean state = shp.getBoolean("downloadCodeState",false);
				if (state)
				{
					startActivity(new Intent(Help.this,DownloadCode.class));
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
				else
				{
					if (clickSum < 5)
						clickSum++;
					else
					{
						edt.putBoolean("downloadCodeState",true);
						if (edt.commit())
							Toast.makeText(getApplicationContext(),"你现在已经打开了下载功能",Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		btn_code.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				startActivity(new Intent(Help.this,OpenSource.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
	}
	public void setMyTheme()
	{
		MyThemes.setThemes(this, titleLayout);
		downLayout.setVisibility(shp.getBoolean("downloadState",false)?View.VISIBLE:View.GONE);
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

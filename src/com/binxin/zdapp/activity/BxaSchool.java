package com.binxin.zdapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.ComponentName;
import android.net.Uri;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.CheckBxaPackage;
import android.widget.LinearLayout;

public class BxaSchool extends Activity
{
	private CheckBxaPackage cbp = new CheckBxaPackage();
	private LinearLayout titleLayout,javaIns,javanoIns;
	private TextView java_read,java_api,java_active,java_down;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate (savedInstanceState);
		setContentView(R.layout.bxa);
		
		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		javaIns = (LinearLayout) findViewById(R.id.bxa_java_isInstall);
		javanoIns = (LinearLayout) findViewById(R.id.bxa_java_notInstall);
		java_api = (TextView) findViewById(R.id.bxa_btn_java_api);
		java_read = (TextView) findViewById(R.id.bxa_btn_java_ready);
		java_active = (TextView) findViewById(R.id.bxa_btn_java_active);
		java_down = (TextView) findViewById(R.id.bxa_java_down);
		init_java();
		//back
		close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
	}
	
	protected void init_java()
	{
		if(cbp.isAvilible(getApplicationContext(),"com.vbea.java21"))
			javanoIns.setVisibility(View.GONE);
		else
			javaIns.setVisibility(View.GONE);
		
		java_api.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					Intent java = new Intent();
					ComponentName cn = new ComponentName("com.vbea.java21",
														 "com.vbea.java21.ApiWord");
					java.setComponent(cn);
					java.setAction("android.intent.action.VIEW");
					startActivityForResult(java, RESULT_OK);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
				catch (Exception err)
				{
					Toast.makeText(getApplicationContext(),"未检测到应用或版本过低",Toast.LENGTH_SHORT).show();
				}
			}
		});
		java_read.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					Intent option5 = new Intent();
					ComponentName cn = new ComponentName("com.vbea.java21",
															 "com.vbea.java21.Chapter");
					option5.setComponent(cn);
					option5.setAction("android.intent.action.VIEW");
					startActivityForResult(option5, RESULT_OK);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
				catch (Exception err)
				{
					Toast.makeText(getApplicationContext(),"未检测到应用或版本过低",Toast.LENGTH_SHORT).show();
				}
			}
		});
		java_active.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					Intent option5 = new Intent();
					ComponentName cn = new ComponentName("com.vbea.java21",
															 "com.vbea.java21.Invitation");
					option5.setComponent(cn);
					option5.setAction("android.intent.action.VIEW");
					startActivityForResult(option5, RESULT_OK);
					//overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
				catch (Exception err)
				{
					Toast.makeText(getApplicationContext(),"未检测到应用或版本过低",Toast.LENGTH_SHORT).show();
				}
			}
		});
		java_down.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					Intent intent = new Intent();
					intent.setClassName("com.tencent.android.qqdownloader", "com.tencent.assistant.link.LinkProxyActivity");
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setData(Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.vbea.java21"));
					startActivity(intent);
				}
				catch (Exception e)
				{
					Uri uri = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.vbea.java21");
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				}
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

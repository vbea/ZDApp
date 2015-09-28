package com.binxin.zdapp.activity;

import android.app.Activity;
import android.app.NativeActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import com.binxin.zdapp.colors.Main;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class App_hardware extends Activity
{
/**首次定义活动类*/
	private ImageButton back;
	private Button audio, tuch, sco, seo, sen;
	private LinearLayout titleLayout;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hardware);
		//定义按钮
		audio = (Button) findViewById(R.id.btn_adotest);
		back = (ImageButton) findViewById(R.id.btn_close);
		tuch = (Button) findViewById(R.id.btn_touchTest);
		sco = (Button) findViewById(R.id.btnSC);
		seo = (Button) findViewById(R.id.btn_sensorTest);
		sen = (Button) findViewById(R.id.btn_hardv);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		//配置按钮方法
		audio.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(App_hardware.this,App_audiotest.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		sco.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent3 = new Intent();
				intent3.setClass(App_hardware.this,Main.class);
				startActivity(intent3);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		seo.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent5 = new Intent(App_hardware.this,App_sensor.class);
				startActivity(intent5);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		sen.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent6 = new Intent(App_hardware.this,DeviceQuery.class);
				startActivity(intent6);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		tuch.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent4 = new Intent(App_hardware.this,PointerLocation.class);
				startActivity(intent4);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

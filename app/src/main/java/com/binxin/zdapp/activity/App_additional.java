package com.binxin.zdapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.content.ComponentName;
import android.content.SharedPreferences;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class App_additional extends Activity
{
	private LinearLayout titleLayout;
	/** Called when the activity is first created. */  
	@Override  
	public void onCreate(Bundle savedInstanceState) 
	{  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additional);

		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		Button btn_optcal = (Button)findViewById(R.id.adt_calculator);
		Button btn_vidbox = (Button)findViewById(R.id.adt_videoBox);
		Button btn_joke = (Button) findViewById(R.id.adt_joke);
		Button btn_wifi = (Button) findViewById(R.id.adt_wifiap);
		Button btn_logview = (Button) findViewById(R.id.adt_logview);
		Button btn_poweradv = (Button) findViewById(R.id.adt_advpower);
		Button btn_bxa = (Button) findViewById(R.id.adt_bxastudy);
		Button btn_key = (Button) findViewById(R.id.adt_ckey);
		
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		btn_optcal.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				startActivity(new Intent(App_additional.this, App_calculator.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		btn_vidbox.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					Intent option5 = new Intent();
					ComponentName cn = new ComponentName("com.vbea.video",
															 "com.vbea.video.MainActivity");
					option5.setComponent(cn);
					option5.setAction("android.intent.action.MAIN");
					startActivityForResult(option5, RESULT_OK);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
					//startActivity(new Intent(App_additional.this, SelectAudio.class));
				}
				catch (Exception err)
				{
					Toast.makeText(App_additional.this,"请先安装正德应用扩展应用包",Toast.LENGTH_SHORT).show();
				}
			}
		});
		btn_joke.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					Intent option5 = new Intent();
					ComponentName cn = new ComponentName("com.vbea.zdapp.bxa",
															 "com.vbea.zdapp.bxa.MainActivity");
					option5.setComponent(cn);
					option5.setAction("android.intent.action.MAIN");
					startActivityForResult(option5, RESULT_OK);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
				catch (Exception err)
				{
					Toast.makeText(getApplicationContext(),"请先安装正德应用扩展应用包",Toast.LENGTH_SHORT).show();
				}
			}
		});
		btn_wifi.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				startActivity(new Intent(App_additional.this,App_wifiap.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		btn_logview.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				startActivity(new Intent(App_additional.this,LogView.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		btn_poweradv.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				startActivity(new Intent(App_additional.this,AdvancePowerManagement.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		btn_bxa.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				startActivity(new Intent(App_additional.this, BxaSchool.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		btn_key.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				//finish();
				startActivity(new Intent(App_additional.this, PasswordKey.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		MyThemes.setThemes(this,titleLayout);
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}

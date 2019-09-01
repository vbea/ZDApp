package com.binxin.zdapp;

import java.lang.InterruptedException;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Context;
import android.app.AlertDialog.Builder;
import android.widget.TextView;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.ExceptionHandler;
import com.binxin.zdapp.classes.Common;

public class MainActivity extends Activity
{
	private int mWelcome;
	//private TextView Welcometext;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{ 
		//ExceptionHandler handler = ExceptionHandler.getInstance();
		//handler.init(getApplicationContext());
		super.onCreate(savedInstanceState);
		//setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		setTheme(android.R.style.Theme_DeviceDefault_Wallpaper);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
								  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(0x08000000,0x8000000);
		setContentView(R.layout.main);
		SharedPreferences welcome = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		mWelcome = welcome.getInt("verCode", 0);
		Common.APP_THEME_ID = welcome.getInt("themeCode", 0);
		new MyWelcome().start();
		//new MyWelcome().onStop();
	} 
	class MyWelcome extends Thread implements Runnable
	{
		@Override 
		public void run()
		{
			try
			{
				if (mWelcome == getResources().getInteger(R.integer.versionCode))
				{
					Thread.sleep(2000);
					Intent intent=new Intent();
					intent.setClass(MainActivity.this,com.binxin.zdapp.activity.Main.class);
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					//MainActivity.this.finish();
				}
				else
				{
					Thread.sleep(3000);
					Intent intent=new Intent(MainActivity.this,com.binxin.zdapp.activity.Welcome.class);
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					//MainActivity.this.finish();
				}
			}
			catch(Exception e)
			{ 
				ExceptionHandler.log(e.toString());
			}
			finally
			{
				finish();
			}
		}
    }
}

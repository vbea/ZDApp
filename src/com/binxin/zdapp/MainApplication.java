package com.binxin.zdapp;

import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

public class MainApplication extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		getConfig(getApplicationContext());
	}
	
	public final void getConfig(Context context)
	{
		SharedPreferences spf = context.getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		String languageLocal = spf.getString("language", "");
		Locale locale;
		if (languageLocal.equals("zh") || languageLocal.equals("en"))
			locale = new Locale(languageLocal);
		else
			locale = context.getResources().getConfiguration().locale;
		//Locale.setDefault(locale);
		Configuration config = context.getResources().getConfiguration();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		config.locale = locale;
		context.getResources().updateConfiguration(config, metrics);
	}
}

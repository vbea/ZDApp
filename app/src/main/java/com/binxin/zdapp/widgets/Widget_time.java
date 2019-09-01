package com.binxin.zdapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.RemoteViews;

import com.binxin.zdapp.R;

public class Widget_time extends AppWidgetProvider 
{
	public void onReceive(Context context, Intent intent) 
	{
		String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) 
			{

			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_time);

			// 点击后打开闹钟
			Intent alarmClockIntent = getAlarmClockIntent(context);
			if (alarmClockIntent != null) 
			{
				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, 0, alarmClockIntent, 0);
				views.setOnClickPendingIntent(R.id.Widget, pendingIntent);
			}

			AppWidgetManager
					.getInstance(context)
					.updateAppWidget(
							intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS),
							views);
		}
	}

	private Intent getAlarmClockIntent(Context context) 
	{
		PackageManager packageManager = context.getPackageManager();

		String clockImpls[][] = 
		{
				{ "HTC Alarm Clock", "com.htc.android.worldclock",
						"com.htc.android.worldclock.WorldClockTabControl" },
				{ "Standar Alarm Clock", "com.android.deskclock",
						"com.android.deskclock.AlarmClock" },
				{ "Froyo Nexus Alarm Clock", "com.google.android.deskclock",
						"com.android.deskclock.DeskClock" },
				{ "Moto Blur Alarm Clock", "com.motorola.blur.alarmclock",
						"com.motorola.blur.alarmclock.AlarmClock" },
				{ "Samsung Galaxy Clock", "com.sec.android.app.clockpackage",
						"com.sec.android.app.clockpackage.ClockPackage" },
				{ "ZTE U985 Clock","zte.com.cn.alarmclock","zte.com.cn.alarmclock.ClockPackage"}
		};

		for (int i = 0; i < clockImpls.length; i++) 
		{
			String vendor = clockImpls[i][0];
			String packageName = clockImpls[i][1];
			String className = clockImpls[i][2];
			try 
			{
				ComponentName cn = new ComponentName(packageName, className);
				ActivityInfo aInfo = packageManager.getActivityInfo(cn,
						PackageManager.GET_META_DATA);
				Intent alarmClockIntent = new Intent(Intent.ACTION_MAIN)
						.addCategory(Intent.CATEGORY_LAUNCHER);
				alarmClockIntent.setComponent(cn);
				return alarmClockIntent;
			}
			catch (NameNotFoundException e) 
			{
			}
		}

		return null;
	}
}

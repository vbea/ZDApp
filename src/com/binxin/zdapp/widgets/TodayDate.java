package com.binxin.zdapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.widget.RemoteViews;

import com.binxin.zdapp.R;

public class TodayDate extends AppWidgetProvider
{
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		RemoteViews updateView = buildUpdate(context);
		appWidgetManager.updateAppWidget(appWidgetIds, updateView);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	private String[] months = {"1月", "2月", "3月", "4月",
	                              "5月", "6月", "7月", "8月",
	                              "9月", "10月", "11月", "12月"};
	private String[] days = {"星期日", "星期一", "星期二", "星期三",
	                              "星期四", "星期五", "星期六"};
	private RemoteViews buildUpdate(Context context)
	{
		RemoteViews updateView = null;
		Time time = new Time();
		time.setToNow();
		String month = time.year + "年" + months[time.month] + " " /*+ time.year*/;
		updateView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		updateView.setTextViewText(R.id.Date, new Integer(time.monthDay).toString());
		updateView.setTextViewText(R.id.Month, month);
		updateView.setTextViewText(R.id.WeekDay, days[time.weekDay]);
		Intent launchIntent = new Intent();
		launchIntent.setComponent(new ComponentName("com.binxin.zdapp", "com.binxin.zdapp.MainActivity"));
		launchIntent.setAction(Intent.ACTION_MAIN);
		launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		PendingIntent intent = PendingIntent.getActivity(context, 0, launchIntent, 0);
		updateView.setOnClickPendingIntent(R.id.Base, intent);
		return updateView;
	}
}

package com.binxin.zdapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionChangeReceiver extends BroadcastReceiver
{
	public boolean IsNetWork = false;
	@Override
	public void onReceive(Context context, Intent intent)
	{
		ConnectivityManager conm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conm.getActiveNetworkInfo();
		if (info != null && info.isAvailable())
			IsNetWork = true;
		else
			IsNetWork = false;
	}
	
	
}

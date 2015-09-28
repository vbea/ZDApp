package com.binxin.zdapp.update;

import android.app.ProgressDialog;
import android.content.Context;
import java.util.*;
import android.app.*;

public class UpdateDialog //extends ProgressDialog
{
	private Context mContext;
	private static ProgressDialog pd;
	private static boolean isShowing = false;
	
	//private UpdateDialog(){}
	private static UpdateDialog instance = null;
	
	public static synchronized UpdateDialog getIntance(Context context)
	{
		if (instance == null)
			instance = new UpdateDialog(context);
		return instance;
	}
	private UpdateDialog(Context context)
	{
		this.mContext = context;
		pd = new ProgressDialog(mContext);
		pd.setMessage("检查更新中……");
		pd.setCanceledOnTouchOutside(false);
		//Dialog notice = pd.showDialog();
	}
	
	public void showDialog()
	{
		pd.show();
		isShowing = true;
	}
	
	public void closeDialog()
	{
		if (pd != null && pd.isShowing()&&isShowing)
		{
			pd.dismiss();
			isShowing = false;
		}
	}
}

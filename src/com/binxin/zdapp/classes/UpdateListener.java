package com.binxin.zdapp.classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
/*
import com.tencent.updatesdk.DialogActivity;
import com.tencent.updatesdk.IUpdateListener;
import com.tencent.updatesdk.UpdateSDK;*/

public class UpdateListener //implements IUpdateListener
{
	private Context mContext;

	public UpdateListener(Context context)
	{
		super();
		this.mContext = context;
	}
/**已取消该功能
	@Override
	public void showUpdateDialog(String appName, String newVersionFeature, String versionName, String versionCode, String fileSize, String diffFileSize)
	{
		// 显示普通升级对话框
		AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
		adb.setMessage("有新版本啦！是否现在升级呢");
		adb.setPositiveButton("升级", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				//进行更新，false表示优先采用省流量升级
				UpdateSDK.update(false);
			}
		});
		adb.setNegativeButton("不了", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		adb.show();
	}

	@Override
	public void showForceUpdateDialog(String appName, String newVersionFeature, String versionName, String versionCode, String fileSize, String diffFileSize)
	{
		// 显示强制升级对话框
		AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
		adb.setMessage("啊，必须升级到新版本哦！");
		adb.setPositiveButton("升级", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				//进行更新，false表示优先采用省流量升级
				UpdateSDK.update(false);
			}
		});
		adb.setNegativeButton("不了", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				//退出程序
				dialog.dismiss();
			}
		});
		adb.show();
	}

	@Override
	public void onCheckUpdateError()
	{
		// 检查更新失败
		AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
		adb.setMessage("检查更新失败");
		adb.show();
	}

	@Override
	public void onNoUpdate()
	{
		AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
		adb.setMessage("恭喜您，已经是最新版本啦");
		adb.show();
	}

	@Override
	public void showDownloadFinishDialog(String appName)
	{
		// 下载完成
		AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
		adb.setMessage("下载完成了，要不要安装呢？");
		adb.setPositiveButton("安装", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				UpdateSDK.install();
			}
		});
		adb.setNegativeButton("现在不装", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		adb.show();
	}

	@Override
	public void showDownloadFailedDialog(String appName, boolean deltaUpdateFailed)
	{
		// 下载完成
		AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
		adb.setMessage("下载失败了，要不要重试呢？");
		adb.setPositiveButton("重试", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				UpdateSDK.retryDownload();
			}
		});
		adb.setNegativeButton("不要", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		adb.show();
	}*/
}

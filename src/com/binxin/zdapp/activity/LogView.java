package com.binxin.zdapp.activity;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.net.Uri;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.Common;
import com.binxin.zdapp.classes.ExceptionHandler;
import com.binxin.zdapp.view.BorderScrollView;

public class LogView extends Activity
{
	private TextView mView,oView,fileName,ltitle;
	private LinearLayout titleLayout,linlayout;
	private StringBuilder sbu;
	private BorderScrollView mScroll;
	private AlertDialog.Builder builder;
	private String logFile = "";//文件路径
	private boolean isExtern = false;//是否外部调用
	private boolean isBigFile = false;//是否大文件
	private boolean canRead = true;//能否读取
	private boolean readOver = false;//是否读完
	private String charSet = "UTF-8";//字符集
	private int chitem = 0;//字符集选项
	private char[] b;//单元字符数组
	private MyThread myThread;//加载文件线程
	private boolean interrupted = true;//线程状态
	//流
	FileInputStream fis;
	BufferedInputStream bis;
	InputStreamReader isr;
	@Override
	public void onCreate(Bundle savedIntanceState)
	{
		super.onCreate(savedIntanceState);
		setContentView(R.layout.logview);

		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		linlayout = (LinearLayout) findViewById(R.id.logTilinlayout);
		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		Button btn_charset = (Button) findViewById(R.id.log_btnCharset);
		mView = (TextView) findViewById(R.id.txtLogView);
		oView = (TextView) findViewById(R.id.logMoreView);
		ltitle = (TextView) findViewById(R.id.log_txtitle);
		fileName = (TextView) findViewById(R.id.log_Filename);
		mScroll = (BorderScrollView) findViewById(R.id.logviewScrollView);
		//mView.setTextIsSelectable(true);
		logFile = Environment.getExternalStorageDirectory()+"/ZDApp/Log/ZDApp.log";
		Intent intent = getIntent();
		if (intent != null && intent.getAction() != null)
		{
			if (intent.getAction().equals(Intent.ACTION_VIEW))
			{
				Uri uri = intent.getData();
				logFile = uri.getPath();
				isExtern = true;
				oView.setVisibility(View.GONE);
				ltitle.setText(R.string.txtReader);
			}
		}
		loadLogView();
		close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		if (isExtern)
		{
			btn_charset.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					final String[] items = getResources().getStringArray(R.array.array_charset);
					setTheme(android.R.style.Theme_DeviceDefault_Light);
					builder = new AlertDialog.Builder(LogView.this);
					builder.setTitle(R.string.chooscharset);
					builder.setSingleChoiceItems(items, chitem, new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int item)
						{
							if (chitem != item)
							{
								chitem = item;
								charSet = items[chitem];
								canRead = false;
								closeStream();
								loadLogView();
							}
							dialog.cancel();
						}
					});
					builder.show();
				}
			});
		}
		if (isBigFile)
		{
			mScroll.setOnBorderListener(new BorderScrollView.OnBorderListener()
			{
				@Override
				public void onBottom()
				{
					canRead = true;
				}
			
				@Override
				public void onTop()
				{
				}
			});
		}
	}
	
	private void loadLogView()
	{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			try
			{
				File file = new File(logFile);
				if (file.exists())
				{
					if (isExtern)
					{
						fileName.setText(file.getName());
						linlayout.setVisibility(View.VISIBLE);
					}
					fis = new FileInputStream(file);
					if (fis.available() <= 4096)
					{
						byte[] b = new byte[fis.available()];
						fis.read(b);
						String result = new String(b, charSet);
						mView.setText(result);
						fis.close();
					}
					else
					{
						bis = new BufferedInputStream(fis);
						loadBigFile(bis);
					}
				}
				else
				{
					oView.setText("没有找到日志文件");
				}
			}
			catch (IOException e)
			{
				oView.setText("无法读取日志文件，请重试");
			}
		}
		else
			oView.setText("当前未挂载SD卡");
	}
	
	private void loadBigFile(final BufferedInputStream bis)
	{
		interrupted = true;
		isBigFile = true;
		readOver = false;
		canRead = true;
		sbu = new StringBuilder();
		mView.setText("");
		oView.setVisibility(View.GONE);
		myThread = new MyThread();
		myThread.start();
	}
	
	public class MyThread extends Thread implements Runnable
	{
		public void run()
		{
			try
			{
				isr = new InputStreamReader(bis, charSet);
				while (!readOver && interrupted)
				{
					if (canRead)
					{
						for (int i = 0; i < 2; i++)
						{
							if (!canRead)
								break;
							b = new char[512];
							if (isr.read(b) > 0)
								sbu.append(b);
							else
							{
								readOver = true;
								mHandle.sendEmptyMessage(2);
								break;
							}
						}
						mHandle.sendEmptyMessage(1);
						canRead = false;
					}
					Thread.sleep(500);
				}
			}
			catch (Exception e)
			{
				ExceptionHandler.log("loadBigTextFile():"+e.toString());
				mHandle.sendEmptyMessage(3);
			}
		}
	}
	
	Handler mHandle = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case 1:
					mView.setText(sbu.toString());
					break;
				case 2:
					oView.setText("-End-");
					oView.setVisibility(View.VISIBLE);
					closeStream();
					break;
				case 3:
					Common.showShortToast(getApplicationContext(), "获取文件内容出错");
					closeStream();
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	private void closeStream()
	{
		try
		{
			if (fis != null)
				fis.close();
			if (bis != null)
				bis.close();
			if (isr != null)
				isr.close();
			interrupted = false;
		}
		catch (Exception e)
		{
			ExceptionHandler.log("LogView_CloseStream():"+e.toString());
		}
	}
	
	public void setMyTheme()
	{
		SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		int code = sube.getInt("themeCode", 0);
		MyThemes.setThemes(this, titleLayout, code);
	}

	@Override
	protected void onPause()
	{
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
		isExtern = false;
		closeStream();
		super.onDestroy();
	}
}

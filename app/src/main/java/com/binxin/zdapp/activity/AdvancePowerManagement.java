package com.binxin.zdapp.activity;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.Common;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.ProgressClass;

public class AdvancePowerManagement extends Activity
{
	private boolean isRoot = false;
	public final int PM_POWER_OFF = 1, PM_POWER_FAST = 2, PM_REBOOT = 3, PM_REBOOT_FAST = 4, PM_RECOVERY = 5, PM_BOOTLOADER = 6, PM_START = 0;
	public final int PM_ERROR = 0x0010,IS_ROOT = 0x0011,SET_NULL = 0x0012,DISSMISS = 0x0013;
	private LinearLayout titleLayout;
	private Button btn_poweroff,btn_reboot,btn_powerfast,btn_rebootf,btn_recovery,btn_bootloader;
	private getRooted execed;
	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adv_power);
		setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		btn_poweroff = (Button) findViewById(R.id.apm_poweroff);
		btn_powerfast = (Button) findViewById(R.id.apm_poweroffast);
		btn_reboot = (Button) findViewById(R.id.apm_reboot);
		btn_rebootf = (Button) findViewById(R.id.apm_rebootfast);
		btn_recovery = (Button) findViewById(R.id.apm_recovery);
		btn_bootloader = (Button) findViewById(R.id.apm_bootloader);
		
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		btn_poweroff.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				/*Intent intent = new Intent(Intent.ACTION_REBOOT);
				intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
				sendBroadcast(intent, android.Manifest.permission.REBOOT);*/
				execed = new getRooted(PM_POWER_OFF);
				execed.start();
				dialog = new ProgressDialog(AdvancePowerManagement.this);
				dialog.setMessage(getStrings(R.string.power_off_ing));
				dialog.setCancelable(false);
				dialog.show();
			}
		});
		btn_reboot.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				execed = new getRooted(PM_REBOOT);
				execed.start();
				dialog = new ProgressDialog(AdvancePowerManagement.this);
				dialog.setMessage(getStrings(R.string.power_reboot_ing));
				dialog.setCancelable(false);
				dialog.show();
			}
		});
		btn_recovery.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				execed = new getRooted(PM_RECOVERY);
				execed.start();
				dialog = new ProgressDialog(AdvancePowerManagement.this);
				dialog.setMessage(getStrings(R.string.power_reboot_ed) + " Recovery...");
				dialog.setCancelable(false);
				dialog.show();
			}
		});
		btn_bootloader.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				execed = new getRooted(PM_BOOTLOADER);
				execed.start();
				dialog = new ProgressDialog(AdvancePowerManagement.this);
				dialog.setMessage(getStrings(R.string.power_reboot_ed) + " Bootloader...");
				dialog.setCancelable(false);
				dialog.show();
			}
		});
	}
	
	final class getRooted extends Thread
	{
		private int _mode;
		public getRooted(int mode)
		{
			_mode = mode;
		}
		@Override
		public void run()
		{
			switch (_mode)
			{
				case PM_START:
				{
					try
					{
						sleep(2000);
						ProgressClass.RootCmd(getPackageName());
						isRoot = ProgressClass.ExecCmd("cd /cache");
						mHandler.sendEmptyMessage(IS_ROOT);
					}
					catch (InterruptedException e)
					{
						isRoot = false;
						mHandler.sendEmptyMessage(IS_ROOT);
						//Common.showShortToast(AdvancePowerManagement.this,e.getMessage());
					}
				}
				break;
				case PM_POWER_OFF:
				{
					try
					{
						sleep(1000);
						ProgressClass.RootCmd(getPackageResourcePath(),true);
						sleep(3000);
						mHandler.sendEmptyMessage(PM_POWER_OFF);
					}
					catch (InterruptedException e)
					{
						mHandler.sendEmptyMessage(DISSMISS);
					}
				}
				break;
				case PM_REBOOT:
				{
					try
					{
						sleep(1000);
						ProgressClass.RootCmd(getPackageResourcePath(),true);
						sleep(3000);
						mHandler.sendEmptyMessage(PM_REBOOT);
					}
					catch (InterruptedException e)
					{
						mHandler.sendEmptyMessage(DISSMISS);
					}
				}
				break;
				case PM_RECOVERY:
				{
					try
					{
						sleep(1000);
						ProgressClass.RootCmd(getPackageResourcePath(),true);
						sleep(3000);
						mHandler.sendEmptyMessage(PM_RECOVERY);
					}
					catch (InterruptedException e)
					{
						mHandler.sendEmptyMessage(DISSMISS);
					}
				}
				break;
				case PM_BOOTLOADER:
				{
					try
					{
						sleep(1000);
						ProgressClass.RootCmd(getPackageResourcePath(),true);
						sleep(3000);
						mHandler.sendEmptyMessage(PM_BOOTLOADER);
					}
					catch (InterruptedException e)
					{
						mHandler.sendEmptyMessage(DISSMISS);
					}
				}
				break;
			}
		}
	}
	
	/*final Runnable mRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			
		}
	};*/
	
	final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case PM_ERROR:
					Common.showShortToast(AdvancePowerManagement.this, "无法获取Root权限，请重试");
					break;
				case DISSMISS:
					if (dialog != null)
						dialog.dismiss();
					break;
				case SET_NULL:
					/*if (execed != null)
						execed = null;*/
					break;
				case PM_POWER_OFF:
					ProgressClass.RootCmd("reboot -p");
					mHandler.sendEmptyMessage(DISSMISS);
					mHandler.sendEmptyMessage(PM_ERROR);
					break;
				case PM_REBOOT:
					ProgressClass.RootCmd("reboot");
					mHandler.sendEmptyMessage(DISSMISS);
					mHandler.sendEmptyMessage(PM_ERROR);
					break;
				case PM_RECOVERY:
					ProgressClass.RootCmd("reboot recovery");
					mHandler.sendEmptyMessage(DISSMISS);
					mHandler.sendEmptyMessage(PM_ERROR);
					break;
				case PM_BOOTLOADER:
					ProgressClass.RootCmd("reboot bootloader");
					mHandler.sendEmptyMessage(DISSMISS);
					mHandler.sendEmptyMessage(PM_ERROR);
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	//利用反射调用系统方法
	public void systemService()
	{
		try
		{
			//获得ServiceManager类
			Class ServiceManager = Class.forName("android.os.ServiceManager");
			//获得ServiceManager的getService方法
			Method getService = ServiceManager.getMethod("getService", java.lang.String.class);
			//调用getService获取RemoteService
			Object oRemoteService = getService.invoke(null,Context.POWER_SERVICE);
			//获得IPowerManager.Stub类
			Class cStub = Class.forName("android.os.IPowerManager$Stub");
			//获得asInterface方法
			Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
			//调用asInterface方法获取IPowerManager对象
			Object oIPowerManager = asInterface.invoke(null, oRemoteService);
			//获得shutdown()方法
			Method shutdown = oIPowerManager.getClass().getMethod("shutdown",boolean.class,boolean.class);
			//调用shutdown()方法
			shutdown.invoke(oIPowerManager,false,true);
		}
		catch (Exception e)
		{
			Common.showShortToast(getApplicationContext(),"错误");
		}
	}
	
	private String getStrings(int resid)
	{
		return getResources().getString(resid);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		MyThemes.setThemes(this, titleLayout);
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

}

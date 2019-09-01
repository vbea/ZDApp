package com.binxin.zdapp.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.KeyEvent;
import android.content.Intent;
import android.content.Context;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.binxin.zdapp.R;
import com.binxin.zdapp.MainApplication;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.view.SlidButton;

public class Settings extends Activity
{
	private int item1;
	private LinearLayout titleLayout;
	private Button btn_setting,btn_setApp,btn_setDev,btn_engineer,btn_onsmtxl,btn_editsmtxl,btn_offsmtxl,btn_optlnt,btn_setimg,btn_setthm,btn_banner,btn_beep,btn_vibrate,btn_touch,btn_count,btn_filemode,btn_fhide,btn_thumb,btn_savedc,btn_language;
	private SlidButton sld_adv,sld_beep,sld_vibrate,sld_touchmode,sld_fhide,sld_thumb,sld_savedc;
	private SharedPreferences sube1,sube2;
	private Editor edt1,edt2;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		btn_setting=(Button)findViewById(R.id.opt_setting);
		btn_setApp = (Button)findViewById(R.id.opt_setApplication);
		btn_setDev = (Button)findViewById(R.id.opt_setDevelopment);
		btn_engineer = (Button) findViewById(R.id.opt_setEngineering);
		btn_onsmtxl = (Button)findViewById(R.id.opt_onpsdsm);
		btn_editsmtxl = (Button)findViewById(R.id.opt_editpsdsm);
		btn_offsmtxl = (Button)findViewById(R.id.opt_offpsdsm);
		btn_optlnt = (Button)findViewById(R.id.opt_torch);
		btn_setimg = (Button)findViewById(R.id.opt_setbackground);
		btn_setthm = (Button)findViewById(R.id.opt_settheme);
		btn_banner = (Button)findViewById(R.id.opt_banner);
		btn_beep = (Button) findViewById(R.id.opt_setBeep);
		btn_vibrate = (Button) findViewById(R.id.opt_setVibrate);
		btn_touch = (Button) findViewById(R.id.opt_setTouchMode);
		btn_count = (Button) findViewById(R.id.opt_countdown);
		btn_filemode = (Button)findViewById(R.id.opt_fileInter);
		btn_fhide = (Button) findViewById(R.id.opt_setFileHide);
		btn_thumb = (Button) findViewById(R.id.opt_setFileThumb);
		btn_savedc = (Button) findViewById(R.id.opt_setFileSave);
		btn_language = (Button) findViewById(R.id.opt_setLanguage);
		
		sld_adv = (SlidButton)findViewById(R.id.sld_advSwitch);
		sld_beep = (SlidButton)findViewById(R.id.sld_beep);
		sld_vibrate = (SlidButton)findViewById(R.id.sld_vibrate);
		sld_touchmode = (SlidButton)findViewById(R.id.sld_touchmode);
		sld_fhide = (SlidButton)findViewById(R.id.sld_fileHide);
		sld_thumb = (SlidButton)findViewById(R.id.sld_fileThumb);
		sld_savedc = (SlidButton)findViewById(R.id.sld_fileSave);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		
		sube1 = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		edt1 = sube1.edit();
		sube2 = getSharedPreferences("files", Context.MODE_PRIVATE);
		edt2 = sube2.edit();
		onChangePage();
		getSlidState();
		
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		btn_setting.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent context = new Intent();
				context.setAction(android.provider.Settings.ACTION_SETTINGS);
				startActivity(context);
			}
		});
		btn_setApp.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent2 = new Intent();
				intent2.setAction(android.provider.Settings.ACTION_APPLICATION_SETTINGS);
				startActivity(intent2);
			}
		});
		btn_setDev.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					Intent intent2 = new Intent();
					intent2.setAction(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
					startActivity(intent2);
				}
				catch(Exception e)
				{
					Toast.makeText(getApplicationContext(),"抱歉，不支持4.0以下系统",Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btn_engineer.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					Intent intent2 = new Intent();
					intent2.setClassName("com.android.settings","com.android.settings.TestingSettings");
					startActivityForResult(intent2,99);
				}
				catch(Exception e)
				{
					Toast.makeText(getApplicationContext(),"抱歉，你的手机不支持",Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btn_optlnt.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				String[] items = {"闪光灯模式(需手机支持)", "屏幕模式(推荐)"};
				SharedPreferences sube = getSharedPreferences("torch", Context.MODE_PRIVATE);
				item1 = sube.getInt("flashCode", 1);
				Builder builder = new Builder(Settings.this);
				builder.setTitle("请选择默认打开方式");
				builder.setSingleChoiceItems(items, item1, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int item)
					{
						//Toast.makeText(getApplicationContext(),"已保存修改",Toast.LENGTH_SHORT).show();
						item1 = item;
						SharedPreferences sube = getSharedPreferences("torch", Context.MODE_PRIVATE);
						SharedPreferences.Editor edit = sube.edit();
						switch(item)
						{
							case 0:
								edit.putInt("flashCode", 0);
								break;
							case 1:
								edit.putInt("flashCode", 1);
								break;
						}
						edit.commit();
						dialog.cancel();
					}
				});
				builder.show();
			}
		});
		btn_count.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				String[] items = {"10(默认)", "20","25","50","100","200","250"};
				SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
				item1 = sube.getInt("countUnit", 10);
				switch (item1)
				{
					case 10:
						item1 = 0;
						break;
					case 20:
						item1 = 1;
						break;
					case 25:
						item1 = 2;
						break;
					case 50:
						item1 = 3;
						break;
					case 100:
						item1 = 4;
						break;
					case 200:
						item1 = 5;
						break;
					case 250:
						item1 = 6;
						break;
					default:
						item1 = 0;
				}
				Builder builder = new Builder(Settings.this);
				builder.setTitle("注:数字越小，精度越高(消耗资源越大)");
				builder.setSingleChoiceItems(items, item1, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int item)
					{
						item1 = item;
						SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
						SharedPreferences.Editor edit = sube.edit();
						switch(item)
						{
							case 0:
								edit.putInt("countUnit", 10);
								break;
							case 1:
								edit.putInt("countUnit", 20);
								break;
							case 2:
								edit.putInt("countUnit", 25);
								break;
							case 3:
								edit.putInt("countUnit", 50);
								break;
							case 4:
								edit.putInt("countUnit", 100);
								break;
							case 5:
								edit.putInt("countUnit", 200);
								break;
							case 6:
								edit.putInt("countUnit", 250);
								break;
						}
						edit.commit();
						dialog.cancel();
					}
				});
			builder.show();
			}
		});
		btn_onsmtxl.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent3 = new Intent(Settings.this,Password_new.class);
				startActivity(intent3);
			}
		});
		btn_editsmtxl.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent3 = new Intent(Settings.this,Password_edit.class);
				startActivity(intent3);
			}
		});
		btn_offsmtxl.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent3 = new Intent(Settings.this,Password_off.class);
				startActivity(intent3);
			}
		});
		btn_setimg.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(Settings.this, Set_background.class);
				startActivity(intent);
			}
		});
		
		btn_setthm.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				startActivity(new Intent(Settings.this, Set_theme.class));
			}
		});
		btn_banner.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (setBooleanValue(edt1,"banner",!sld_adv.getChecked()))
					sld_adv.setChecked(!sld_adv.getChecked());
			}
		});
		btn_beep.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (setBooleanValue(edt1,"beep",!sld_beep.getChecked()))
					sld_beep.setChecked(!sld_beep.getChecked());
			}
		});
		btn_vibrate.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (setBooleanValue(edt1,"vibrate",!sld_vibrate.getChecked()))
					sld_vibrate.setChecked(!sld_vibrate.getChecked());
			}
		});
		btn_touch.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (setBooleanValue(edt1,"touch",!sld_touchmode.getChecked()))
					sld_touchmode.setChecked(!sld_touchmode.getChecked());
			}
		});
		btn_filemode.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				String[] items = {getString(R.string.file_mode1), getString(R.string.file_mode2)};
				SharedPreferences sube = getSharedPreferences("files", Context.MODE_PRIVATE);
				if (sube.getBoolean("detail",false))
					item1 = 1;
				else
					item1 = 0;
				Builder builder = new Builder(Settings.this);
				builder.setTitle(R.string.file_inter);
				builder.setSingleChoiceItems(items, item1, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int item)
					{
						item1 = item;
						SharedPreferences sube = getSharedPreferences("files", Context.MODE_PRIVATE);
						SharedPreferences.Editor edit = sube.edit();
						switch(item)
						{
							case 0:
								edit.putBoolean("detail",false);
								break;
							case 1:
								edit.putBoolean("detail",true);
								break;
						}
						edit.commit();
						dialog.cancel();
					}
				});
				builder.show();
			}
		});
		btn_fhide.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (setBooleanValue(edt2,"hide",!sld_fhide.getChecked()))
					sld_fhide.setChecked(!sld_fhide.getChecked());
			}
		});
		btn_thumb.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (setBooleanValue(edt2,"thumb",!sld_thumb.getChecked()))
					sld_thumb.setChecked(!sld_thumb.getChecked());
			}
		});
		btn_savedc.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (setBooleanValue(edt2,"savedc",!sld_savedc.getChecked()))
					sld_savedc.setChecked(!sld_savedc.getChecked());
			}
		});
		btn_language.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				String[] items = {getString(R.string.str_default), "English","简体中文","繁體中文"};
				SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
				switch (sube.getString("language", ""))
				{
					case "":
						item1 = 0;
						break;
					case "en":
						item1 = 1;
						break;
					case "zh":
						item1 = 2;
						break;
					case "tw":
						item1 = 3;
						break;
					default:
						item1 = 0;
				}
				Builder builder = new Builder(Settings.this);
				builder.setTitle(R.string.lanSetting);
				builder.setSingleChoiceItems(items, item1, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int item)
					{
						if (item1 != item)
						{
							item1 = item;
							SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
							SharedPreferences.Editor edit = sube.edit();
							switch(item)
							{
								case 0:
									edit.putString("language", "");
									break;
								case 1:
									edit.putString("language", "en");
									break;
								case 2:
									edit.putString("language", "zh");
									break;
								case 3:
									edit.putString("language", "tw");
									break;
							}
							edit.commit();
							new MainApplication().getConfig(getApplicationContext());
							Intent intent = new Intent(Intent.ACTION_MAIN);
							intent.addCategory(Intent.CATEGORY_LAUNCHER);
							intent.setComponent(new ComponentName(getPackageName(), getPackageName() + ".activity.Main"));
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							//PendingIntent pend = PendingIntent.getActivity(Settings.this, 0, intent, 0);
							startActivity(intent);
							overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
							//释放占用的内存资源
							finish();
							android.os.Process.killProcess(android.os.Process.myPid());
							
						}
						dialog.cancel();
					}
				});
				builder.show();
			}
		});
	}
	private void onChangePage()
	{
		LinearLayout re_pon = (LinearLayout) findViewById(R.id.settingsContactPwdON);
		LinearLayout re_off = (LinearLayout) findViewById(R.id.settingsContactPwdOff);
		SharedPreferences sube = getSharedPreferences("Zdapp_MyContact", Context.MODE_PRIVATE);
		boolean mPsd2 = sube.getBoolean("passWord", false);
		if(mPsd2)
		{
			re_off.setVisibility(View.GONE);
			re_pon.setVisibility(View.VISIBLE);
		}
		else
		{
			re_pon.setVisibility(View.GONE);
			re_off.setVisibility(View.VISIBLE);
		}
	}
	private void getSlidState()
	{
		sld_adv.setChecked(getBooleanValue(sube1,"banner", true));
		sld_beep.setChecked(getBooleanValue(sube1,"beep",true));
		sld_vibrate.setChecked(getBooleanValue(sube1,"vibrate",false));
		sld_touchmode.setChecked(getBooleanValue(sube1,"touch",true));
		sld_fhide.setChecked(getBooleanValue(sube2,"hide",false));
		sld_thumb.setChecked(getBooleanValue(sube2,"thumb",false));
		sld_savedc.setChecked(getBooleanValue(sube2,"savedc",true));
		sld_adv.setEnabled(false);
		sld_beep.setEnabled(false);
		sld_vibrate.setEnabled(false);
		sld_touchmode.setEnabled(false);
		sld_fhide.setEnabled(false);
		sld_thumb.setEnabled(false);
		sld_savedc.setEnabled(false);
	}
	
	private boolean getBooleanValue(SharedPreferences sh,String key,boolean dfault)
	{
		return sh.getBoolean(key, dfault);
	}
	private boolean setBooleanValue(Editor edt,String kty, boolean value)
	{
		edt.putBoolean(kty,value);
		return edt.commit();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		onChangePage();
		MyThemes.setThemes(this,titleLayout);
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
}

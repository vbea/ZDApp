package com.binxin.zdapp.activity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.EditText;
import android.view.View;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.DecHelper;
import com.binxin.zdapp.classes.Common;
import com.binxin.zdapp.classes.ExceptionHandler;
import com.binxin.zdapp.classes.Java21Web;

public class PasswordKey extends Activity
{
	private SharedPreferences sube;
	private LinearLayout titleLayout;
	private TextView txt_value;
	private DecHelper dec;
	private RadioButton rdbStan,rdbPro;
	private EditText maxQty, txt_key;
	private String versionCode = "s";
	final String Key_Remark = "ZDApp_Key_Java21_5.2";
	final String Key_Password = "3BE54FC5CCBBA30ACAEBF6F25C2CF5A1";
	private String editionStr = "";
	private String K_Pass = "java";
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.passwordkey);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		Button btn_create = (Button) findViewById(R.id.btn_keyCreate);
		Button btn_copy = (Button) findViewById(R.id.btn_keyCopy);
		txt_key = (EditText) findViewById(R.id.txt_ckeyPass);
		txt_value = (TextView) findViewById(R.id.txt_keyPassValue);
		rdbStan = (RadioButton) findViewById(R.id.rdb_stand);
		rdbPro = (RadioButton) findViewById(R.id.rdb_pro);
		maxQty = (EditText) findViewById(R.id.passMaxActive);
		
		back.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		btn_create.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				/*if (maxQty.getText().toString().trim().length() <= 0)
				{
					Common.showShortToast(getApplicationContext(), "请输入最大激活次数");
					return;
				}*/
				try
				{
					if (dec == null)
						dec = new DecHelper(K_Pass);
					SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
					Date now = new Date();
					if (maxQty.getText().toString().length() > 0)
					{
						int max = Integer.parseInt(maxQty.getText().toString());
						now.setDate(now.getDate()+max);
					}
					//now.setMonth(now.getMonth()+1);
					String key = sdf.format(now);//原始key字符
					//if (maxQty.getText().toString().trim().length() > 0)
						//key = maxQty.getText().toString();
					
					String kee = dec.encrypt(key);//初次加密
					if (rdbPro.isChecked())
					{
						K_Pass = kee.substring(0, 5).toUpperCase();//获取初次加密的前5位当做二次加密的密码
						dec = new DecHelper(K_Pass);//二次加密
						txt_value.setText(key + "\n" + kee +"\n"+K_Pass);
						String newkey = createKey(dec.encrypt(key).toUpperCase(), K_Pass);
						txt_key.setText(newkey);
					}
					else
					{
						K_Pass = kee.substring(16-4).toUpperCase();
						dec = new DecHelper(K_Pass);//二次加密
						txt_value.setText(key + "\n" + kee +"\n"+K_Pass);
						String newkey = createKey(getEditionKey(K_Pass, dec.encrypt(key).toUpperCase()));
						txt_key.setText(newkey);
					}
				}
				catch (Exception e)
				{
					Common.showShortToast(getApplicationContext(),"生成失败");
					ExceptionHandler.log(e.toString());
				}
			}
		});
		
		btn_copy.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (txt_key.getText().toString().equals(""))
				{
					Common.showShortToast(getApplicationContext(), "请先生成注册码");
					return;
				}
				ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cbm.setPrimaryClip(ClipData.newPlainText("注册码",txt_key.getText().toString()));
				/*if (isNet())
				{
					progressDialog = ProgressDialog.show(PasswordKey.this, "提交至服务器", "正在提交……", false, false);
					registKey();
				}
				else*/
					Common.showShortToast(getApplicationContext(), "复制成功");
			}
		});
	}
	
	private String getEditionKey(String pass, String key)
	{
		//maxQty.setText(key+"("+pass+")");
		editionStr = rdbPro.isChecked()?"专业版":"标准版";
		if (rdbPro.isChecked())
		{
			versionCode = "p";
			return pass + key;
		}
		else
		{
			versionCode = "s";
			return key + pass;
		}
	}
	
	public String createKey(String k)
	{
		String newk = "";
		if (k.length() == 20)
		{
			for (int i=0; i < 20; i+=5)
			{
				newk += k.substring(i,i+5) + "-";
			}
			newk = newk.substring(0, newk.length() - 1);
		}
		else
			Common.showShortToast(this,"生成失败，请重试！");
		return newk;
	}
	
	public String createKey(String k, String psd)
	{
		//maxQty.setText(k+"("+psd+")");
		if (k.length() == 16 && psd.length() == 5)
		{
			String psd1 = psd.substring(0, 2);//6
			psd = psd.replace(psd1, "");//4
			String key1 = k.substring(0, 5);//1
			String key2 = k.substring(5, 8);//2
			String key3 = k.substring(8, 13);//3
			String key4 = k.substring(13, 16);//5
			
			return (key1 + "-" + key2 + "-" + key3 + "-" + psd + "-" + key4+psd1);
		}
		else
			Common.showShortToast(this,"生成失败，请重试！");
		return "";
	}
	
	public boolean isNet()
	{
		ConnectivityManager zdapp = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (zdapp == null)
		{
			return false;
		}
		else
		{
			NetworkInfo[] info = zdapp.getAllNetworkInfo();
			if (info != null)
			{
				for (int i=0; i<info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
				//return zdapp.getActiveNetworkInfo().isAvailable();
			}
		}
		return false;
	}
	
	public void setMyTheme()
	{
		sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		K_Pass = sube.getString("keypass", "java");
		txt_value.setText(K_Pass);
		//txt_key.setText("");
		MyThemes.setThemes(this, titleLayout);
	}
	
	@Override
	protected void onResume()
	{
		setMyTheme();
		super.onResume();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	@Override
	protected void onDestroy()
	{
		SharedPreferences.Editor edt = sube.edit();
		edt.putString("keypass", K_Pass);
		edt.commit();
		super.onDestroy();
	}
	
	boolean web_result;
	public void registKey()
	{
		if (rdbStan.isChecked())
			mHandler.sendEmptyMessageDelayed(2, 1000);
		else
			mHandler.sendEmptyMessageDelayed(6, 1000);
		/*
		try
		{
			Thread mThread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						//Java21Web java = new Java21Web();
						//if (java.CreateKey(txt_key.getText().toString(), versionCode, maxQty.getText().toString(), Key_Remark, Key_Password))
							mHandler.sendEmptyMessage(2);
						//else
							//mHandler.sendEmptyMessage(3);
					}
					catch(Exception e)
					{
						Message msg = new Message();
						msg.what = 1;
						msg.obj = e;
						mHandler.sendMessage(msg);
					}
				}
			});
			mThread.start();
			//mThread.join();
			//if (!resultSTR.equals(""))
		}
		catch(Exception e)
		{
			mHandler.sendEmptyMessage(4);
		}*/
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					txt_value.setText(msg.obj.toString());
					Common.showShortToast(getApplicationContext(),"提交异常："+msg.obj.toString());
					break;
				case 2:
					Common.showShortToast(getApplicationContext(),"提交成功");
					ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					cbm.setPrimaryClip(ClipData.newPlainText("注册码",txt_key.getText().toString()+"\n该注册码仅适用于“21天学通Java” 5.2版以上注册，内置证书有效期为一个月，在有效期内使用可进行无限次注册。当证书过期后该注册码将被自动销毁。(本注册码为"+editionStr+")【邠心工作室】"));
					Common.showShortToast(getApplicationContext(),"复制成功");
					break;
				case 6:
					Common.showShortToast(getApplicationContext(),"提交成功");
					ClipboardManager cbm1 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					cbm1.setPrimaryClip(ClipData.newPlainText("密钥",txt_key.getText().toString()+"\n该密钥仅适用于“21天学通Java” 6.0版以上使用，加密证书有效期为一个月，在有效期内可进行无限次验证。当证书过期后不能用于验证，请尽快使用。【邠心工作室】"));
					Common.showShortToast(getApplicationContext(),"复制成功");
					break;
				case 3:
					Common.showShortToast(getApplicationContext(), "提交失败");
					break;
				case 4:
					Common.showShortToast(getApplicationContext(), "提交异常");
					break;
				case 5:
					Common.showShortToast(getApplicationContext(), "response");
					break;
			}
			if (progressDialog != null)
				progressDialog.dismiss();
			super.handleMessage(msg);
		}
	};
}

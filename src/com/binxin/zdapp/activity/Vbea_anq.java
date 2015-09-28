package com.binxin.zdapp.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.telephony.SmsManager;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class Vbea_anq extends Activity
{
	private TextView test;
	private String massage, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10;
	private String a11 = "请选择";
	private int mSubmit;
	private EditText name, age, address, appname, goodapp, falseapp, apphope, gaijin, yijian, phone;
	private Button submit;
	private ImageButton back;
	private ImageView star01, star02, star03, star04, star05;
	private Spinner male;
	private ArrayAdapter adapter2;
	private LinearLayout titleLayout;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zdapp_aq);
		test = (TextView) findViewById(R.id.aq_test);
		male = (Spinner) findViewById(R.id.aq_btn_male);
		submit = (Button) findViewById(R.id.aq_submit);
		back = (ImageButton) findViewById(R.id.btn_close);
		name = (EditText) findViewById(R.id.aq_name);
		age = (EditText) findViewById(R.id.aq_age);
		address = (EditText) findViewById(R.id.aq_address);
		appname = (EditText) findViewById(R.id.aq_appname);
		goodapp = (EditText) findViewById(R.id.aq_appgood);
		falseapp = (EditText) findViewById(R.id.aq_appfalse);
		apphope = (EditText) findViewById(R.id.aq_apphope);
		gaijin = (EditText) findViewById(R.id.aq_gaijin);
		yijian = (EditText) findViewById(R.id.aq_yijian);
		phone = (EditText) findViewById(R.id.aq_phone);
		star01 = (ImageView) findViewById(R.id.star_image01);
		star02 = (ImageView) findViewById(R.id.star_image02);
		star03 = (ImageView) findViewById(R.id.star_image03);
		star04 = (ImageView) findViewById(R.id.star_image04);
		star05 = (ImageView) findViewById(R.id.star_image05);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		SharedPreferences sube = getSharedPreferences("vbea_zdapp", Context.MODE_PRIVATE);
		mSubmit = sube.getInt("subCode", 0);
		if (mSubmit != 0)
		{
			starView(mSubmit);
			test.setText("已累计提交"+mSubmit+"次，贡献：");
		}
		else
		{
			test.setText("已累计提交"+mSubmit+"次");
		}
		adapter2 = ArrayAdapter.createFromResource(this, R.array.spnner, android.R.layout.simple_spinner_item);//设置下拉列表的风格
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//将adapter2 添加到spinner中
		male.setAdapter(adapter2);
		
		//添加事件Spinner事件监听
		male.setOnItemSelectedListener(new SpinnerXMLSelectedListener());//设置默认值
		male.setVisibility(View.VISIBLE);
		/*male.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				LayoutInflater layout = LayoutInflater.from(Vbea_anq.this);
				View mView = layout.inflate(R.layout.aq_males, null);
				
				Button o_male = (Button) mView.findViewById(R.id.btn_male);
				Button female = (Button) mView.findViewById(R.id.btn_female);
				Builder builder = new Builder(Vbea_anq.this);
				builder.setView(mView);
				//builder.setOnItemSelectedListener(DialogInterface dialog, int which)
				new AlertDialog.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						
					}
				};
				o_male.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						male.setText("男");
						onKeyDown(0,null);
						//finish();
					}
				});
				female.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						Intent intent = new Intent(Vbea_anq.this,App_sptest.class);
						startActivity(intent);
					}
				});
				builder.create();
				builder.show();
			}
		});*/
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		submit.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				mRun();
				if (a1.length() <= 0 || a2.length() <= 0 || a3.length() <= 0 || a4.length() <= 0 || a5.length() <= 0 || a6.length() <= 0 || a7.length() <= 0 || a8.length() <= 0 || a9.length() <= 0 || a10.length() <= 0 || a11.length() > 2)
				{
					Toast.makeText(Vbea_anq.this, "请耐心填完，谢谢！", Toast.LENGTH_SHORT).show();
				}
				else
				{
					SmsManager sms = SmsManager.getDefault();
					ArrayList<String> contents = sms.divideMessage(massage);
					for(String msg : contents)
					{
						sms.sendTextMessage("10086", null, msg, null, null);
					}
					//提交版本信息
					/*mWelcome = 6200;
					SharedPreferences welcome = getSharedPreferences("vbea_zdapp", Context.MODE_PRIVATE);
					//mWelcome = welcome.getBoolean("vbea_zdapp", false);
					SharedPreferences.Editor edit = welcome.edit();
					edit.putInt("verCode", mWelcome);
					edit.commit();*/
					//提交次数
					mSubmit = mSubmit + 1;
					SharedPreferences sube = getSharedPreferences("vbea_zdapp", Context.MODE_PRIVATE);
					SharedPreferences.Editor edit2 = sube.edit();
					edit2.putInt("subCode", mSubmit);
					edit2.commit();
					starView(mSubmit);
					test.setText("已累计提交"+mSubmit+"次，贡献：");
					Toast.makeText(Vbea_anq.this, "提交成功，按返回键即可退出", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	public void mRun()
	{
		a1 = name.getText().toString();
		a2 = age.getText().toString();
		a3 = address.getText().toString();
		a4 = appname.getText().toString();
		a5 = goodapp.getText().toString();
		a6 = falseapp.getText().toString();
		a7 = apphope.getText().toString();
		a8 = gaijin.getText().toString();
		a9 = yijian.getText().toString();
		a10 = phone.getText().toString();
		massage = "ZDApp问卷：\n姓名:"+a1+"\n年龄:"+a2+"\n性别:"+a11+"\n认识方式:"+a3+"\n使用功能:"+a4+"\n好功能:"+a5+"\n不足功能:"+a6+"\n希望功能:"+a7+"\n改进:"+a8+"\n意见:"+a9+"\n联系方式："+a10+"\n";
	}
	public void starView(int code)
	{
		if (code >= 1)
		{
			star01.setImageResource(R.drawable.star_small);
		}
		if (code >= 5)
		{
			star02.setImageResource(R.drawable.star_small);
		}
		if (code >= 10)
		{
			star03.setImageResource(R.drawable.star_small);
		}
		if (code >= 15)
		{
			star04.setImageResource(R.drawable.star_small);
		}
		if (code >= 20)
		{
			star05.setImageResource(R.drawable.star_small);
		}
	}
	public class SpinnerXMLSelectedListener implements OnItemSelectedListener
	{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			a11 = adapter2.getItem(arg2).toString();
		}
		public void onNothingSelected(AdapterView<?> arg0)
		{

		}
	}
	public void setMyTheme()
	{
		SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		int code = sube.getInt("themeCode", 0);
		MyThemes.setThemes(this, titleLayout, code);
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
		//释放占用的内存资源
		super.onDestroy();
		//System.exit(0);
	}
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		//这里构建KeyEvent对象，其功能为返回键的功能
		//因此我们按任意键都会执行返回键功能
		//KeyEvent key = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if (mSubmit != 0)
			{
				/*Intent intent = new Intent(Vbea_anq.this,About.class);
				startActivity(intent);*/
				finish();
			}
		}
		/*if(keyCode == KeyEvent.KEYCODE_HOME && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
			
			return true;
		}*/
		//这里传入的参数就是我们自己构建的KeyEvent对象key
		return super.onKeyDown(keyCode,event);
	}
}

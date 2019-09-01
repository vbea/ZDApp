package com.binxin.zdapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.content.*;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MD5Util;

public class Password_edit extends Activity
{
	private String edt1 ="";
	private String edt2 ="";
	private EditText edit1;
	private EditText edit2;
	private EditText edit3;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		setContentView(R.layout.password_edit);
		setFinishOnTouchOutside(true);

		Button btn1 = (Button) findViewById(R.id.psd_btnOK);
		Button btn2 = (Button) findViewById(R.id.psd_btnCancel);
		edit1 = (EditText) findViewById(R.id.psd_edit04);
		edit2 = (EditText) findViewById(R.id.psd_edit05);
		edit3 = (EditText) findViewById(R.id.psd_edit06);

		btn1.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					if (edit1.length() == 0 && edit2.length() == 0 && edit3.length() == 0)
					{
						Toast.makeText(getApplicationContext(),"未输入任何内容",Toast.LENGTH_SHORT).show();
						return;
					}
					if (edit1.length() == 0 && edit2.length() != 0)
					{
						Toast.makeText(getApplicationContext(),"请输入原密码",Toast.LENGTH_SHORT).show();
						return;
					}
					if (edit1.length() != 0 && edit2.length() == 0)
					{
						Toast.makeText(getApplicationContext(),"请输入新密码",Toast.LENGTH_SHORT).show();
						return;
					}
					if (edit1.length() != 0 && edit2.length() != 0)
					{
						/*if (edit2.length() <= 0)
						{
							Toast.makeText(getApplicationContext(),"请输入新密码",Toast.LENGTH_SHORT).show();
						}*/
						if (edit3.length() == 0)
						{
							Toast.makeText(getApplicationContext(),"请再输入一次新密码",Toast.LENGTH_SHORT).show();
						}
						else
						{
							edt1 = MD5Util.md5(edit2.getText().toString());
							edt2 = MD5Util.md5(edit3.getText().toString());
							if (!edt1.equals(edt2))
							{
								Toast.makeText(getApplicationContext(),"两次输入不一致，请重新输入",Toast.LENGTH_SHORT).show();
								edit3.setText("");
							}
							else
							{
								SharedPreferences sube5 = getSharedPreferences("Zdapp_MyContact", Context.MODE_PRIVATE);
								String passwode = sube5.getString("password", "");
								String edt3 = MD5Util.md5(edit1.getText().toString());
								if (!passwode.equals(edt3))
								{
									Toast.makeText(getApplicationContext(),"原密码输入错误！",Toast.LENGTH_SHORT).show();
									finish();
								}
								else
								{
									Toast.makeText(getApplicationContext(),"修改成功！",Toast.LENGTH_SHORT).show();
									SharedPreferences sube3 = getSharedPreferences("Zdapp_MyContact", Context.MODE_PRIVATE);
									SharedPreferences.Editor edit3 = sube3.edit();
									edit3.putString("password", edt2);
									edit3.commit();
									finish();
								}
							}
						}
					}
				}
				catch (Exception e)
				{
					Toast.makeText(getApplicationContext(),"未输入任何内容",Toast.LENGTH_SHORT).show();
				}
			}
		});
		btn2.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});
	}
}

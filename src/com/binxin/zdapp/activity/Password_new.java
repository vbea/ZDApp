package com.binxin.zdapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.content.*;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MD5Util;

public class Password_new extends Activity
{
	private String edt1 = "";
	private String edt2 = "";
	private EditText edit1;
	private EditText edit2;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		setContentView(R.layout.password);
		setFinishOnTouchOutside(true);
		
		Button btn1 = (Button) findViewById(R.id.psd_btnOK);
		Button btn2 = (Button) findViewById(R.id.psd_btnCancel);
		edit1 = (EditText) findViewById(R.id.psd_edit01);
		edit2 = (EditText) findViewById(R.id.psd_edit02);
		/*edt1 = Integer.parseInt(edit1.getText().toString());
		edt2 = Integer.parseInt(edit2.getText().toString());*/
		
		btn1.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					if (edit1.length() == 0 && edit2.length() == 0)
					{
						Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
						return;
					}
					
					edt1 = MD5Util.md5(edit1.getText().toString());
					edt2 = MD5Util.md5(edit2.getText().toString());
					if (!edt1.equals(edt2))
					{
						Toast.makeText(getApplicationContext(),"两次输入不一致，请重新输入",Toast.LENGTH_SHORT).show();
						edit2.setText("");
					}
					else
					{
						Toast.makeText(getApplicationContext(),"创建成功！",Toast.LENGTH_SHORT).show();
						SharedPreferences sube = getSharedPreferences("Zdapp_MyContact", Context.MODE_PRIVATE);
						SharedPreferences.Editor edit = sube.edit();
						edit.putBoolean("passWord", true);
						edit.putString("password",edt2);
						edit.commit();
						finish();
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

package com.binxin.zdapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.content.*;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MD5Util;

public class Password_on extends Activity
{
	private String edt1;
	private EditText edit1;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		setContentView(R.layout.password_off);
		setFinishOnTouchOutside(true);

		Button btn1 = (Button) findViewById(R.id.psd_btnOK);
		Button btn2 = (Button) findViewById(R.id.psd_btnCancel);
		edit1 = (EditText) findViewById(R.id.psd_edit03);

		btn1.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					edt1 = MD5Util.md5(edit1.getText().toString());
					SharedPreferences sube1 = getSharedPreferences("Zdapp_MyContact", Context.MODE_PRIVATE);
					String edt2 = sube1.getString("password","");
					if (edt1.equals(edt2))
					{
						startActivity(new Intent(Password_on.this, App_contact.class));
						finish();
					}
					else
					{
						Toast.makeText(getApplicationContext(),"密码错误！",Toast.LENGTH_SHORT).show();
						finish();
						return;
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

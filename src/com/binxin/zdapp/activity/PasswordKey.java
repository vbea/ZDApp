package com.binxin.zdapp.activity;

import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.View;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.ClipboardManager;
import android.content.ClipData;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.DecHelper;
import com.binxin.zdapp.classes.Common;

public class PasswordKey extends Activity
{
	private LinearLayout titleLayout;
	private TextView txt_key;
	private DecHelper dec;
	int radm = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.passwordkey);
		
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		Button btn_create = (Button) findViewById(R.id.btn_keyCreate);
		Button btn_copy = (Button) findViewById(R.id.btn_keyCopy);
		txt_key = (TextView) findViewById(R.id.txt_ckeyPass);
		
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
				try
				{
					if (dec == null)
						dec = new DecHelper("java");
					SimpleDateFormat sdf = new SimpleDateFormat("MMddH");
					Random rdm = new Random();
					int radms = rdm.nextInt(9);
					radm = (radm != radms) ? radms : radms + 1;
					String key = sdf.format(new Date()) + radm;
					String kee = dec.encrypt(key);
					txt_key.setText(createKey(kee.toUpperCase()));
				}
				catch (Exception e)
				{
					
				}
			}
		});
		
		btn_copy.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cbm.setPrimaryClip(ClipData.newPlainText("注册码",txt_key.getText().toString()));
				Common.showShortToast(getApplicationContext(),"复制成功");
			}
		});
	}
	
	public String createKey(String k)
	{
		String newk = "";
		if (k.length() == 16)
		{
			for (int i=0; i < 16; i+=4)
			{
				newk += k.substring(i,i+4) + "-";
			}
		}
		else
			Common.showShortToast(this,"生成失败，请重试！");
		return newk.substring(0, newk.length() - 1);
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
		super.onDestroy();
	}
}

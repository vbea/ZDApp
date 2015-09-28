package com.binxin.zdapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.KeyEvent;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;

import com.binxin.zdapp.R;
import com.binxin.zdapp.style.SystemBarTintManager;
import com.binxin.zdapp.classes.MyThemesInfo;

public class Set_theme extends Activity
{
	private LinearLayout mt_layout;
	private ImageButton tm01,tm02,tm03,tm04,tm05,tm06,tm07,tm08,tm09,tm10,tm11,tm12,tm13,tm14,tm15,tm16,tm17,tm18,tm19,tm20;
	private Button btn_save;
	private TextView mt_Name;
	private ImageView act_img;
	private int mt_Code = -1;
	private int mt_oCode;
	private ImageButton[] mtList;
	private MyThemesInfo mTheme;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			//状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		setContentView(R.layout.sethemes);
		mtList = new ImageButton[20];
		mt_layout = (LinearLayout) findViewById(R.id.themeLayout);
		mt_Name = (TextView) findViewById(R.id.sethemeName);
		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		act_img = (ImageView) findViewById(R.id.mt_Image);
		btn_save = (Button) findViewById(R.id.mt_btnOk);
		tm01 = (ImageButton) findViewById(R.id.mt_them01);
		tm02 = (ImageButton) findViewById(R.id.mt_them02);
		tm03 = (ImageButton) findViewById(R.id.mt_them03);
		tm04 = (ImageButton) findViewById(R.id.mt_them04);
		tm05 = (ImageButton) findViewById(R.id.mt_them05);
		tm06 = (ImageButton) findViewById(R.id.mt_them06);
		tm07 = (ImageButton) findViewById(R.id.mt_them07);
		tm08 = (ImageButton) findViewById(R.id.mt_them08);
		tm09 = (ImageButton) findViewById(R.id.mt_them09);
		tm10 = (ImageButton) findViewById(R.id.mt_them10);
		tm11 = (ImageButton) findViewById(R.id.mt_them11);
		tm12 = (ImageButton) findViewById(R.id.mt_them12);
		tm13 = (ImageButton) findViewById(R.id.mt_them13);
		tm14 = (ImageButton) findViewById(R.id.mt_them14);
		tm15 = (ImageButton) findViewById(R.id.mt_them15);
		tm16 = (ImageButton) findViewById(R.id.mt_them16);
		tm17 = (ImageButton) findViewById(R.id.mt_them17);
		tm18 = (ImageButton) findViewById(R.id.mt_them18);
		tm19 = (ImageButton) findViewById(R.id.mt_them19);
		tm20 = (ImageButton) findViewById(R.id.mt_them20);
		
		mtList[0] = tm01;
		mtList[1] = tm02;
		mtList[2] = tm03;
		mtList[3] = tm04;
		mtList[4] = tm05;
		mtList[5] = tm06;
		mtList[6] = tm07;
		mtList[7] = tm08;
		mtList[8] = tm09;
		mtList[9] = tm10;
		mtList[10] = tm11;
		mtList[11] = tm12;
		mtList[12] = tm13;
		mtList[13] = tm14;
		mtList[14] = tm15;
		mtList[15] = tm16;
		mtList[16] = tm17;
		mtList[17] = tm18;
		mtList[18] = tm19;
		mtList[19] = tm20;
		
		for (int i = 0; i < 20; i++)
		{
			mtList[i].setOnClickListener(new ThemeClick(i));
		}
		//test.setOnCheckedChangeListener(new OnCheckedChangeListener
		onStarted();//初始化
		//返回
		close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				onChangeExited();
			}
		});
		
		//保存
		btn_save.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				setThemes(mt_Code);
				//Toast.makeText(getApplicationContext(),"已保存设置",Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
	//按钮点击事件监听
	public class ThemeClick implements OnClickListener
	{
		private int _id;
		public ThemeClick(int code)
		{
			_id = code;
		}
		@Override
		public void onClick(View v)
		{
			setThemeId(_id);
		}
	}
	//临时保存主题ID
	public void setThemeId(int tid)
	{
		if (mt_Code != tid)
		{
			mt_Code = tid;
			mTheme.setImage(act_img,mt_Code);
			mTheme.setThemes(mt_layout,mt_Code);
			if (mt_Code < 11)
				mt_Name.setText("颜色" + (mt_Code + 1));
			else if (mt_Code > 11)
				mt_Name.setText("颜色" + mt_Code);
			else
				mt_Name.setText("半透明");
		}
	}
	//保存主题设置
	public void setThemes(int code)
	{
		SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		Editor edt1 = sube.edit();
		edt1.putInt("themeCode",code);
		edt1.commit();
	}
	
	//初始化
	public void onStarted()
	{
		mTheme = new MyThemesInfo(this);
		SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		mt_oCode = sube.getInt("themeCode", 0);
		setThemeId(mt_oCode);
	}
	
	private void onChangeExited()
	{
		if (mt_Code != mt_oCode)
		{
			setTheme(android.R.style.Theme_DeviceDefault_Light);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示");
			builder.setMessage("设置已改变，是否保存？");
			builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int swich)
				{
					setThemes(mt_Code);
					onDestroy();
				}
			});
			builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int swich)
				{
					onDestroy();
				}
			});
			builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int swich)
				{
					dialog.cancel();
				}
			});
			builder.show();
		}
		else
		{
			onDestroy();
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
			onChangeExited();
		return super.onKeyDown(keyCode, event);
	}
	
}

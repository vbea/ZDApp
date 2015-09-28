package com.binxin.zdapp.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.MyImageView;
import com.binxin.zdapp.classes.MyImageView.OnMyViewClick;

public class Set_background extends Activity
{
	private ImageView is0,is1,is2,is3,is4,is5,is6,is7,is8,is9,is10,is11;
	private MyImageView myImg00,myImg01,myImg02,myImg03,myImg04,myImg05,myImg06,myImg07,myImg08,myImg09,myImg10,myImg11;
	private LinearLayout titleLayout;
	private RelativeLayout homePage;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setbackground);
		
		is0 = (ImageView) findViewById(R.id.isbackimg00);
		is1 = (ImageView) findViewById(R.id.isbackimg01);
		is2 = (ImageView) findViewById(R.id.isbackimg02);
		is3 = (ImageView) findViewById(R.id.isbackimg03);
		is4 = (ImageView) findViewById(R.id.isbackimg04);
		is5 = (ImageView) findViewById(R.id.isbackimg05);
		is6 = (ImageView) findViewById(R.id.isbackimg06);
		is7 = (ImageView) findViewById(R.id.isbackimg07);
		is8 = (ImageView) findViewById(R.id.isbackimg08);
		is9 = (ImageView) findViewById(R.id.isbackimg09);
		is10 = (ImageView) findViewById(R.id.isbackimg10);
		is11 = (ImageView) findViewById(R.id.isbackimg11);
		myImg00 = (MyImageView)findViewById(R.id.setMyImageView00);
		myImg01 = (MyImageView)findViewById(R.id.setMyImageView01);
		myImg02 = (MyImageView)findViewById(R.id.setMyImageView02);
		myImg03 = (MyImageView)findViewById(R.id.setMyImageView03);
		myImg04 = (MyImageView)findViewById(R.id.setMyImageView04);
		myImg05 = (MyImageView)findViewById(R.id.setMyImageView05);
		myImg06 = (MyImageView)findViewById(R.id.setMyImageView06);
		myImg07 = (MyImageView)findViewById(R.id.setMyImageView07);
		myImg08 = (MyImageView)findViewById(R.id.setMyImageView08);
		myImg09 = (MyImageView)findViewById(R.id.setMyImageView09);
		myImg10 = (MyImageView)findViewById(R.id.setMyImageView10);
		myImg11 = (MyImageView)findViewById(R.id.setMyImageView11);
		
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		homePage = (RelativeLayout) findViewById(R.id.setBgReLayout);
		
		myImg00.setOnClickIntent(new OnMyViewClick()
		{
			public void onClick()
			{
				setIsBackImg(0);
			}
		});
		myImg01.setOnClickIntent(new OnMyViewClick()
		{
			public void onClick()
			{
				setIsBackImg(1);
			}
		});
		myImg02.setOnClickIntent(new OnMyViewClick()
		{
			public void onClick()
			{
				setIsBackImg(2);
			}
		});
		myImg03.setOnClickIntent(new OnMyViewClick()
		{
			public void onClick()
			{
				setIsBackImg(3);
			}
		});
		myImg04.setOnClickIntent(new OnMyViewClick()
		{
			public void onClick()
			{
				setIsBackImg(4);
			}
		});
		myImg05.setOnClickIntent(new OnMyViewClick()
		{
			public void onClick()
			{
				setIsBackImg(5);
			}
		});
		myImg06.setOnClickIntent(new OnMyViewClick()
		{
			public void onClick()
			{
				setIsBackImg(6);
			}
		});
		myImg07.setOnClickIntent(new OnMyViewClick()
		{
			public void onClick()
			{
				setIsBackImg(7);
			}
		});
		myImg08.setOnClickIntent(new OnMyViewClick()
		{
			public void onClick()
			{
				setIsBackImg(8);
			}
		});
		myImg09.setOnClickIntent(new OnMyViewClick()
		{
			public void onClick()
			{
				setIsBackImg(9);
			}
		});
		myImg10.setOnClickIntent(new OnMyViewClick()
		{
			public void onClick()
			{
				setIsBackImg(10);
			}
		});
		myImg11.setOnClickIntent(new OnMyViewClick()
		{
			public void onClick()
			{
				setIsBackImg(11);
			}
		});
		/*myImg12.setOnClickIntent(new OnMyViewClick()
		{
			public void onClick()
			{
				setIsBackImg(12);
			}
		});*/
		//Button btn1 = (Button) findViewById(R.id.setbackgroundButton01);
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		getIsBackImg();
		setBackground();
	}
	///获取已设置的背景
	public void getIsBackImg()
	{
		reStart();//初始化
		SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		int img = sube.getInt("imageCode", 0);
		switch (img)
		{
			case 0:
				setAtiveImg(is0);
				break;
			case 1:
				setAtiveImg(is1);
				break;
			case 2:
				setAtiveImg(is2);
				break;
			case 3:
				setAtiveImg(is3);
				break;
			case 4:
				setAtiveImg(is4);
				break;
			case 5:
				setAtiveImg(is5);
				break;
			case 6:
				setAtiveImg(is6);
				break;
			case 7:
				setAtiveImg(is7);
				break;
			case 8:
				setAtiveImg(is8);
				break;
			case 9:
				setAtiveImg(is9);
				break;
			case 10:
				setAtiveImg(is10);
				break;
			case 11:
				setAtiveImg(is11);
				break;
		}
	}
	///设置背景
	public void setIsBackImg(int index)
	{
		SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		int img = sube.getInt("imageCode", 0);
		if (img != index)
		{
			Editor edt = sube.edit();
			edt.putInt("imageCode", index);
			edt.commit();
			//Toast.makeText(getApplicationContext(),"设置成功！",Toast.LENGTH_SHORT).show();
			getIsBackImg();
			setBackground();
		}
	}
	///初始化
	public void reStart()
	{
		ImageView [] mlist = {is0,is1,is2,is3,is4,is5,is6,is7,is8,is9,is10,is11};
		for (int i = 0; i < mlist.length; i++)
		{
			mlist[i].setVisibility(View.GONE);
		}
	}
	///获取设置的图片
	public void setAtiveImg(ImageView view)
	{
		view.setVisibility(View.VISIBLE);
	}
	public void setBackground()
	{
		SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
		int img = sube.getInt("imageCode", 0);
		switch (img)
		{
			case 0:
				homePage.setBackgroundResource(R.drawable.background);
				break;
			case 1:
				homePage.setBackgroundResource(R.drawable.bg_image01);
				break;
			case 2:
				homePage.setBackgroundResource(R.drawable.bg_image02);
				break;
			case 3:
				homePage.setBackgroundResource(R.drawable.bg_image03);
				break;
			case 4:
				homePage.setBackgroundResource(R.drawable.bg_image04);
				break;
			case 5:
				homePage.setBackgroundResource(R.drawable.bg_image05);
				break;
			case 6:
				homePage.setBackgroundResource(R.drawable.bg_image06);
				break;
			case 7:
				homePage.setBackgroundResource(R.drawable.bg_image07);
				break;
			case 8:
				homePage.setBackgroundResource(R.drawable.bg_image08);
				break;
			case 9:
				homePage.setBackgroundResource(R.drawable.bg_image09);
				break;
			case 10:
				homePage.setBackgroundResource(R.drawable.bg_image10);
				break;
			case 11:
				homePage.setBackgroundResource(R.drawable.bg_image11);
				break;
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
		// TODO: Implement this method
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		setMyTheme();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
}

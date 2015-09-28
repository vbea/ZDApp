package com.binxin.zdapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ImageButton;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class App_colorLight extends Activity
{
    /** Called when the activity is first created. */
	private LinearLayout mylayout,titleLayout;
	private Resources myColor;
	private int pin;
	private int li;
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//设置无标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.light);
        //改变layout背景颜色
        mylayout=(LinearLayout)findViewById(R.id.light_layout);
        setDeColor();
		setDeBright();
		
		ImageButton btn_close = (ImageButton)findViewById(R.id.btn_close);
		ImageButton btn_light = (ImageButton)findViewById(R.id.btn_colortorch);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		btn_close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		btn_light.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(App_colorLight.this,App_lightColor.class);
				startActivity(intent);
				finish();
				Toast.makeText(getApplicationContext(), "全屏显示", Toast.LENGTH_SHORT).show();
			}
		});
    }
	//屏幕点击事件显示菜单
	@Override
    public boolean onTouchEvent(MotionEvent event)
	{
    	openOptionsMenu();
    	return false;
	}
	//关联菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
		//setTheme(android.R.style.Theme_Holo_Light);
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,1,0,"设置颜色");
		menu.add(0,2,0,"调整亮度");
		menu.add(0,3,0,"全屏显示");
    	return true;
    }
	//捕捉菜单事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case 1:
				selectColor();
				break;
			case 2:
				selectBright();
				break;
			case 3:
				Intent intent = new Intent(App_colorLight.this,App_lightColor.class);
				startActivity(intent);
				finish();
				Toast.makeText(getApplicationContext(), "全屏显示", Toast.LENGTH_SHORT).show();
				return true;
		}
		return false;
	}
	//选择颜色
	public void selectColor()
    {
    	final String[] items = {"白色", "红色", "黑色","黄色","绿色","粉色","蓝色","靓蓝"}; 
    	new AlertDialog.Builder(this) 
			.setTitle("选择颜色") //此处 this 代表当前Activity 
			.setSingleChoiceItems(items, pin, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int item)
				{
					Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show(); //将选中的文本内容按照土司提示 方式显示出来, 此处的getApplicationContext() 得到的也是当前的Activity对象，可用当前Activity对象的名字.this代替（Activity.this） 
					pin = item;
					switch (item)
					{
						case 0:
							SetColor(R.color.white);
							saveColor(0);
							break;
						case 1:
							SetColor(R.color.red);
							saveColor(1);
							break;
						case 2:
							SetColor(R.color.black);
							saveColor(2);
							break;
						case 3:
							SetColor(R.color.yellow);
							saveColor(3);
							break;
						case 4:
							SetColor(R.color.green);
							saveColor(4);
							break;
						case 5:
							SetColor(R.color.pink);
							saveColor(5);
							break;
						case 6:
							SetColor(R.color.blue);
							saveColor(6);
							break;
						case 7:
							SetColor(R.color.lightBlue);
							saveColor(7);
							break;
						default:
							SetColor(R.color.white);
					}
					dialog.cancel();
				} 
			})
			.show();//显示对话框 
	}
	//选择亮度
	private void selectBright()
    {
    	final String[] items2 = {"100%", "90%", "80%", "75%", "50%", "30%", "20%" ,"10%","5%"}; 
    	new AlertDialog.Builder(this) 
			.setTitle("选择亮度") 
			.setSingleChoiceItems(items2, li ,new DialogInterface.OnClickListener()
			{ //此处数字为选项的下标，从0开始， 表示默认哪项被选中 
				public void onClick(DialogInterface dialog, int item) 
				{
					Toast.makeText(getApplicationContext(), items2[item],Toast.LENGTH_SHORT).show();
					li = item;
					switch (item)
					{
						case 0:
							SetBright(1.0f);
							saveBright(100);
							break;
						case 1:
							SetBright(0.90f);
							saveBright(90);
							break;
						case 2:
							SetBright(0.80f);
							saveBright(80);
							break;
						case 3:
							SetBright(0.75f);
							saveBright(75);
							break;
						case 4:
							SetBright(0.50f);
							saveBright(50);
							break;
						case 5:
							SetBright(0.30f);
							saveBright(30);
							break;
						case 6:
							SetBright(0.20f);
							saveBright(20);
							break;
						case 7:
							SetBright(0.10f);
							saveBright(10);
							break;
						case 8:
							SetBright(0.05f);
							saveBright(5);
							break;
						default:
							SetBright(1.0f);
					}
					dialog.cancel();
				}
			})
		.show();//显示对话框 
    }
	//设置默认颜色
	private void setDeColor()
	{
		SharedPreferences color = getSharedPreferences("torch", Context.MODE_PRIVATE);
		pin = color.getInt("colors", 0);
		if (pin == 0)
		{
			SetColor(R.color.white);
		}
		else if (pin == 1)
		{
			SetColor(R.color.red);
		}
		else if(pin == 2)
		{
			SetColor(R.color.black);
		}
		else if(pin == 3)
		{
			SetColor(R.color.yellow);
		}
		else if(pin == 4)
		{
			SetColor(R.color.green);
		}
		else if(pin == 5)
		{
			SetColor(R.color.pink);
		}
		else if(pin == 6)
		{
			SetColor(R.color.blue);
		}
		else if(pin == 7)
		{
			SetColor(R.color.lightBlue);
		}
		else
		{
			SetColor(R.color.title);
		}
	}
	//设置默认亮度
	private void setDeBright()
	{
		SharedPreferences brights = getSharedPreferences("torch", Context.MODE_PRIVATE);
		int bright = brights.getInt("bright", 100);
		if (bright == 100)
		{
			SetBright(1.0f);
			li = 0;
		}
		else if (bright == 90)
		{
			SetBright(0.90f);
			li = 1;
		}
		else if (bright == 80)
		{
			SetBright(0.80f);
			li = 2;
		}
		else if (bright == 75)
		{
			SetBright(0.75f);
			li = 3;
		}
		else if (bright == 50)
		{
			SetBright(0.50f);
			li = 4;
		}
		else if (bright == 30)
		{
			SetBright(0.30f);
			li = 5;
		}
		else if (bright == 20)
		{
			SetBright(0.20f);
			li = 6;
		}
		else if (bright == 10)
		{
			SetBright(0.10f);
			li = 7;
		}
		else if (bright == 5)
		{
			SetBright(0.05f);
			li = 8;
		}
		else
		{
			SetBright(1.0f);
			li = 0;
		}
	}
	//保存设置的亮度值
	private void saveBright(int bright)
	{
		SharedPreferences lights = getSharedPreferences("torch", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit2 = lights.edit();
		edit2.putInt("bright", bright);
		edit2.commit();
	}
	//保存设置的颜色值
	private void saveColor(int color)
	{
		SharedPreferences colors = getSharedPreferences("torch", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = colors.edit();
		edit.putInt("colors", color);
		edit.commit();
	}
	//设置屏幕颜色
    private void SetColor(int color_1)
    {
    	myColor = getBaseContext().getResources();
		Drawable color_M = myColor.getDrawable(color_1);
    	mylayout.setBackgroundDrawable(color_M);
    }
	//设置屏幕亮度
    private void SetBright(float light)
    {
    	WindowManager.LayoutParams lp=getWindow().getAttributes();
    	lp.screenBrightness=light;
    	getWindow().setAttributes(lp);
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
		setMyTheme();
		setDeColor();
		setDeBright();
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
}

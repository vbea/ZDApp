package com.binxin.zdapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.LayoutInflater;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.ViewGroup.LayoutParams;
import android.view.KeyEvent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import android.graphics.Typeface;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.CountdownTimer; //倒计时核心类
import com.binxin.zdapp.wheel.WheelView;	//时间选择自定义控件
import com.binxin.zdapp.wheel.StrericWheelAdapter; 
import com.binxin.zdapp.view.RoundProgressBar; //自定义进度条控件
import com.binxin.zdapp.classes.Common;

public class App_countDown extends Activity
{
	private boolean CountState = false;	//正在计时
	private boolean StartCount = false; //开始计时
	private WheelView hourWheel,minuteWheel,secondWheel;
	private String[] hourContent,minuteContent,secondContent;
	private int mHour = 0,mMinute = 0,mSecond = 0;
	private int mUnit;
	private long mTimeCount = 0;
	private Countdown mCoutd;
	private RoundProgressBar mPro;
	private Button btn_star,btn_time,btn_stop;
	private LinearLayout titleLayout;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.countdown);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		mPro = (RoundProgressBar) findViewById(R.id.countProgess);
		btn_time = (Button) findViewById(R.id.btn_timeCount);
		btn_star = (Button) findViewById(R.id.btn_countd);
		btn_stop = (Button) findViewById(R.id.btn_count1m);
		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		init();
		
		close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (StartCount)
				{
					AlertDialog.Builder bbu = new AlertDialog.Builder(App_countDown.this);
					bbu.setTitle("提示");
					//bbu.setIcon(R.drawable.ic_icon);
					bbu.setMessage("当前正在计时，确定要退出吗？");
					bbu.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog,int swich)
						{
							finish();
							overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
						}
					});
					bbu.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog,int swich)
						{
							dialog.cancel();
						}
					});
					bbu.create();
					bbu.show();
				}
				else
				{
					finish();
					overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				}
			}
		});
		btn_time.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				onTimePicker();
			}
		});
		btn_star.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (mTimeCount != 0)
				{
					if (StartCount)
					{
						if (!CountState)
						{
							mCoutd.start();
							CountState = true;
						}
						else
						{
							mCoutd.pause();
							CountState = false;
						}
					}
					else
					{
						//mTimeCount = ((mHour * 3600) + (mMinute * 60) + mSecond) * 1000;
						mCoutd = new Countdown(mTimeCount,mUnit);
						mCoutd.start();
						CountState = true;
						StartCount = true;
					}
					invalidate();
				}
				else
				{
					Toast.makeText(getApplicationContext(),"请先选择时间",Toast.LENGTH_SHORT).show();
				}
			}
		});
		btn_stop.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				cacelCount();
				invalidate();
			}
		});
	}
	
	private void cacelCount()
	{
		mCoutd.cancel();
		mCoutd = null;
		CountState = false;
		StartCount = false;
		mPro.setText(mPro.START_TIME);
		mPro.setProgress(0);
		mTimeCount = 0;
	}
	
	private void init()
	{
		AssetManager mgr = getAssets();
		mPro.setTypeface(Typeface.createFromAsset(mgr,"fonts/zdapp.ttf"));
		mPro.setMax(720);
		SharedPreferences spf = getSharedPreferences("zdapp",Context.MODE_PRIVATE);
		mUnit = spf.getInt("countUnit",50);
		hourContent = new String[24];
		for(int i=0;i<24;i++)
		{
			hourContent[i]= String.valueOf(i);
			if(hourContent[i].length()<2)
	        {
				hourContent[i] = "0"+hourContent[i];
	        }
		}

		minuteContent = new String[60];
		for(int i=0;i<60;i++)
		{
			minuteContent[i]=String.valueOf(i);
			if(minuteContent[i].length()<2)
	        {
				minuteContent[i] = "0"+minuteContent[i];
	        }
		}

		secondContent = new String[60];
		for(int i=0;i<60;i++)
		{
			secondContent[i]=String.valueOf(i);
			if(secondContent[i].length()<2)
	        {
				secondContent[i] = "0"+secondContent[i];
	        }
		}
	}
	
	private void invalidate()
	{
		if (StartCount)
		{
			if (CountState)
			{
				btn_star.setText(R.string.pause);
				btn_stop.setVisibility(View.VISIBLE);
			}
			else
			{
				btn_star.setText(R.string.Continue);
				btn_stop.setVisibility(View.VISIBLE);
			}
		}
		else
		{
			btn_star.setText(R.string.start);
			btn_stop.setVisibility(View.GONE);
		}
	}
	
	public void onTimePicker()
    {
		View view = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.timepicker, null); 

		int curHour = mHour;
		int curMinute = mMinute;
		int curSecond = mSecond;

		hourWheel = (WheelView)view.findViewById(R.id.hourwheel);
		minuteWheel = (WheelView)view.findViewById(R.id.minutewheel);
		secondWheel = (WheelView)view.findViewById(R.id.secondwheel);

		setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);  
		builder.setView(view); 

		hourWheel.setAdapter(new StrericWheelAdapter(hourContent));
		hourWheel.setCurrentItem(curHour);
		hourWheel.setCyclic(true);
		hourWheel.setInterpolator(new AnticipateOvershootInterpolator());

		minuteWheel.setAdapter(new StrericWheelAdapter(minuteContent));
		minuteWheel.setCurrentItem(curMinute);
		minuteWheel.setCyclic(true);
		minuteWheel.setInterpolator(new AnticipateOvershootInterpolator());

		secondWheel.setAdapter(new StrericWheelAdapter(secondContent));
		secondWheel.setCurrentItem(curSecond);
		secondWheel.setCyclic(true);
		secondWheel.setInterpolator(new AnticipateOvershootInterpolator());

		builder.setTitle(R.string.cd_select);  
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
		{
			@Override  
			public void onClick(DialogInterface dialog, int which)
			{
				mHour = Integer.parseInt(hourWheel.getCurrentItemValue());
				mMinute = Integer.parseInt(minuteWheel.getCurrentItemValue());
				mSecond = Integer.parseInt(secondWheel.getCurrentItemValue());
				mTimeCount = ((mHour * 3600) + (mMinute * 60) + mSecond) * 1000;
				if (mTimeCount == 0)
				{
					Common.showShortToast(getApplicationContext(),"无效的时间");
				}
				else
				{
					if (CountState && StartCount)
					{
						try
						{
							mCoutd.cancel();
							mCoutd = new Countdown(mTimeCount,mUnit);
							mCoutd.start();
							CountState = true;
							StartCount = true;
						}
						catch (Exception e)
						{
							cacelCount();
						}
					}
					else
					{
						if (mCoutd != null)
							cacelCount();
						mTimeCount = ((mHour * 3600) + (mMinute * 60) + mSecond) * 1000;
						mPro.setText(hourWheel.getCurrentItemValue()+":"+minuteWheel.getCurrentItemValue()+":"+secondWheel.getCurrentItemValue());
						mPro.setProgress(0);
					}
					invalidate();
				}
			}  
		});
		builder.show();
    }
	class Countdown extends CountdownTimer
	{
		@Override
		public Countdown(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture,countDownInterval);
		}
		
		@Override
		public void onTick(long millisUntilFinished, int percent)
		{
			//mTimeCount = millisUntilFinished;
			int _hou = (int)millisUntilFinished / 3600000;
			int _min = (int)(millisUntilFinished / 60000) % 60;
			int _sec = (int)(millisUntilFinished / 1000) % 60;
			mPro.setText(String.format("%1$02d:%2$02d:%3$02d", _hou, _min, _sec));
			mPro.setProgress(percent);
		}

		@Override
		public void onFinish()
		{
			cacelCount();
			mPro.setProgress(mPro.getMax());
			Toast.makeText(getApplicationContext(),"倒计时完成",Toast.LENGTH_SHORT).show();
			invalidate();
		}
	}
	public void setMyTheme()
	{
		android.content.SharedPreferences sube = getSharedPreferences("zdapp", android.content.Context.MODE_PRIVATE);
		int code = sube.getInt("themeCode", 0);
		MyThemes.setThemes(this, titleLayout, code);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == event.KEYCODE_BACK && event.getAction() == event.ACTION_DOWN)
		{
			if (!StartCount)
			{
				this.finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
			else
			{
				return false;
			}
			//super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume()
	{
		setMyTheme();
		invalidate();
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
		if (mCoutd != null)
		{
			mCoutd.cancel();
			mCoutd = null;
		}
		super.onDestroy();
	}
}

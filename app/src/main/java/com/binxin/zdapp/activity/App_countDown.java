package com.binxin.zdapp.activity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.LayoutInflater;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.ViewGroup.LayoutParams;
import android.view.KeyEvent;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.AudioManager;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.CountdownTimer; //倒计时核心类
import com.binxin.zdapp.wheel.WheelView;	//时间选择自定义控件
import com.binxin.zdapp.wheel.StrericWheelAdapter; 
import com.binxin.zdapp.view.RoundProgressBar; //自定义进度条控件
import com.binxin.zdapp.classes.Common;
import com.binxin.zdapp.classes.ExceptionHandler;

/*邠心工作室*
 *正德应用*
 *倒计时功能*/
public class App_countDown extends Activity
{
	private boolean CountState = false;	//正在计时
	private boolean StartCount = false; //开始计时
	private WheelView hourWheel,minuteWheel,secondWheel;
	private String[] hourContent,minuteContent,secondContent;
	private int mHour = 0,mMinute = 0,mSecond = 0;
	private int mUnit;
	private long mTimeCount = 0;
	private String mPath;
	private boolean mRing, mVibrate;
	private MediaPlayer mediaPlayer;
	private Vibrator mVibrator;
	private Countdown mCoutd;
	private RoundProgressBar mPro;
	private Button btn_star,btn_time,btn_stop,btn_ring,btn_vibrate;
	private LinearLayout titleLayout, layoutIn;
	private RelativeLayout layoutOut;
	private String pasText = "";
	private long[] pattern = {1000, 1000, 1000, 1000};
	private int PRO_MAX = 990;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.countdown);
		setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		mPro = (RoundProgressBar) findViewById(R.id.countProgess);
		btn_time = (Button) findViewById(R.id.btn_timeCount);
		btn_star = (Button) findViewById(R.id.btn_countd);
		btn_stop = (Button) findViewById(R.id.btn_count1m);
		btn_ring = (Button) findViewById(R.id.btn_timeRing);
		btn_vibrate = (Button) findViewById(R.id.btn_timeVibrate);
		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		layoutIn = (LinearLayout) findViewById(R.id.count_linIn);
		layoutOut = (RelativeLayout) findViewById(R.id.count_relOut);
		init();
		
		close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (StartCount)
				{
					AlertDialog.Builder bbu = new AlertDialog.Builder(App_countDown.this);
					bbu.setTitle(android.R.string.dialog_alert_title);
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
				onTimePicker(false);
			}
		});
		btn_star.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (StartCount)//正在运行
				{
					if (!CountState)//暂停
					{
						mCoutd.start();
						CountState = true;
					}
					else//继续
					{
						mCoutd.pause();
						CountState = false;
						mHandler.sendEmptyMessage(1);
					}
					invalidate();
				}
				else
				{
					if (mTimeCount != 0)
					{
						//mTimeCount = ((mHour * 3600) + (mMinute * 60) + mSecond) * 1000;
						setMax();
						mCoutd = new Countdown(mTimeCount,mUnit, PRO_MAX);
						mCoutd.start();
						CountState = true;
						StartCount = true;
						invalidate();
					}
					else
					{
						//CountState = StartCount = true;
						onTimePicker(true);
						//Toast.makeText(getApplicationContext(),"请先选择时间",Toast.LENGTH_SHORT).show();
					}
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
		btn_ring.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (mRing) {
					AlertDialog.Builder builder = new AlertDialog.Builder(App_countDown.this);
					builder.setTitle(R.string.tips);
					builder.setMessage(getString(R.string.close) + getString(R.string.de_beep) + ": " + mPath + "?");
					builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int s)
						{
							mRing = !mRing;
							mPath = "";
							setButtonStatus();
						}
					});
					builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int s)
						{
							dialog.cancel();
						}
					});
					builder.setNeutralButton(R.string.brows, new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int s)
						{
							startActivityForResult(new Intent(App_countDown.this, SelectAudio.class), 0);
						}
					});
					builder.show();
				}
				else
					startActivityForResult(new Intent(App_countDown.this, SelectAudio.class), 0);
			}
		});
		btn_vibrate.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				mVibrate = !mVibrate;
				setButtonStatus();
			}
		});
	}
	
	private void cacelCount()
	{
		if (mCoutd != null)
		{
			mCoutd.cancel();
			mCoutd = null;
		}
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
		mPro.setMax(PRO_MAX);
		SharedPreferences spf = getSharedPreferences("zdapp",Context.MODE_PRIVATE);
		mUnit = spf.getInt("countUnit",10);
		mRing =spf.getBoolean("countRing", false);
		mVibrate = spf.getBoolean("countVibrate", false);
		mPath = spf.getString("countRingPath", "");
		if (mPath.equals(""))
			mRing = false;
		setButtonStatus();
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
	
	private void setButtonStatus()
	{
		btn_ring.setText(getString(R.string.de_beep) + ": " + (mRing ? getString(R.string.turn_on) : getString(R.string.turn_off)));
		btn_vibrate.setText(getString(R.string.de_vibrate) + ": " + (mVibrate ? getString(R.string.turn_on) : getString(R.string.turn_off)));
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
	
	private void playRing()
	{
		//File audio = new File(Environment.getExternalStorageDirectory(), mPath.replace(Environment.getExternalStorageState(), "").substring(1));
		if (mRing)
		{
			try
			{
				if (mediaPlayer == null)
					mediaPlayer = new MediaPlayer();
				else
					mediaPlayer.reset();// 把各项参数恢复到初始状态
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.setDataSource(mPath);
				mediaPlayer.prepare();// 进行缓冲
				mediaPlayer.setLooping(true);
				mediaPlayer.start();
			}
			catch (Exception e)
			{
				ExceptionHandler.log("CountDown_Ring_Play:"+e.toString());
			}
		}
		if (mVibrate)
		{
			if (mVibrator == null)
				mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			if (mVibrator.hasVibrator())
			{
				mVibrator.vibrate(pattern, 0);
			}
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(App_countDown.this);
		builder.setTitle(R.string.countdown);
		builder.setMessage(getString(R.string.timeup)  + (mRing ? "\n" + mPath.substring(mPath.lastIndexOf(File.separator) + 1, mPath.lastIndexOf(".")) : ""));
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int s)
			{
				dialog.cancel();
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			public void onCancel(DialogInterface dialog)
			{
				if (mRing && mediaPlayer != null && mediaPlayer.isPlaying())
					mediaPlayer.stop();
				if (mVibrate)
					mVibrator.cancel();
			}
		});
		builder.show();
	}
	
	public void onTimePicker(final boolean start)
    {
		View view = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.timepicker, null); 

		int curHour = mHour;
		int curMinute = mMinute;
		int curSecond = mSecond;

		hourWheel = (WheelView)view.findViewById(R.id.hourwheel);
		minuteWheel = (WheelView)view.findViewById(R.id.minutewheel);
		secondWheel = (WheelView)view.findViewById(R.id.secondwheel);

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
					Common.showShortToast(getApplicationContext(), R.string.validTime);
				}
				else
				{
					if ((CountState && StartCount) || start)
					{
						try
						{
							if (mCoutd != null)
								mCoutd.cancel();
							setMax();
							mCoutd = new Countdown(mTimeCount,mUnit, PRO_MAX);
							mCoutd.start();
							CountState = StartCount = true;
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
	
	private void setMax()
	{
		PRO_MAX = (int)(mTimeCount / mUnit);
	}
	
	class Countdown extends CountdownTimer
	{
		@Override
		public Countdown(long millisInFuture, long countDownInterval, int max)
		{
			super(millisInFuture,countDownInterval, max);
		}
		
		@Override
		public void onTick(long millisUntilFinished, int percent)
		{
			//mTimeCount = millisUntilFinished;
			int _hou = (int)millisUntilFinished / 3600000;
			int _min = (int)(millisUntilFinished / 60000) % 60;
			int _sec = ((int)(millisUntilFinished / 1000) % 60) + 1;
			if (_sec >= 60)
			{
				_sec %= 60;
				_min +=1;
			}
			if (_min >= 60)
			{
				_min %= 60;
				_hou += 1;
			}
			mPro.setText(String.format("%1$02d:%2$02d:%3$02d", _hou, _min, _sec));
			mPro.setProgress(percent);
		}

		@Override
		public void onFinish()
		{
			cacelCount();
			mPro.setProgress(mPro.getMax());
			if (mRing || mVibrate)
				playRing();
			else
				Toast.makeText(getApplicationContext(),"倒计时完成",Toast.LENGTH_SHORT).show();
			invalidate();
		}
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					pasText = mPro.getText();
					mHandler.sendEmptyMessageDelayed(2, 500);
					break;
				case 2:
					if (!CountState && StartCount)
					{
						mPro.setTextPost("");
						mHandler.sendEmptyMessageDelayed(3, 500);
					}
					break;
				case 3:
					if (!CountState && StartCount)
					{
						mPro.setTextPost(pasText);
						mHandler.sendEmptyMessageDelayed(2, 500);
					}
					break;
			}
			switch(msg.what)
			{
				
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK)
		{
			Bundle b = data.getBundleExtra("data");
			mPath = b.getString("path");
			mRing = true;
			setButtonStatus();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		int width = layoutIn.getWidth();
		int height = layoutIn.getHeight();
		LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(width, height);
		para.setMargins(0, (height-width)/2, 0, 0);
		mPro.setLayoutParams(para);
		super.onWindowFocusChanged(hasFocus);
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
		MyThemes.setThemes(this, titleLayout);
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
		SharedPreferences spf = getSharedPreferences("zdapp",Context.MODE_PRIVATE);
		SharedPreferences.Editor edt = spf.edit();
		edt.putBoolean("countRing", mRing);
		edt.putBoolean("countVibrate", mVibrate);
		edt.putString("countRingPath", mPath);
		edt.commit();
		if (mediaPlayer != null)
			mediaPlayer.release();
		if (mCoutd != null)
		{
			mCoutd.cancel();
			mCoutd = null;
		}
		super.onDestroy();
	}
}

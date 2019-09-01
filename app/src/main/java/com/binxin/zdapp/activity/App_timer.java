package com.binxin.zdapp.activity;  
  
import java.io.ByteArrayOutputStream;  
import java.io.FileInputStream;  
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;  
import java.util.ArrayList;
import java.text.SimpleDateFormat;
  
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;  
import android.os.Bundle;  
import android.os.Handler;  
import android.os.Message;  
import android.view.LayoutInflater;  
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;  
import android.widget.TextView;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.content.res.AssetManager;
import android.graphics.Typeface; 

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.ExceptionHandler;
  
public class App_timer extends Activity
{  
    private long mlCount = 0;  
    private long mlTimerUnit = 1;  
    private TextView tvTime;  
    private ImageButton btnStartPause, close, btnStop, btnSec;  
    private Timer timer = null;  
    private TimerTask task = null;  
    //private Handler handler = null;  
	private long lastTime = 0;
	private int viewUi = 1;
	private int viewId = 1;
	private boolean iact = false;
    private Message msg = null;  
    private boolean bIsRunningFlg = false;
	private boolean blFirstMode = true;
	private LinearLayout timerList;
	private Button btnAdd,btnClear;
	private LinearLayout titleLayout;
      
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState)
	{  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.timer);  
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        tvTime = (TextView)findViewById(R.id.tvTime);
		AssetManager mgr = getAssets();//得到AssetManager
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/zdapp.ttf");//根据路径得到Typeface
		tvTime.setTypeface(tf);//设置字体 
        btnStartPause = (ImageButton)findViewById(R.id.btnStartPaunse);  
        btnStop = (ImageButton)findViewById(R.id.btnStop);  
		btnSec = (ImageButton) findViewById(R.id.btnSec);
		close = (ImageButton) findViewById(R.id.btn_close);
        timerList = (LinearLayout) findViewById(R.id.timerListView);
		btnAdd = (Button) findViewById(R.id.btn_timerAdd);
		btnClear = (Button) findViewById(R.id.btn_timerClear);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		
		SharedPreferences sharedPreferences = getSharedPreferences("ZDApp_timer", Context.MODE_PRIVATE);  
        //getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值  
        mlTimerUnit = sharedPreferences.getLong("time_unit", 10);

        if (mlTimerUnit == 100)
		{
            tvTime.setText(R.string.init_time_second);
			btnSec.setImageResource(R.drawable.bg_btn_timer0);
        }
		else
		{  
            tvTime.setText(R.string.init_time_100millisecond);
			btnSec.setImageResource(R.drawable.bg_btn_timer1);
        }
		//事件处理
		btnSec.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				SharedPreferences sharedPreferences = getSharedPreferences("ZDApp_timer", Context.MODE_PRIVATE);
				long d = sharedPreferences.getLong("time_unit",10);
				if (d == 10)
				{  
                    mlTimerUnit = 100;
					btnSec.setImageResource(R.drawable.bg_btn_timer0);
					if (mlCount == 0)
                		tvTime.setText(R.string.init_time_second);// 100毫秒
					if (!blFirstMode)
						handler.sendEmptyMessage(2);
				}
                else if (d == 100)
				{  
                    mlTimerUnit = 10;
					btnSec.setImageResource(R.drawable.bg_btn_timer1);
                	if (mlCount == 0)
						tvTime.setText(R.string.init_time_100millisecond);
					if (!blFirstMode)
						handler.sendEmptyMessage(2);
				}
				try
				{
					SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器  
					editor.putLong("time_unit", mlTimerUnit);  
					editor.commit();//提交修改  
				}
				catch(Exception e)
				{  
					e.printStackTrace();
				}
			}
		});
		close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				onDestroy();
			}
		});
		btnAdd.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if(blFirstMode)
				{
					return;
					//blFirstMode = false;
					//timerList.removeAllViews();
				}
				if (lastTime != 0)
				{
					addItem(viewUi,viewId,subTime(mlCount,lastTime),tvTime.getText().toString());
				}
				else
				{
					addItem(viewUi,viewId,tvTime.getText().toString(),tvTime.getText().toString());
					lastTime = mlCount;
				}
			}
		});
		btnClear.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				viewId = 1;
				viewUi = 1;
				timerList.removeAllViews();
			}
		});
		btnStartPause.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if(blFirstMode)
				{
					blFirstMode = false;
					//timerList.removeAllViews();
				}
				restart();//开始
				if (!bIsRunningFlg)
				{  
					bIsRunningFlg = true;  
					btnStartPause.setImageResource(R.drawable.bg_btn_pause);  
				}
				else
				{
					// pause  
					try
					{  
						bIsRunningFlg = false;  
						pause(); 
						btnStartPause.setImageResource(R.drawable.bg_btn_restart);  
					}
					catch(Exception e)
					{  
						e.printStackTrace();  
					}
				}
			}
		});
		btnStop.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (null != timer)
				{
					task.cancel();  
					task = null;  
					timer.cancel(); // Cancel timer  
					timer.purge();  
					timer = null;  
					handler.removeMessages(msg.what);  
				} 
				mlCount = 0;
				lastTime = 0;
				viewId = 1;
				if (iact)
				{
					viewUi++;
					iact = false;
				}
				bIsRunningFlg = false;
				blFirstMode = true;
				btnStartPause.setImageResource(R.drawable.bg_btn_start);
				if (mlTimerUnit == 100)
				{  
					// second  
					tvTime.setText(R.string.init_time_second);  
				}
				else if (mlTimerUnit == 10)
				{  
					// 100 millisecond  
					tvTime.setText(R.string.init_time_100millisecond);  
				}  
			}
		});
    }
	private Handler handler = new Handler()
	{  
		@Override  
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{  
				case 1:  
					mlCount++;  
					showTimer();
					break;
				case 2:
					showTimer();
					break;
				default:  
					break;  
			}
			super.handleMessage(msg);  
		}
	};

	private void showTimer()
	{
		int totalSec = 0;  
		int yushu = 0;
		// 显示时间
		totalSec = (int)(mlCount/100);  
		yushu = (int)(mlCount % 100);
		int min = (totalSec / 60);  
		int sec = (totalSec % 60);
		int hou = (totalSec / 3600);
		if (min >= 60)
		{
			min -= 60 * hou;
		}
		try
		{
			if (mlTimerUnit == 100)
				tvTime.setText(String.format("%1$02d:%2$02d:%3$02d", hou, min, sec));
			else
				tvTime.setText(String.format("%1$02d:%2$02d:%3$02d.%4$02d", hou, min, sec, yushu));
		}
		catch(Exception e)
		{  
			tvTime.setText(hou + ":" + min + ":" + sec + "." + yushu);  
			e.printStackTrace();
		} 
	}
	private void pause()
	{
		task.cancel();  
		task = null;  
		timer.cancel(); // Cancel timer  
		timer.purge();  
		timer = null;  
		handler.removeMessages(msg.what); 
	}
	
	private void restart()
	{
		if (null == timer)
		{  
			if (null == task)
			{  
				task = new TimerTask()
				{  
					@Override  
					public void run()
					{  
						// TODO Auto-generated method stub  
						if (null == msg)
						{  
							msg = new Message();  
						}
						else
						{  
							msg = Message.obtain();  
						}  
						msg.what = 1;  
						handler.sendMessage(msg);  
					}   
				};  
			}
			timer = new Timer(true);  
			timer.schedule(task, 10, 10); // set timer duration  
		}
	}
	
	private void addItem(int ui,int id,String now,String total)
	{
		View listView = getLayoutInflater().inflate(R.layout.timerlist,null);
		TextView txt_id = (TextView)listView.findViewById(R.id.timerlist_id);
		TextView txt_sub = (TextView)listView.findViewById(R.id.timerlist_sub);
		TextView txt_tol = (TextView)listView.findViewById(R.id.timerlist_all);
		txt_id.setText(ui+"#"+id);
		txt_sub.setText(now);
		txt_tol.setText(total);
		timerList.addView(listView,0);
		viewId++;
		iact = true;
	}
	
	private String subTime(long time1,long time2)
	{
		lastTime = time1;
		try
		{
			int temp = (int)(time1 - time2);
			int totalSec = temp / 100;
			int yushu = temp % 100;
			int min = (totalSec / 60);  
			int sec = (totalSec % 60);
			int hou = (totalSec / 3600);
			if (min >= 60)
			{
				min -= 60 * hou;
			}
			if (mlTimerUnit == 10)
				return String.format("%1$02d:%2$02d:%3$02d.%4$02d", hou, min, sec, yushu);
			else
				return String.format("%1$02d:%2$02d:%3$02d", hou, min, sec);
		}
		catch (Exception e)
		{
			return "00:00:00.00";
		}
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		// pause  
		/*try
		{  
			bIsRunningFlg = false;  
			task.cancel();  
			task = null;  
			timer.cancel(); // Cancel timer  
			timer.purge();  
			timer = null;  
			handler.removeMessages(msg.what);  
			btnStartPause.setImageResource(R.drawable.bg_btn_restart);  
		}
		catch(Exception e)
		{  
			e.printStackTrace();  
		}*/
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	protected void onResume()
	{
		MyThemes.setThemes(this,titleLayout);
		super.onResume();
	}
} 

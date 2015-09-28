package com.binxin.zdapp.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.KeyEvent;
import android.content.*;
import android.widget.*;
import org.xml.sax.*;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class App_sensor extends Activity
{
	//private SurfaceView surfaceView;
	private TextView sensort, sensort3;
	private ImageView sensort2;
	private float x = 0, y = 0, z = 0;
	private Paint paint;
	private LinearLayout titleLayout;
	//public static App_sensortest ma;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//ma = App_sensortest.this;
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.sensor);
		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		close.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					finish();
					overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				}
			});
		sensort = (TextView) findViewById(R.id.textsensor);
		sensort3 = (TextView) findViewById(R.id.textsensor3);
		sensort2= (ImageView) findViewById(R.id.textsensor2);
		new MySurfaceView(this);
		//surfaceView.setBackgroundResource(R.drawable.background);

	}
	class MySurfaceView extends SurfaceView implements Callback, Runnable
	{
		private Thread th = new Thread(this);
		private SurfaceHolder sfh;


		private SensorManager sm;
		private Sensor sensor;
		private SensorEventListener mySensorListener;
		private int arc_x, arc_y;// 圆形的x,y位置


		public MySurfaceView(Context context)
		{
			super(context);
			this.setKeepScreenOn(true);
			sfh = this.getHolder();
			sfh.addCallback(this);
			paint = new Paint();
			paint.setAntiAlias(true);
			setFocusable(true);
			setFocusableInTouchMode(true);
			//通过服务得到传感器管理对象 
			sm = (SensorManager) App_sensor.this.getSystemService(Service.SENSOR_SERVICE);
			sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//得到一个重力传感器实例
			//TYPE_ACCELEROMETER    加速度传感器(重力传感器)类型。
			//TYPE_ALL              描述所有类型的传感器。
			//TYPE_GYROSCOPE        陀螺仪传感器类型
			//TYPE_LIGHT            光传感器类型
			//TYPE_MAGNETIC_FIELD   恒定磁场传感器类型。
			//TYPE_ORIENTATION      方向传感器类型。
			//TYPE_PRESSURE         描述一个恒定的压力传感器类型
			//TYPE_PROXIMITY        常量描述型接近传感器
			//TYPE_TEMPERATURE      温度传感器类型描述
			mySensorListener = new SensorEventListener()
			{
				@Override
				//传感器获取值发生改变时在响应此函数
				public void onSensorChanged(SensorEvent event)
				{
					//备注1 
					//传感器获取值发生改变，在此处理 
					x = event.values[0]; //手机横向翻滚
					//x>0 说明当前手机左翻 x<0右翻     
					y = event.values[1]; //手机纵向翻滚
					//y>0 说明当前手机下翻 y<0上翻
					z = event.values[2]; //屏幕的朝向
					//z>0 手机屏幕朝上 z<0 手机屏幕朝下
					arc_x += x;//备注2
					arc_y += y;
					int left = 0;
					int right = 0;
					int top = 0;
					int bottom = 0;
					//arc_x = sensort2.getWidth() / 2 - 25;
					//arc_y = sensort2.getHeight() / 2 - 25;
					if (arc_x < 0)//左
					{
						left = arc_x * -1;
						right = 0;
					}
					else if (arc_x > 0)//右
					{
						right = arc_x;
						left = 0;
					}
					/*if (arc_x == 0)
					 {
					 right = arc_x;
					 left = arc_x;
					 }*/
					if (arc_y < 0)//上
					{
						bottom = arc_y * -1;
						top = 0;
					}
					else if (arc_y > 0)//下
					{
						top = arc_y;
						bottom = 0;
					}
					/*if (arc_y == 0)
					 {
					 bottom = arc_y;
					 left = arc_y;
					 }*/
					sensort3.setText("当前手机重力传感器的值\nx="+x+"y="+y+"z="+z);
					sensort2.setPadding(left,top,right,bottom);
					if (x < 1 && x > -1 && y < 1 && y > -1)
					{
						sensort.setText("当前手机处于水平放置的状态");
					}
					else if (x > 1)
					{
						sensort.setText("当前手机处于向左翻的状态");
						if (y > 1)
						{
							sensort.setText(sensort.getText().toString() + "\n当前手机处于向下翻的状态");
						}
						else if (y < -1)
						{
							sensort.setText(sensort.getText().toString() + "\n当前手机处于向上翻的状态");
						}
					}
					else if (x < -1)
					{
						sensort.setText("当前手机处于向右翻的状态");
						if (y > 1)
						{
							sensort.setText(sensort.getText().toString() + "\n当前手机处于向下翻的状态");
						}
						else if (y < -1)
						{
							sensort.setText(sensort.getText().toString() + "\n当前手机处于向上翻的状态");
						}
					}
					else if (y > 1)
					{
						sensort.setText("当前手机处于向下翻的状态");
					}
					else if (y < -1)
					{
						sensort.setText("当前手机处于向上翻的状态");
					}
					if (z > 0)
					{
						sensort.setText(sensort.getText().toString() + "\n并且屏幕朝上");
					}
					else
					{
						sensort.setText(sensort.getText().toString() + "\n并且屏幕朝下\n(别躺着玩手机，对眼睛不好哟)");
					}
				}

				@Override
				//传感器的精度发生改变时响应此函数
				public void onAccuracyChanged(Sensor sensor, int accuracy)
				{
					// TODO Auto-generated method stub
				}
			};
			sm.registerListener(mySensorListener, sensor, SensorManager.SENSOR_DELAY_GAME);
			//第一个参数是传感器监听器，第二个是需要监听的传感实例
			//最后一个参数是监听的传感器速率类型： 一共一下四种形式
			//SENSOR_DELAY_NORMAL  正常
			//SENSOR_DELAY_UI  适合界面
			//SENSOR_DELAY_GAME  适合游戏  (我们必须选这个呀 哇哈哈~)
			//SENSOR_DELAY_FASTEST  最快
		}

		public void surfaceCreated(SurfaceHolder holder)
		{
			arc_x = this.getWidth() / 2 - 25;
			arc_y = this.getHeight() / 2 - 25;
			th.start();
		}

		/*public void draw()
		 {
		 try
		 {
		 canvas = sfh.lockCanvas();
		 if (canvas != null)
		 {
		 canvas.drawColor(Color.BLACK);
		 paint.setColor(Color.RED);
		 canvas.drawArc(new RectF(arc_x, arc_y, arc_x + 50,
		 arc_y + 50), 0, 360, true, paint);
		 paint.setColor(Color.YELLOW);
		 canvas.drawText("当前重力传感器的值:", arc_x - 50, arc_y-30, paint);
		 canvas.drawText("x=" + x + ",y=" + y + ",z=" + z,
		 arc_x - 50, arc_y, paint);
		 String temp_str1= "ZDApp提示：";
		 String temp_str2 = "";
		 String temp_str3 = "";
		 String temp_str4 = "";
		 if (x < 1 && x > -1 && y < 1 && y > -1)
		 {
		 temp_str2 += "\n当前手机处于水平放置的状态";
		 sensort.setText("当前手机处于水平放置的状态");
		 if (z > 0)
		 {
		 sensort.setText(sensort + "\n并且屏幕朝上");
		 temp_str3 += "并且屏幕朝上";
		 }
		 else
		 {
		 sensort.setText(sensort + "\n并且屏幕朝下\n(别躺着玩手机，对眼睛不好哟)");
		 temp_str3 += "并且屏幕朝下";
		 temp_str4 += "(别躺着玩手机，对眼睛不好哟)";
		 }
		 }
		 else
		 {
		 if (x > 1)
		 {
		 sensort.setText("当前手机处于向左翻的状态");
		 temp_str2 += "当前手机处于向左翻的状态";
		 }
		 else if (x < -1)
		 {
		 temp_str2 += "当前手机处于向右翻的状态";
		 }
		 if (y > 1)
		 {
		 temp_str2 += "当前手机处于向下翻的状态";
		 }
		 else if (y < -1)
		 {
		 temp_str2 += "当前手机处于向上翻的状态";
		 }
		 if (z > 0)
		 {
		 temp_str3 += "并且屏幕朝上";
		 }
		 else
		 {
		 temp_str3 += "并且屏幕朝下";
		 temp_str4 += "(别躺着玩手机，对眼睛不好哟)";
		 }
		 }
		 paint.setTextSize(20);
		 canvas.drawText(temp_str1, 0, 20, paint);
		 canvas.drawText(temp_str2, 0, 50, paint);
		 canvas.drawText(temp_str3, 0, 80, paint);
		 canvas.drawText(temp_str4, 0, 110, paint);
		 }
		 }
		 catch (Exception e)
		 {
		 Log.v("ZDApp", "draw is Error!");
		 }
		 finally
		 {
		 sfh.unlockCanvasAndPost(canvas);
		 }
		 }*/

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			while (true)
			{
				//draw();
				try
				{
					Thread.sleep(100);
				}
				catch (Exception ex)
				{
				}
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
		{
		}

		public void surfaceDestroyed(SurfaceHolder holder)
		{
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		// TODO Auto-generated method stub
		// return super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			//Intent intent = new Intent(App_sensortest.this,App_hardware.class);
			//startActivity(intent);
			this.finish();
			onDestroy();
			//return false;
		}
		return true;
	}
	public void setMyTheme()
	{
		android.content.SharedPreferences sube = getSharedPreferences("zdapp", android.content.Context.MODE_PRIVATE);
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
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
}

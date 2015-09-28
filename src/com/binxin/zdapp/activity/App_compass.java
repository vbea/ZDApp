package com.binxin.zdapp.activity;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.content.*;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
/**
 * @author  指南针应用
 * @version  6.0.2
 */
public class App_compass extends Activity implements SensorEventListener
{
	ImageView image;  //指南针图片
	float currentDegree = 0f; //指南针图片转过的角度
	LinearLayout titleLayout;
	
	SensorManager mSensorManager; //管理器
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass);
        
        image = (ImageView)findViewById(R.id.compassimg);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE); //获取管理服务
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		ImageButton btn_close = (ImageButton)findViewById(R.id.btn_close);
		/*ImageButton btn_comto = (ImageButton)findViewById(R.id.btn_torchcompass);
		btn_comto.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(App_compass.this, App_comtorch.class);
				startActivity(intent);
			}
		});*/
		btn_close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
    }
	public void setMyTheme()
	{
		android.content.SharedPreferences sube = getSharedPreferences("zdapp", android.content.Context.MODE_PRIVATE);
		int code = sube.getInt("themeCode", 0);
		MyThemes.setThemes(this, titleLayout, code);
	}
    @Override 
    protected void onResume()
	{
    	super.onResume();
		setMyTheme();
    	//注册监听器
    	mSensorManager.registerListener(this
    			, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }
    //取消注册
    @Override
    protected void onPause()
	{
    	mSensorManager.unregisterListener(this);
    	super.onPause();
    }
    @Override
    protected void onStop(){
    	mSensorManager.unregisterListener(this);
    	super.onStop();
    }

    //传感器值改变
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// TODO Auto-generated method stub
	}
    //精度改变
	@Override
	public void onSensorChanged(SensorEvent event)
	{
		// TODO Auto-generated method stub
		//获取触发event的传感器类型
		int sensorType = event.sensor.getType();
		
		switch(sensorType)
		{
			case Sensor.TYPE_ORIENTATION:
			float degree = event.values[0]; //获取z转过的角度
			//穿件旋转动画
			RotateAnimation ra = new RotateAnimation(currentDegree,-degree,Animation.RELATIVE_TO_SELF,0.5f
					,Animation.RELATIVE_TO_SELF,0.5f);
			ra.setDuration(100);//动画持续时间
			image.startAnimation(ra);
			currentDegree = -degree;
			break;
		}
	}
}

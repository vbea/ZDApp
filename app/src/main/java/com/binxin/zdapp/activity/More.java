package com.binxin.zdapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.net.Uri;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class More extends Activity
{
	private LinearLayout titleLayout;
/**首次定义活动类*/
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
	//定义按钮
		ImageButton button_morer = (ImageButton) findViewById(R.id.btn_close);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		
		button_morer.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
	}

	/*public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		//这里构建KeyEvent对象，其功能为返回键的功能
		//因此我们按任意键都会执行返回键功能
		KeyEvent key = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);

		//这里传入的参数就是我们自己构建的KeyEvent对象key
		return super.onKeyDown(key.getKeyCode(), key);
	}*/
	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		MyThemes.setThemes(this,titleLayout);
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
	
}

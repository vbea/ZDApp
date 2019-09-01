package com.binxin.zdapp.colors;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class Main extends Activity
{
	LinearLayout titleLayout;
	@Override
	public void onCreate(Bundle savedInstenceState)
	{
		super.onCreate(savedInstenceState);
		setContentView(R.layout.scrcolor);
		
		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		Button star = (Button) findViewById(R.id.sc_star);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		
		close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});
		star.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(Main.this,Color.class);
				startActivity(intent);
				Toast.makeText(Main.this,"开始进行颜色测试，点击屏幕即可切换",Toast.LENGTH_SHORT).show();
			}
		});
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
		MyThemes.setThemes(this, titleLayout);
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
}

package com.binxin.zdapp.colors;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.LinearLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class End extends Activity
{
	LinearLayout titleLayout;
	RadioButton per,ord,bad;
	@Override
	public void onCreate(Bundle savedInstenceState)
	{
		super.onCreate(savedInstenceState);
		setContentView(R.xml.end);

		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		Button star = (Button) findViewById(R.id.sc_restar);
		Button end = (Button) findViewById(R.id.sc_end);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		per = (RadioButton) findViewById(R.id.rdb_perfect);
		ord = (RadioButton) findViewById(R.id.rdb_ordinary);
		bad = (RadioButton) findViewById(R.id.rdb_bad);
		setMyTheme();
		
		close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});
		end.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (getSelectMessage() != null)
					Toast.makeText(getApplicationContext(),getSelectMessage(),Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		star.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(End.this,Color.class);
				startActivity(intent);
				Toast.makeText(End.this,"开始进行颜色测试，点击屏幕即可切换",Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
	
	private String getSelectMessage()
	{
		if (per.isChecked())
		{
			return "恭喜亲，您的屏幕很完美！";
		}
		else if (ord.isChecked())
		{
			return "您的屏幕很一般，再接再厉！";
		}
		else if (bad.isChecked())
		{
			return "亲，您的屏幕很不好，要不在试一次？";
		}
		else
		{
			return null;
		}
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

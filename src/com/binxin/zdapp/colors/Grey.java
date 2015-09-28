package com.binxin.zdapp.colors;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import com.binxin.zdapp.R;


public class Grey extends Activity
{
	@Override
	public void onCreate(Bundle savedInstenceState)
	{
		super.onCreate(savedInstenceState);
		setContentView(R.xml.colors_grey);

		Button next = (Button) findViewById(R.id.btn_next);
		next.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent next = new Intent(Grey.this,Red_com.class);
				startActivity(next);
				finish();
				Toast.makeText(Grey.this,"开始进行颜色渐变测试，滑动屏幕到最底部点击按钮可切换到下一项测试",Toast.LENGTH_SHORT).show();
			}
		});
	}
}

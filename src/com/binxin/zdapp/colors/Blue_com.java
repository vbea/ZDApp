package com.binxin.zdapp.colors;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import com.binxin.zdapp.R;

public class Blue_com extends Activity
{
	@Override
	public void onCreate(Bundle savedInstenceState)
	{
		super.onCreate(savedInstenceState);
		setContentView(R.xml.colors_com_blue);

		Button next = (Button) findViewById(R.id.btn_next);
		next.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent next = new Intent(Blue_com.this,B_W.class);
				startActivity(next);
				finish();
				Toast.makeText(Blue_com.this,"开始进行对比度测试，点击屏幕即可切换",Toast.LENGTH_SHORT).show();
			}
		});
	}
}

package com.binxin.zdapp.colors;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import com.binxin.zdapp.R;

public class Green extends Activity
{
	@Override
	public void onCreate(Bundle savedInstenceState)
	{
		super.onCreate(savedInstenceState);
		setContentView(R.xml.colors_green);
		getWindow().setFlags(0x08000000,0x8000000);

		Button next = (Button) findViewById(R.id.btn_next);
		next.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent next = new Intent(Green.this,Blue.class);
				startActivity(next);
				finish();
			}
		});
	}
}

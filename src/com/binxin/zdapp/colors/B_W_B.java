package com.binxin.zdapp.colors;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import com.binxin.zdapp.R;

public class B_W_B extends Activity
{
	@Override
	public void onCreate(Bundle savedInstenceState)
	{
		super.onCreate(savedInstenceState);
		setContentView(R.xml.colors_b_w_b);

		Button next = (Button) findViewById(R.id.btn_next);
		next.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent next = new Intent(B_W_B.this,W_B_W.class);
				startActivity(next);
				finish();
			}
		});
	}
}

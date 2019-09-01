package com.binxin.zdapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.*;
import android.widget.*;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class Vbea_about extends Activity
{
	private LinearLayout titleLayout;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{//*邠心工作室
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vbeatelier);
		
		ImageButton bk = (ImageButton) findViewById(R.id.btn_close);
		Button wx = (Button) findViewById(R.id.intoWeixin);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		
		bk.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		wx.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				/*try
				{
					Intent intent = new Intent();
					intent.setClassName("com.tencent.mm", "com.tencent.mm.ui.qrcode.GetQRCodeInfoUI");
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setData(Uri.parse("http://weixin.qq.com/r/OmPJ0efE4LqfraOy9zYo"));
					startActivity(intent);
				}
				catch (Exception e)
				{*/
					//Toast.makeText(getApplicationContext(), "如果你的手机安装了微信，请点击微信打开本链接。如果你未安装微信，请点击任意浏览器下载微信", Toast.LENGTH_SHORT).show();
					Uri uri = Uri.parse("weixin://qr/OmPJ0efE4LqfraOy9zYo");
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setData(uri);
					startActivity(intent);
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

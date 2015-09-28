package com.binxin.zdapp.activity;

import java.lang.reflect.Field;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.net.Uri;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.content.Intent;
import android.content.Context;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.DialogInterface;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.classes.DecHelper;

public class DecodeShow extends Activity
{
	private TextView txt1;
	private boolean isDecryp;
	Button btn_copy,btn_brow,btn_encp;
	private String strs;
	LinearLayout titleLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.decode_show);

		txt1 = (TextView) findViewById(R.id.txt1);
		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		btn_copy = (Button) findViewById(R.id.decode_GBK);
		btn_brow = (Button) findViewById(R.id.decode_Brow);
		btn_encp = (Button) findViewById(R.id.decode_encode);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		setMyTheme();
		initIntent();
		//返回
		close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		btn_copy.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cbm.setPrimaryClip(ClipData.newPlainText("二维码信息",strs));
				Toast.makeText(getApplicationContext(),"复制成功",Toast.LENGTH_SHORT).show();
			}
		});
		btn_brow.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (strs.indexOf("http://") == 0 || strs.indexOf("https://") == 0 || strs.indexOf("ftp://") == 0)
				{
					Uri content_url = Uri.parse(strs);
					Intent intent = new Intent(Intent.ACTION_VIEW, content_url);
					startActivity(intent);
				}
				else
				{
					Uri search = Uri.parse("http://m.baidu.com/s?word="+strs);
					Intent intent1 = new Intent(Intent.ACTION_VIEW, search);
					startActivity(intent1);
				}
			}
		});
		btn_encp.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				encrypDialog();
			}
		});
	}

	private void initIntent()
	{
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			strs = bundle.getString("msg");
			try
			{
				if (strs.indexOf("数据加密:") != -1 || strs.indexOf("#正德应用扫一扫#") != -1)
				{
					btn_encp.setVisibility(View.VISIBLE);
					encrypDialog();
				}
				else
				{
					btn_encp.setVisibility(View.GONE);
					txt1.setText("扫描结果：\n"+strs);
				}
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	private void encrypDialog()
	{
		try
		{
			setTheme(android.R.style.Theme_Holo_Light);
			//自定义一个带输入的对话框由TextView和EditText构成
			final LayoutInflater factory = LayoutInflater.from(this);
			final View dialogview = factory.inflate(R.layout.file_rename, null);
			//设置TextView的提示信息
			((TextView) dialogview.findViewById(R.id.TextView01)).setText("请输入密码");
			//设置EditText输入框初始值
			final EditText edtPsd = (EditText) dialogview.findViewById(R.id.EditText01);
			edtPsd.setText("");
			edtPsd.setHint("请输入密码(可不填)");

			final String [] stres;
			try
			{
				stres = strs.substring(1).split("#");
			}
			catch (Exception e)
			{
				stres = strs.split(":");
			}
			Builder bd = new Builder(this);
			bd.setIcon(R.drawable.ic_icon);
			bd.setTitle("二维码解密");
			bd.setView(dialogview);
			bd.setPositiveButton("确定",new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface d,int swich)
				{
					try
					{
						DecHelper dec = new DecHelper(edtPsd.getText().toString().trim());
						strs = dec.decrypt(stres[1]);
						txt1.setText("扫描结果：\n"+strs);
						d.dismiss();
						btn_encp.setVisibility(View.GONE);
						/*try
						{
							Field field = d.getClass().getSuperclass().getDeclaredField("mShowing");
							field.setAccessible(true);
							field.set(d, true);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}*/
					}
					catch (Exception e)
					{
						Toast.makeText(getApplicationContext(),"密码错误！",Toast.LENGTH_SHORT).show();
						/*try
						{
							Field field = d.getClass().getSuperclass().getDeclaredField("mShowing");
							field.setAccessible(true);
							field.set(d, false);
						}
						catch (Exception e1)
						{
							e1.printStackTrace();
						}*/
					}
				}
			});
			bd.setNegativeButton("取消",new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface d,int swich)
				{
					txt1.setText("扫描结果：\n"+strs);
					d.cancel();
					/*try
					{
						Field field = d.getClass().getSuperclass().getDeclaredField("mShowing");
						field.setAccessible(true);
						field.set(d, true);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}*/
				}
			});
			bd.setCancelable(false);
			bd.create();
			bd.show();
		}
		catch (Exception e)
		{
			txt1.setText("扫描结果：\n"+strs);
		}
	}
	/*public String StrtoChar(String str,String charsetName,String toDecodeName)
	{
		String CharStr = "";
		try
		{
			byte[] b = str.getBytes(CharStr);
			CharStr = new String(b, toDecodeName);
		}
		catch(UnsupportedEncodingException e)
		{

		}
		return CharStr;
	}*/
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

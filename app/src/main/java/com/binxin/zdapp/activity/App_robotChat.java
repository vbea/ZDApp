package com.binxin.zdapp.activity;

import java.net.URLEncoder;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.View.OnLayoutChangeListener;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.provider.Settings;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.ExceptionHandler;
import com.binxin.zdapp.classes.MyThemes;
import com.binxin.zdapp.receiver.ConnectionChangeReceiver;
import com.binxin.zdapp.classes.Common;

public class App_robotChat extends Activity
{
	private static int CHILD_MSG_ID = 0;
	//private static int CHILD_FROM_ID = 0;
	//private static int CHILD_ERROR_ID = 0;
	private boolean NET_STATE = true;
	private int scrollY = 0;
	private EditText edit;
	private Button btn_netNull,btnSend;
	private TextView txtNouse;
	private ScrollView scl_msg;
	private LinearLayout titleLayout,adapter;
	private ConnectionChangeReceiver myReceive;
	private LayoutInflater inflate;
	private Builder menuDialog;
	private String message;
	private String result = "";
	private View fromview;
	private TextView txtFrom;
	private View sendview;
	private TextView txtSend;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		ExceptionHandler handler = ExceptionHandler.getInstance();
		handler.init(this);
        setContentView(R.layout.robotchat);
		
		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		edit = (EditText) findViewById(R.id.txtEdit);
		adapter = (LinearLayout) findViewById(R.id.msgAdapter);
		btnSend = (Button) findViewById(R.id.btnSend);
		btn_netNull = (Button) findViewById(R.id.xd_btn_netnull);
		scl_msg = (ScrollView) findViewById(R.id.scl_msg);
		txtNouse = (TextView) findViewById(R.id.txt_nouse);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		inflate = LayoutInflater.from(App_robotChat.this);
		//scl_msg.setScrollBar
		btnState();
		addFromMsg("您好，主人\n送你一个大大的么么哒(^з^)");
		registerReceiver();
		new CheckNetwork().start();
		
		close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				NET_STATE = false;
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		btnSend.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if(edit.getText().toString().trim().length() == 0)
				{
					addFromMsg("只要输入文字就可以跟我对话！");
					edit.setText("");
					mHandler.sendEmptyMessage(7);
					return;
				}
				if (!myReceive.IsNetWork)
				{
					addErrorMsg("网络连接失败，请检查你的网络");
					mHandler.sendEmptyMessage(7);
					return;
				}
				addSendMsg(edit.getText().toString());
				mHandler.sendEmptyMessage(7);
				message = edit.getText().toString();
				try
				{
					String msg = URLEncoder.encode(message, "utf-8");
					String requesturl = "http://www.tuling123.com/openapi/api?key=58ef3070adf8b6ad9903256b12f479db&info="+msg;
					//text.setText(requesturl);
					edit.setText("");
					final HttpGet httpget = new HttpGet(requesturl);
					final HttpClient httpClient = new DefaultHttpClient();
					//Toast.makeText(getApplicationContext(),INFO,Toast.LENGTH_SHORT).show();
					
					new Thread()
					{
						@Override
						public void run()
						{
							try
							{
								//mHandler.sendEmptyMessage(1);
								HttpResponse response = httpClient.execute(httpget);
								//mHandler.sendEmptyMessage(2);
								if(response.getStatusLine().getStatusCode()==200&&response != null)//200即正确的返回码
								{
									HttpEntity entity = response.getEntity();
									InputStream is = entity.getContent();
									InputStreamReader isr = new InputStreamReader(is);
									BufferedReader reader = new BufferedReader(isr);
									//String result = EntityUtils.toString(response.getEntity());
									if (getJson(reader.readLine()))
										mHandler.sendEmptyMessage(4);
									else
										mHandler.sendEmptyMessage(3);
								}
								//super.run();
							}
							catch(Exception e)
							{
								e.printStackTrace();
								mHandler.sendEmptyMessage(5);
							}
						}
					}.start();
				}
				catch(IOException e)
				{
					e.printStackTrace();
					mHandler.sendEmptyMessage(6);
				}
			}
		});
		btn_netNull.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				startActivity(new Intent(Settings.ACTION_SETTINGS));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		edit.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
			{
				btnState();
			}

			@Override
			public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
			{
				
			}

			@Override
			public void afterTextChanged(Editable p1)
			{
				
			}
		});
		scl_msg.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View p1, MotionEvent p2)
			{
				//Toast.makeText(getApplicationContext(),"滚动:x("+scl_msg.getScaleX()+")y("+scl_msg.getScaleY()+")/x,y["+scl_msg.getScrollX()+","+scl_msg.getScrollY()+"]",Toast.LENGTH_SHORT).show();
				//edit.setText("Y:"+scl_msg.getScrollY()+",Size:"+scl_msg.getScrollBarSize()+",Height"+scl_msg.getHeight());
				scrollY = scl_msg.getScrollY()+scl_msg.getHeight();
				return false;
			}
		});
		adapter.addOnLayoutChangeListener(new OnLayoutChangeListener()
		{
			@Override
			public void onLayoutChange(View p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, int p9)
			{
				scl_msg.scrollTo(0,scrollY-scl_msg.getHeight());
				//mHandler.sendEmptyMessage(7);
			}
		});
	}
	class CheckNetwork extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				while(NET_STATE)
				{
					mHandler.sendEmptyMessage(8);
					Thread.sleep(1000);
				}
			}
			catch (InterruptedException e)
			{
			}
		}
	}
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case 1:
					Toast.makeText(getApplicationContext(),"发起请求",Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(getApplicationContext(),"判断数据",Toast.LENGTH_SHORT).show();
					break;
				case 3:
					//Toast.makeText(getApplicationContext(),"获取数据",Toast.LENGTH_SHORT).show();
					/*if (message.equals("清屏"))
						adapter.removeAllViews();
					else*/
						addErrorMsg(result);
					mHandler.sendEmptyMessage(7);
					break;
				case 4:
					//Toast.makeText(getApplicationContext(),"返回结果："+result,Toast.LENGTH_SHORT).show();
					addFromMsg(result.replace("<br>","\n"));
					mHandler.sendEmptyMessage(7);
					break;
				case 5:
					Toast.makeText(getApplicationContext(),"连接超时",Toast.LENGTH_SHORT).show();
					break;
				case 6:
					Toast.makeText(getApplicationContext(),"IO异常",Toast.LENGTH_SHORT).show();
					break;
				case 7:
					scl_msg.scrollTo(0,adapter.getMeasuredHeight() - scl_msg.getHeight() + edit.getHeight() + txtNouse.getHeight());
					scrollY = scl_msg.getScrollY() + scl_msg.getHeight();
					break;
				case 8:
					netNullState();
					//btnState();
					break;
			}
			super.handleMessage(msg);
		}
	};
	private boolean getJson(String json)
	{
		String code = "";
		String jstr = "";
		boolean state = true;
		try
		{
			JSONObject jsobj = new JSONObject(json);
			//JSONObject jo = jsobj.getJSONObject("name");
			code= jsobj.getString("code");
			switch(code)
			{
				case "200000"://网址类数据
					jstr = jsobj.getString("text") + "\n" + jsobj.getString("url");
					break;
				case "100000"://文本类数据
				case "301000"://小说
				case "302000"://新闻
				case "304000"://应用、软件、下载
				case "305000"://列车
				case "306000"://航班
				case "307000"://团购
				case "308000"://优惠
				case "309000"://酒店
				case "310000"://彩票
				case "311000"://价格
				case "312000"://餐厅
					jstr = jsobj.getString("text");
					break;
				//以下为错误返回
				case "40001":
				case "40002":
				case "40003":
				case "40004":
				case "40005":
				case "40006":
				case "40007":
					state = false;
					jstr = jsobj.getString("text");
					break;
			}
			//+ "\ntext=" + jo.getString("text");
			result = jstr;
		}
		catch(JSONException e)
		{
			result = "Error!(" + code +")";
			state = false;
		}
		return state;
	}
	public void btnState()
	{
		if (edit.getText().toString().length() == 0)
		{
			btnSend.setEnabled(false);
			btnSend.setBackgroundResource(R.drawable.bg_btn_nosend);
		}
		else
		{
			btnSend.setEnabled(true);
			btnSend.setBackgroundResource(R.drawable.bg_btn_send);
		}
	}
	private void netNullState()
	{
		if (!myReceive.IsNetWork)
			btn_netNull.setVisibility(View.VISIBLE);
		else
			btn_netNull.setVisibility(View.GONE);
		//Common.showShortToast(getApplicationContext(),""+myReceive.IsNetWork);
	}
	/*private boolean isNetWork()
	{
		boolean net = false;
		ConnectivityManager conm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conm.getActiveNetworkInfo();
		if (info != null && info.isAvailable())
			net = true;
		return net;
	}*/
	private void addSendMsg(String msg)
	{
		sendview = inflate.inflate(R.layout.msgsend,null);
		txtSend = (TextView) sendview.findViewById(R.id.txt_msgsend);
		txtSend.setText(msg);
		sendview.setId(CHILD_MSG_ID);
		txtSend.setTag(sendview);
		txtSend.setOnLongClickListener(new SendOnLongClick());
		CHILD_MSG_ID++;
		adapter.addView(sendview);
	}
	private void addFromMsg(String msg)
	{
		fromview = inflate.inflate(R.layout.msgfrom,null);
		txtFrom = (TextView) fromview.findViewById(R.id.txt_msgfrom);
		txtFrom.setText(msg);
		txtFrom.setId(CHILD_MSG_ID);
		fromview.setId(CHILD_MSG_ID);
		txtFrom.setTag(fromview);
		txtFrom.setOnLongClickListener(new FromOnLongClick());
		CHILD_MSG_ID++;
		adapter.addView(fromview);
	}
	private void addErrorMsg(String msg)
	{
		/*View errorview = inflate.inflate(R.layout.errormsg,null);
		TextView txtErr = (TextView) errorview.findViewById(R.id.txt_msgerror);
		txtErr.setText(msg);
		errorview.setId(CHILD_ERROR_ID);
		CHILD_ERROR_ID++;
		adapter.addView(errorview);*/
		Common.showShortToast(getApplicationContext(),msg);
	}
	
	public class SendOnLongClick implements View.OnLongClickListener
	{
		@Override
		public boolean onLongClick(View v)
		{
			dialogMenu((TextView)v);
			return false;
		}
	}
	
	public class FromOnLongClick implements View.OnLongClickListener
	{
		@Override
		public boolean onLongClick(View v)
		{
			dialogMenu((TextView)v);
			return false;
		}
	}
	
	private void dialogMenu(final TextView view)
	{
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				switch(which)
				{
					case 0://复制
					{
						ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
						cbm.setPrimaryClip(ClipData.newPlainText("汐黛-聊天信息",view.getText().toString()));
						break;
					}
					case 1://删除
						adapter.removeView((View)view.getTag());
						break;
					case 2://清空聊天记录
						adapter.removeAllViews();
						break;
				}
			}
		};
		//显示操作菜单
		String[] menu={getString(R.string.dc_copy),getString(R.string.delete),getString(R.string.emptyr)};
		setTheme(android.R.style.Theme_DeviceDefault_Light);
	    menuDialog = new Builder(App_robotChat.this);
		menuDialog.setTitle(R.string.operate);
		menuDialog.setItems(menu,listener);
		menuDialog.create();
		menuDialog.show();
	}

	public void registerReceiver()
	{
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		myReceive = new ConnectionChangeReceiver();
		this.registerReceiver(myReceive, filter);
	}

	public void unregisterReceiver()
	{
		this.unregisterReceiver(myReceive);
	}

	@Override
	protected void onResume()
	{
		netNullState();
		MyThemes.setThemes(this, titleLayout);
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		NET_STATE = false;
		unregisterReceiver();
		super.onDestroy();
	}

}

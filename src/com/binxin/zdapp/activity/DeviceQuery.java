package com.binxin.zdapp.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.os.BatteryManager;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.util.DisplayMetrics;
import android.app.ActivityManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.view.View;
import android.view.View.OnClickListener;
import java.util.*;
import android.os.Handler;
import android.os.Message;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.SharedPreferences;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class DeviceQuery extends Activity
{
	private TextView model, ver, sd, mac, rom, ram, time, cpum, cpul, fbl, batinfo;
	private LinearLayout titleLayout;
	private IntentFilter mIntentFilter;
	//private BroadcastReceiver mIntentReceiver;
	private String batteryFos;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hard_device);
		/*SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.hard_device, new String[]
		{ "title", "info" },
		new int[]
		{ R.id.title, R.id.info });
		setListAdapter(adapter);
		/*/
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		//定义要显示的TEXTVIEW的id
		ImageButton close = (ImageButton) findViewById(R.id.btn_close);
		cpul = (TextView) findViewById(R.id.device_text_cpuhz);
		cpum = (TextView) findViewById(R.id.device_text_cpumodel);
		fbl = (TextView) findViewById(R.id.device_text_sclv);
		mac = (TextView) findViewById(R.id.device_text_mac);
		model = (TextView) findViewById(R.id.device_text_model);
		ram = (TextView) findViewById(R.id.device_text_ram);
		rom = (TextView) findViewById(R.id.device_text_rom);
		sd = (TextView) findViewById(R.id.device_text_sdcard);
		time = (TextView) findViewById(R.id.device_text_ontime);
		ver = (TextView) findViewById(R.id.device_text_ver);
		batinfo = (TextView) findViewById(R.id.device_text_battery);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);

		close.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		Timer timer = new Timer(true);
		timer.schedule(task, 1500, 100);
	}
	@Override
	public void omStart()
	{
		/*List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;*/
		// SD卡容量 
		if (ExistSDCard())
		{
			sd.setText("总大小：" + formatSize(getTotalSDSize()) + "\n已用空间：" + formatSize(getTotalSDSize() - getAvailSDSize()) + "\n剩余空间：" + formatSize(getAvailSDSize()));
		}
		else
		{
			sd.setText("当前未挂载SD卡");
		}
		// CPU型号与频率  
		String[] cpuInfo = getCpuInfo();
		cpum.setText(cpuInfo[0]);
		cpul.setText(getCPUfreq());
		// RAM 大小  
		float ram1 = getTotalRAMSize() - getAvailRAMSize();
		float ram2 = getTotalRAMSize();
		String rampref = new DecimalFormat("#0.00").format(ram1/ram2*100);
		ram.setText("总大小:" + formatSize(getTotalRAMSize()) + "\n已用空间：" + formatSize(getTotalRAMSize() - getAvailRAMSize()) + "\n可用空间：" + formatSize(getAvailRAMSize())+"\n使用率："+rampref+"%");
		// ROM 大小  
		float rom1 = getTotalROMSize() - getAvailROMSize();
		float rom2 = getTotalROMSize();
		String rompref = new DecimalFormat("#0.00").format(rom1/rom2*100);
		rom.setText("总大小：" + formatSize(getTotalROMSize()) + "\n已用空间：" + formatSize(getTotalROMSize() - getAvailROMSize()) + "\n可用空间：" + formatSize(getAvailROMSize())+"\n使用率："+rompref+"%");
		// MAC 地址  
		mac.setText(getMacAddress());
		// 设备型号  
		model.setText(android.os.Build.MODEL);
		// 系统版本  
		ver.setText("Android " + android.os.Build.VERSION.RELEASE);
		// 屏幕分辨率  
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels; // 当前分辨率宽度  
		int height = dm.heightPixels; // 当前分辨率高度  
		fbl.setText(width + "×" + height);
		// 开机时长
		/*while (true)
		{*/
		batinfo.setText(batteryFos);
		time.setText(getElapsedTime());
		/*Timer timer = new Timer(true);
		timer.schedule(task, 1000, 1000);*/
		//}
		
	}
	// SD卡 可用空间
	public long getAvailSDSize()
	{
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)  
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量  
		long availBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小  
		return availBlocks * blockSize; // Byte 
	}
	// SD卡总大小  
	public long getTotalSDSize()
	{
		// 取得SD卡文件路径  
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)  
		long blockSize = sf.getBlockSize();
		// 获取所有数据块数  
		long totalBlocks = sf.getBlockCount();
		// 返回SD卡大小  
		return totalBlocks * blockSize; // Byte
	}
	// 获得CPU信息  
	public String[] getCpuInfo()
	{
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" }; // 1-cpu型号 //2-cpu频率  
		String[] arrayOfString;
		try
		{
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++)
			{
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		}
		catch (IOException e)
		{
		}
		// Log.i(TAG, "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);  
		return cpuInfo;
	}
	//CPU频率
	public String getCPUfreq()
	{
		String CPUfreq = "N/A";
		String str1 = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq";
		String str2 = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
		String str3 = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
		try
		{
			FileReader fr1 = new FileReader(str1);
			FileReader fr2 = new FileReader(str2);
			FileReader fr3 = new FileReader(str3);
			BufferedReader br1 = new BufferedReader(fr1,8192);
			BufferedReader br2 = new BufferedReader(fr2,8192);
			BufferedReader br3 = new BufferedReader(fr3,8192);
			int freq1 = Integer.parseInt(br1.readLine());
			int freq2 = Integer.parseInt(br2.readLine());
			int freq3 = Integer.parseInt(br3.readLine());
			CPUfreq = "最低频率："+freq1/1000+"MHz\n当前频率："+freq2/1000+"MHz\n最高频率："+freq3/1000+"MHz";
		}
		catch(IOException e)
		{
			
		}
		return CPUfreq;
	}
	// RAM 总大小  
	public long getTotalRAMSize()
	{
		String str1 = "/proc/meminfo";// 系统内存信息文件  
		String str2;
		String[] arrayOfString;
		long totalSize = 0;
		try
		{
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小  
			arrayOfString = str2.split("\\s+");
			// 获得系统总内存，单位是KB，乘以1024转换为Byte  
			totalSize = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
			localBufferedReader.close();
		}
		catch (IOException e)
		{
		}
		return totalSize;
	}
	// RAM 可用空间
	public long getAvailRAMSize()
	{
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(memInfo);
		return memInfo.availMem;
	}
	// ROM总大小  
	public long getTotalROMSize()
	{
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize; // Byte  
	}
	// ROM可用空间  
	public long getAvailROMSize()
	{
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize; // Byte  
	}
	// MAC地址  
	public String getMacAddress()
	{
		String macStr = "";
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (wifiInfo.getMacAddress() != null)
		{
			macStr = wifiInfo.getMacAddress();// MAC地址  
		}
		else
		{
			macStr = "null";
		}
		return macStr;
	}
	// 取得开机时长  
	public String getElapsedTime()
	{
		long ut = SystemClock.elapsedRealtime() / 1000;
		if (ut == 0)
		{
			ut = 1;
		}
		int m = (int) ((ut / 60) % 60);
		int h = (int) ((ut / 3600));
		int s = (int) (ut % 60);
		String gi = "";
		String mi = "";
		String hi = "";
		if (s < 10 && s >= 0)
		{
			gi = "0";
		}
		else
		{
			gi = "";
		}
		if (m < 10 && m >= 0)
		{
			mi = "0";
		}
		else
		{
			mi = "";
		}
		if (h < 10 && h >= 0)
		{
			hi = "0";
		}
		else
		{
			hi = "";
		}
		String timesed="";
		if (h < 24)
		{
			timesed=hi + h + "时" + mi + m + "分" + gi + s + "秒";
		}
		else
		{
			int d = h / 24;
			
			if (h % 24 < 10)
			{
				hi = "0";
			}
			else
			{
				hi = "";
			}
			timesed=d + "天" + hi + (h % 24)+ "时" + mi + m + "分" + gi + s + "秒";
		}
		return timesed;
	}
	// 判断存储卡是否存在  
	public boolean ExistSDCard()
	{
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// 初始化数据  
	public String formatSize(long size)
	{
		String suffix = "B";// Byte 
		float fSize = 0;
		if (size >= 1024)
		{
			suffix = "KB";
			fSize = size / 1024;
			if (fSize >= 1024)
			{
				suffix = "MB";
				fSize /= 1024;
			}
			if (fSize >= 1024)
			{
				suffix = "GB";
				fSize /= 1024;
			}
		}
		else
		{
			fSize = size;
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
		StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
		if (suffix != null)
		{
			resultBuffer.append(suffix);
		}
		return resultBuffer.toString();
	}
	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver()
	{  
		@Override  
		public void onReceive(Context context, Intent intent)
		{  
			String action = intent.getAction();  
			//要看看是不是我们要处理的消息  
			if (action.equals(Intent.ACTION_BATTERY_CHANGED))
			{
				//电池电量，数字  
				/*Log.d("Battery", "" + intent.getIntExtra("level", 0));                 
				 //电池最大容量  
				 Log.d("Battery", "" + intent.getIntExtra("scale", 0));                 
				 //电池伏数  
				 Log.d("Battery", "" + intent.getIntExtra("voltage", 0));                 
				 //电池温度  
				 Log.d("Battery", "" + intent.getIntExtra("temperature", 0));  */
				String batteryState = "未知";
				//电池状态，返回是一个数字 
				switch(intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN))
				{
					case BatteryManager.BATTERY_STATUS_CHARGING:
						batteryState = "充电中";
						break;
					case BatteryManager.BATTERY_STATUS_DISCHARGING:
						batteryState = "放电中";
						break;
					case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
						batteryState = "未充电";
						break;
					case BatteryManager.BATTERY_STATUS_FULL:
						batteryState = "已充满";
						break;
					case BatteryManager.BATTERY_STATUS_UNKNOWN:
						batteryState = "未知";
						break;
				}
				//Log.d("Battery", "" + intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN));  
				//充电类型 BatteryManager.BATTERY_PLUGGED_AC 表示是充电器，不是这个值，表示是 USB  
				//Log.d("Battery", "" + intent.getIntExtra("plugged", 0));  
				String batteyHel="状态良好";
				switch(intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_GOOD))
				{
						//电池健康情况，返回也是一个数字  
					case BatteryManager.BATTERY_HEALTH_GOOD:
						batteyHel = "状态良好";
						break;
					case BatteryManager.BATTERY_HEALTH_OVERHEAT:
						batteyHel = "电池过热";
						break;
					case BatteryManager.BATTERY_HEALTH_DEAD:
						batteyHel = "电池没有电";
						break;
					case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
						batteyHel = "电池电压过高";
						break;
					case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
						batteyHel = "未知错误";
						break;
				}
				String voltages;
				float volt = intent.getIntExtra("voltage", 0);
				voltages = (volt / 1000)+ "V";
				//Log.d("Battery", "" + intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN));  
				batteryFos = "电池电量：" + intent.getIntExtra("level", 0) + "%\n电池电压：" + voltages +"\n电池温度："+intent.getIntExtra("temperature", 0)/10+"℃\n电池状态："+batteryState+"\n健康状况："+batteyHel;
			}  
		}  
	};
	final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					omStart();
					break;
			}
			super.handleMessage(msg);
		}
	};
	TimerTask task = new TimerTask()
	{
		public void run()
		{
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};
	public void setMyTheme()
	{
		SharedPreferences sube = getSharedPreferences("zdapp", Context.MODE_PRIVATE);
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
		registerReceiver(mIntentReceiver, mIntentFilter);
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
	/*@Override
	class getOnTime implements Runnable//()new Runnable()
	{
		@Override
		public void run()
		{
				while (!Thread.currentThread().isInterrupted())
				{
					Message message = new Message();
					message.what = DeviceQuery.REFRESH;
					//
					DeviceQuery.this.handler.sendMessage(message);
					message.what = DeviceQuery.REFRESH;
					//time.setText(getElapsedTime());
					//new getOnTime().start();
				//}
			}
			/*catch (Exception e)
			{
				time.setText(getElapsedTime());
			}
		}
	}*/
	/*private void onTime()
	{
		while (true)
		{
			new getOnTime().start();
		}
	}*/
}

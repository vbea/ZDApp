package com.binxin.zdapp.activity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import java.security.*;
import android.provider.*;
import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;

public class App_wifiap extends Activity
{
	private final String TAG = "WifiSoftAP";
    public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    public static final int WIFI_AP_STATE_DISABLING = 0;
    public static final int WIFI_AP_STATE_DISABLED = 1;
    public static final int WIFI_AP_STATE_ENABLING = 2;
    public static final int WIFI_AP_STATE_ENABLED = 3;
    public static final int WIFI_AP_STATE_FAILED = 4;
    TextView result;
	WifiManager wifiManager;
	WifiReceiver receiverWifi;
	List<ScanResult> wifiList;
    private List<WifiConfiguration> wifiConfiguration;
	StringBuilder resultList = new StringBuilder();
	LinearLayout titleLayout;
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifiap);
		//setTitle("");
		result = (TextView) findViewById(R.id.result);
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		ImageButton back = (ImageButton)this.findViewById(R.id.btn08);
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		Button btnOpenAP = (Button)this.findViewById(R.id.btnOpenAP);
		btnOpenAP.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				try
				{
				if (!isApEnabled())
				{
					setWifiApEnabled(true);
				}
				Toast.makeText(App_wifiap.this, "Wi-Fi已关闭", Toast.LENGTH_SHORT).show();
				Toast.makeText(App_wifiap.this, "热点共享已打开",Toast.LENGTH_SHORT).show();
				}
				catch(Exception e)
				{
					Toast.makeText(App_wifiap.this, "抱歉，您的设备不支持",Toast.LENGTH_SHORT);
				}
				
			}
		});
		Button btnCloseAP = (Button)this.findViewById(R.id.btnCloseAP);
		btnCloseAP.setOnClickListener(new OnClickListener() {
				public void onClick(View v) 
				{
					if (isApEnabled())
					{
						setWifiApEnabled(false);
					}
				}
			});
		Button btnScan = (Button)this.findViewById(R.id.btnScan);
		btnScan.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) 
				{
					if (!wifiManager.isWifiEnabled()) 
					{
						wifiManager.setWifiEnabled(true);
					}
					Toast.makeText(App_wifiap.this, "Wi-Fi已打开" , Toast.LENGTH_SHORT).show();
					//Toast.makeText(App_wifiap.this, "正在扫描" , Toast.LENGTH_LONG).show();
					StartScan();
					Toast.makeText(App_wifiap.this, "扫描完成" , Toast.LENGTH_SHORT).show();
				}
			});
		Button btnConnectAP = (Button)this.findViewById(R.id.btnConnectAP);
		btnConnectAP.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
					connectAP();
				}
			});
		Button ipSet = (Button)this.findViewById(R.id.wifiip);
		ipSet.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					Intent context1 = new Intent();
					context1.setAction(android.provider.Settings.ACTION_WIFI_IP_SETTINGS);
					startActivity(context1);
				}
			});
		Button btnSet = (Button)this.findViewById(R.id.btn_wifist);
		btnSet.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					//此为2.3与4.0的兼容段代码
					/*if (android.os.Build.VERSION.SDK_INT > 10)
					 {
					 Intent context = new Intent();
					 context.setAction(android.provider.Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
					 startActivity(context);
					 }
					 else
					 {*/
					Intent intent = new Intent();
					intent.setAction(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
					startActivity(intent);
				}
			});
		Button wifiSet = (Button)this.findViewById(R.id.btn_wifiast);
		wifiSet.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					Intent intent1 = new Intent();
					intent1.setAction(android.provider.Settings.ACTION_WIFI_SETTINGS);
					startActivity(intent1);
				}
			});
		Button btnGetConnectedIP = (Button)this.findViewById(R.id.btnGetConnectedIP);
		btnGetConnectedIP.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
					ArrayList<String> connectedIP = getConnectedIP();
					resultList = new StringBuilder();
					for(String ip : connectedIP)
					{
						resultList.append(ip);
						resultList.append("地址:\n");
					}
					result.setText(resultList);
				}
			});
		findViewById(R.id.btnGetOwmId).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					Log.d("ZSM", getLocalIpAddress());
					WifiInfo wifiinfo = wifiManager.getConnectionInfo();
					Log.v("getBSSID", wifiinfo.getBSSID() + "");
					Log.v("getHiddenSSID", wifiinfo.getBSSID() + "");
					Log.v("getIpAddress", Formatter.formatIpAddress(wifiinfo.getIpAddress()) + "");
					Log.v("getLinkSpeed", Formatter.formatIpAddress(wifiinfo.getLinkSpeed()) + "");
					Log.v("getMacAddress", wifiinfo.getMacAddress() + "");
					Log.v("getNetworkId", Formatter.formatIpAddress(wifiinfo.getNetworkId()) + "");
					Log.v("getRssi", wifiinfo.getRssi() + "");
					Log.v("getSSID", wifiinfo.getSSID() + "");
					DhcpInfo dhcpinfo = wifiManager.getDhcpInfo();
					Log.v("ipaddr", Formatter.formatIpAddress(dhcpinfo.ipAddress) + "");
					Log.v("gatewau", Formatter.formatIpAddress(dhcpinfo.gateway) + "");
					Log.v("netmask", Formatter.formatIpAddress(dhcpinfo.netmask) + "");
					Log.v("dns1", Formatter.formatIpAddress(dhcpinfo.dns1) + "");
					Log.v("dns1", Formatter.formatIpAddress(dhcpinfo.dns2) + "");
					Log.v("serverAddress", Formatter.formatIpAddress(dhcpinfo.serverAddress) + "");
					Log.d("ZSM", Formatter.formatIpAddress(dhcpinfo.serverAddress));
					Log.d("ZSM","ipaddr  "+ Formatter.formatIpAddress(dhcpinfo.ipAddress));
				}
			});
	}
	//此方法暂不可用
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
					 .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
						 .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }
    public String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }
	protected void onPause() {
		if (receiverWifi != null)
			unregisterReceiver(receiverWifi);
		super.onPause();
	}
	protected void onResume() {
		if (receiverWifi != null)
			registerReceiver(receiverWifi, new IntentFilter(
								 WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		super.onResume();
		MyThemes.setThemes(this,titleLayout);
	}
	public void StartScan() {
		//扫描wifi
		wifiManager.setWifiEnabled(true);
		receiverWifi = new WifiReceiver();
		registerReceiver(receiverWifi, new IntentFilter(
							 WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wifiManager.startScan();
		result.setText("扫描中...\n");
	}
	public boolean setWifiApEnabled(boolean enabled) {
		if (enabled) { // disable WiFi in any case
			wifiManager.setWifiEnabled(false);
		}
		try {
			WifiConfiguration apConfig = new WifiConfiguration();
			apConfig.SSID = "GossipDog";
			apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			apConfig.preSharedKey = "abcdefgh";
			Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
			return (Boolean) method.invoke(wifiManager, apConfig, enabled);
		} catch (Exception e) {
			Log.e(TAG, "Cannot set WiFi AP state", e);
			return false;
		}
	}
	public int getWifiApState() {
		try {
			Method method = wifiManager.getClass().getMethod("getWifiApState");
			return (Integer) method.invoke(wifiManager);
		} catch (Exception e) {
			Log.e(TAG, "Cannot get WiFi AP state", e);
			return WIFI_AP_STATE_FAILED;
		}
	}
	public boolean isApEnabled() {
        int state = getWifiApState();
        return WIFI_AP_STATE_ENABLING == state || WIFI_AP_STATE_ENABLED == state;
	}
    //GossipDog
    public void connectAP() {
		WifiConfiguration gossipDog = new WifiConfiguration();
        for (WifiConfiguration ap : wifiConfiguration) {
			if (ap.SSID == "GossipDog") {
				gossipDog = ap;
			}
        }
        if (gossipDog != null) {
			gossipDog.preSharedKey = "abcdefgh";
			gossipDog.networkId = wifiManager.addNetwork(gossipDog);
			wifiManager.enableNetwork(gossipDog.networkId, true);
			result.setText("Wi-Fi AP");
        }
    }
    //获取所有连接到本wifi热点的手机IP地址
    private ArrayList<String> getConnectedIP() {
        ArrayList<String> connectedIP = new ArrayList<String>();
        try {
			BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
				Log.d("meng", line);
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String ip = splitted[0];
                    connectedIP.add(ip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectedIP;
    }
	class WifiReceiver extends BroadcastReceiver
	{
		public void onReceive(Context c, Intent intent)
		{
			resultList = new StringBuilder();
			wifiList = wifiManager.getScanResults();
			for (int i = 0; i < wifiList.size(); i++) 
			{
				resultList.append(new Integer(i + 1).toString() + ".");
				resultList.append((wifiList.get(i)).toString());
				resultList.append("\n\n");
			}
			result.setText(resultList);
			wifiConfiguration = wifiManager.getConfiguredNetworks();
		}
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
}
